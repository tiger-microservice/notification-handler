package vn.tiger.notification.services.notification;

import com.tiger.common.utils.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.tiger.notification.constants.enums.NotifyBusinessType;
import vn.tiger.notification.constants.enums.ProcessStatus;
import vn.tiger.notification.dtos.request.NotificationInput;
import vn.tiger.notification.entities.mongoose.NotificationSendingHistory;
import vn.tiger.notification.events.NotificationWarningProducer;
import vn.tiger.notification.events.notification.NotificationRetryProducer;
import vn.tiger.notification.repositories.mongoose.NotificationHistoryRepository;
import vn.tiger.notification.utils.TimeoutUtil;

import java.time.LocalDateTime;

import static vn.tiger.notification.constants.enums.ProcessStatus.SUCCESS;

@Slf4j
@Service("NotifyResultService")
@RequiredArgsConstructor
public class NotifyResultService implements NotifyBusinessService {

    private static final Integer MAX_RETRY_TIMES = 3;

    final NotificationRetryProducer notificationRetryProducer;
    final NotificationWarningProducer notificationWarningProducer;
    final NotificationHistoryRepository notificationHistoryRepository;

    @Override
    public boolean isNotifyBusinessType(NotifyBusinessType notifyBusinessType) {
        return NotifyBusinessType.A.equals(notifyBusinessType);
    }

    @Override
    public void businessLogic(NotificationInput obj) {
        // check history by id
        NotificationSendingHistory notificationSendingHistory = notificationHistoryRepository
                .findById(obj.getId().toString())
                .orElse(NotificationSendingHistory.builder()
                        .id(obj.getId().toString())
                        .messageJson(ObjectMapperUtil.castToString(obj))
                        .receive(obj.getReceive())
                        .userId(obj.getUserId())
                        .retryNumber(0)
                        .build());
        // case success
        if (ProcessStatus.SUCCESS.equals(obj.getProcessStatus())) {
            log.warn("[handlerNotifyResult] msg id {} success", obj.getId());
            notificationSendingHistory.setProcessStatus(SUCCESS.name());
            notificationSendingHistory.setCreatedDate(LocalDateTime.now());
            notificationSendingHistory.setCreatedUser(obj.getReceive());

            // save into database
            notificationHistoryRepository.save(notificationSendingHistory);
            return;
        }

        // case error, push into retry process
        // check retry times = MAX_RETRY_TIMES
        notificationSendingHistory.setProcessMessage(obj.getErrorMsg());
        if (MAX_RETRY_TIMES.equals(notificationSendingHistory.getRetryNumber())) {
            log.warn("[handlerNotifyResult] msg id {} retry max times", obj.getId());
            this.notificationWarningProducer.sendNotifyWarningError(obj);
        } else {
            notificationSendingHistory.setRetryNumber(notificationSendingHistory.getRetryNumber() + 1);
            log.warn("[handlerNotifyResult] msg id {} retry times {}", obj.getId(), notificationSendingHistory.getRetryNumber());
            var retryNumber = notificationSendingHistory.getRetryNumber();
            // push retry message
            TimeoutUtil.setTimeout(() -> this.notificationRetryProducer.sendNotifyRetry(obj),
                    retryNumber * 1000 // 1 seconds
            );
        }

        notificationHistoryRepository.save(notificationSendingHistory);
    }
}
