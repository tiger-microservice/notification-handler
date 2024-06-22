package vn.tiger.notification.services.sms;

import com.tiger.common.utils.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.tiger.notification.constants.enums.NotificationStatus;
import vn.tiger.notification.constants.enums.NotifyBusinessType;
import vn.tiger.notification.constants.enums.ProcessStatus;
import vn.tiger.notification.dtos.request.NotificationInput;
import vn.tiger.notification.entities.mongoose.SmsSendingHistory;
import vn.tiger.notification.events.NotificationWarningProducer;
import vn.tiger.notification.events.notification.NotificationRetryProducer;
import vn.tiger.notification.repositories.mongoose.SmsSendingHistoryRepository;

import java.time.LocalDateTime;

import static vn.tiger.notification.constants.enums.ProcessStatus.SUCCESS;

@Slf4j
@Service("NotifyResultService")
@RequiredArgsConstructor
public class SmsResultService implements SmsBusinessService {

    private static final Integer MAX_RETRY_TIMES = 3;

    final NotificationRetryProducer notificationRetryProducer;
    final NotificationWarningProducer notificationWarningProducer;
    final SmsSendingHistoryRepository smsSendingHistoryRepository;

    @Override
    public boolean isNotifyBusinessType(NotifyBusinessType notifyBusinessType) {
        return NotifyBusinessType.A.equals(notifyBusinessType);
    }

    @Override
    public void businessLogic(NotificationInput obj) {
        // check history by id
        SmsSendingHistory smsSendingHistory = smsSendingHistoryRepository
                .findById(obj.getId().toString())
                .orElse(SmsSendingHistory.builder()
                        .id(obj.getId().toString())
                        .messageJson(ObjectMapperUtil.castToString(obj))
                        .receive(obj.getReceive() == null ? "system" : obj.getReceive())
                        .status(NotificationStatus.UNREAD.getStatus())
                        .retryNumber(0)
                        .build());
        // case success
        if (ProcessStatus.SUCCESS.equals(obj.getProcessStatus())) {
            log.warn("[handlerNotifyResult] msg id {} success", obj.getId());
            smsSendingHistory.setProcessStatus(SUCCESS.name());
            smsSendingHistory.setCreatedDate(LocalDateTime.now());
            smsSendingHistory.setCreatedUser(obj.getReceive());

            // save into database
            smsSendingHistoryRepository.save(smsSendingHistory);
            return;
        }

        // case error, push into retry process
        // check retry times = MAX_RETRY_TIMES
        smsSendingHistory.setProcessMessage(obj.getErrorMsg());
        if (MAX_RETRY_TIMES.equals(smsSendingHistory.getRetryNumber())) {
            log.warn("[handlerNotifyResult] msg id {} retry max times", obj.getId());
            this.notificationWarningProducer.sendNotifyWarningError(obj);
        } else {
            smsSendingHistory.setRetryNumber(smsSendingHistory.getRetryNumber() + 1);
            log.warn("[handlerNotifyResult] msg id {} retry times {}", obj.getId(), smsSendingHistory.getRetryNumber());
            var retryNumber = smsSendingHistory.getRetryNumber();
            // push retry message
            setTimeout(() -> this.notificationRetryProducer.sendNotifyRetry(obj),
                    retryNumber * 1000 // 1 seconds
            );
        }

        smsSendingHistoryRepository.save(smsSendingHistory);
    }

    public static void setTimeout(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                log.error("[setTimeout] error {}", e.getMessage(), e);
            }
        }).start();
    }
}
