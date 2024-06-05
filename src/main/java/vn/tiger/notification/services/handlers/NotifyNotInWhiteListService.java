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

@Slf4j
@Service("NotifyNotInWhiteListService")
@RequiredArgsConstructor
public class NotifyNotInWhiteListService implements NotifyBusinessService {

    final NotificationHistoryRepository notificationHistoryRepository;

    @Override
    public boolean isNotifyBusinessType(NotifyBusinessType notifyBusinessType) {
        return NotifyBusinessType.A.equals(notifyBusinessType);
    }

    @Override
    public void businessLogic(NotificationInput obj) {
        try {
            // cast to object
            this.notificationHistoryRepository.save(NotificationHistory.builder()
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
