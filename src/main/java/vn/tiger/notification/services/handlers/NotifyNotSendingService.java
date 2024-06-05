package vn.tiger.notification.services.handlers;

import com.tiger.common.utils.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.tiger.notification.constants.enums.NotificationStatus;
import vn.tiger.notification.constants.enums.NotifyBusinessType;
import vn.tiger.notification.dtos.request.NotificationInput;
import vn.tiger.notification.entities.mongoose.NotificationHistory;
import vn.tiger.notification.mongoose.NotificationHistoryRepository;

import java.time.LocalDateTime;

import static vn.tiger.notification.constants.enums.ProcessStatus.SUCCESS;

@Slf4j
@Service("NotifyNotSendingService")
@RequiredArgsConstructor
public class NotifyNotSendingService  implements NotifyBusinessService {

    final NotificationHistoryRepository notificationHistoryRepository;

    @Override
    public boolean isNotifyBusinessType(NotifyBusinessType notifyBusinessType) {
        return NotifyBusinessType.B.equals(notifyBusinessType);
    }

    @Override
    public void businessLogic(NotificationInput obj) {
        try {
            // cast to object
            notificationHistoryRepository.save(NotificationHistory.builder()
                    .id(obj.getId().toString())
                    .notificationType(obj.getType().name())
                    .messageJson(ObjectMapperUtil.castToString(obj))
                    .receive(obj.getReceive())
                    .status(NotificationStatus.UNREAD.getStatus())
                    .retryNumber(0)
                    .processStatus(SUCCESS.name())
                    .createdDate(LocalDateTime.now())
                    .createdUser(obj.getReceive())
                    .build());
        } catch (Exception e) {
            log.error("[handlerNotifyNotInWhitelist] error {}", e.getMessage(), e);
            throw e;
        }
    }
}
