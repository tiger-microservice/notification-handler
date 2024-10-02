package vn.tiger.notification.services.sms;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.tiger.notification.constants.enums.NotificationType;
import vn.tiger.notification.constants.enums.NotifyBusinessType;
import vn.tiger.notification.dtos.request.NotificationInput;
import vn.tiger.notification.factories.SmsHandlerFactory;
import vn.tiger.notification.services.NotificationAbstractService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService implements NotificationAbstractService {

    final ObjectMapper objectMapper;
    final SmsHandlerFactory factory;

    @Override
    public boolean isNotificationType(NotificationType notificationType) {
        return NotificationType.SMS.equals(notificationType);
    }

    @Override
    public void businessNotification(NotificationInput input, NotifyBusinessType notifyBusinessType) {
        try {
            SmsBusinessService service = factory.getService(notifyBusinessType);
            service.businessLogic(input);
        } catch (Exception e) {
            log.error("[executeBusinessLogic] error {}", e.getMessage(), e);
        }
    }
}
