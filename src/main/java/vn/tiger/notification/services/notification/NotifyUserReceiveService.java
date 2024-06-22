package vn.tiger.notification.services.notification;

import com.tiger.common.utils.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.tiger.notification.constants.enums.NotificationStatus;
import vn.tiger.notification.constants.enums.NotifyBusinessType;
import vn.tiger.notification.dtos.request.NotificationInput;
import vn.tiger.notification.entities.mongoose.NotificationUserReceive;
import vn.tiger.notification.repositories.mongoose.NotificationUserReceiveRepository;

import java.time.LocalDateTime;


@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyUserReceiveService implements NotifyBusinessService {

    final NotificationUserReceiveRepository notificationUserReceiveRepository;

    @Override
    public boolean isNotifyBusinessType(NotifyBusinessType notifyBusinessType) {
        return NotifyBusinessType.B.equals(notifyBusinessType);
    }

    @Override
    public void businessLogic(NotificationInput obj) {
        try {
            // cast to object
            notificationUserReceiveRepository.save(NotificationUserReceive.builder()
                    .id(obj.getId().toString())
                    .messageJson(ObjectMapperUtil.castToString(obj))
                    .receive(obj.getReceive())
                    .status(NotificationStatus.UNREAD.getStatus())
                    .createdDate(LocalDateTime.now())
                    .createdUser(obj.getReceive())
                    .build());
        } catch (Exception e) {
            log.error("[handlerNotifyNotInWhitelist] error {}", e.getMessage(), e);
            throw e;
        }
    }
}
