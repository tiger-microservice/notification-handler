package vn.tiger.notification.services.email;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.tiger.notification.constants.enums.NotificationType;
import vn.tiger.notification.constants.enums.NotifyBusinessType;
import vn.tiger.notification.dtos.request.NotificationInput;
import vn.tiger.notification.factories.EmailHandlerFactory;
import vn.tiger.notification.services.NotificationAbstractService;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements NotificationAbstractService {

    final ObjectMapper objectMapper;
    final EmailHandlerFactory factory;

    @Override
    public boolean isNotificationType(NotificationType notificationType) {
        return NotificationType.EMAIL.equals(notificationType);
    }

    @Override
    public void businessNotification(NotificationInput input, NotifyBusinessType notifyBusinessType) {
        try {
            EmailBusinessService service = factory.getService(notifyBusinessType);
            service.businessLogic(input);
        } catch (Exception e) {
            log.error("[executeBusinessLogic] error {}", e.getMessage(), e);
        }
    }
}
