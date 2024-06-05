package vn.tiger.notification.services;

import com.tiger.common.utils.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.tiger.notification.constants.enums.NotificationStatus;
import vn.tiger.notification.constants.enums.ProcessStatus;
import vn.tiger.notification.dtos.request.NotificationInput;
import vn.tiger.notification.entities.mongoose.NotificationHistory;
import vn.tiger.notification.events.NotificationRetryProducer;
import vn.tiger.notification.events.NotificationWarningProducer;
import vn.tiger.notification.mongoose.NotificationHistoryRepository;

import java.time.LocalDateTime;

import static vn.tiger.notification.constants.enums.ProcessStatus.SUCCESS;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationHandlerService {

    private static final Integer MAX_RETRY_TIMES = 3;

    final NotificationRetryProducer notificationRetryProducer;
    final NotificationWarningProducer notificationWarningProducer;
    final NotificationHistoryRepository notificationHistoryRepository;

    public void handlerNotifyResult(String message) {
        try {
            // cast to object
            var obj = ObjectMapperUtil.castToObject(message, NotificationInput.class);

            // check history by id
            NotificationHistory notificationHistory = notificationHistoryRepository
                    .findById(obj.getId().toString())
                    .orElse(NotificationHistory.builder()
                            .id(obj.getId().toString())
                            .notificationType(obj.getType().name())
                            .messageJson(ObjectMapperUtil.castToString(obj))
                            .receive(obj.getReceive() == null ? "system" : obj.getReceive())
                            .status(NotificationStatus.UNREAD.getStatus())
                            .retryNumber(0)
                            .build());
            // case success
            if (ProcessStatus.SUCCESS.equals(obj.getProcessStatus())) {
                log.warn("[handlerNotifyResult] msg id {} success", obj.getId());
                notificationHistory.setProcessStatus(SUCCESS.name());
                notificationHistory.setCreatedDate(LocalDateTime.now());
                notificationHistory.setCreatedUser(obj.getReceive());

                // save into database
                notificationHistoryRepository.save(notificationHistory);
                return;
            }

            // case error, push into retry process
            // check retry times = MAX_RETRY_TIMES
            notificationHistory.setProcessMessage(obj.getErrorMsg());
            if (MAX_RETRY_TIMES.equals(notificationHistory.getRetryNumber())) {
                log.warn("[handlerNotifyResult] msg id {} retry max times", obj.getId());
                this.notificationWarningProducer.sendNotifyWarningError(obj);
            } else {
                notificationHistory.setRetryNumber(notificationHistory.getRetryNumber() + 1);
                log.warn("[handlerNotifyResult] msg id {} retry times {}", obj.getId(), notificationHistory.getRetryNumber());
                var retryNumber = notificationHistory.getRetryNumber();
                // push retry message
                setTimeout(() -> this.notificationRetryProducer.sendNotifyRetry(obj),
                        retryNumber * 1000 // 1 seconds
                        );
            }

            notificationHistoryRepository.save(notificationHistory);
        } catch (Exception e) {
            log.error("[handlerNotifyResult] error {}", e.getMessage(), e);
            throw e;
        }
    }

    public static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e) {
                log.error("[setTimeout] error {}", e.getMessage(), e);
            }
        }).start();
    }

    public void handlerNotifyNotInWhitelist(String message) {
        try {
            // cast to object
            var obj = ObjectMapperUtil.castToObject(message, NotificationInput.class);

            notificationHistoryRepository.save(NotificationHistory.builder()
                    .id(obj.getId().toString())
                    .notificationType(obj.getType().name())
                    .messageJson(ObjectMapperUtil.castToString(obj))
                    .receive(obj.getReceive() == null ? "system" : obj.getReceive())
                    .status(NotificationStatus.NONE.getStatus())
                    .retryNumber(0)
                    .processStatus(obj.getProcessStatus().toString())
                    .processMessage(obj.getErrorMsg())
                    .createdDate(LocalDateTime.now())
                    .createdUser("system")
                    .build());
        } catch (Exception e) {
            log.error("[handlerNotifyNotInWhitelist] error {}", e.getMessage(), e);
            throw e;
        }
    }
}
