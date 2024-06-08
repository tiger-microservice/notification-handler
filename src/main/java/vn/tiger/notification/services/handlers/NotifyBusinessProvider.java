package vn.tiger.notification.services.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiger.common.utils.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.tiger.notification.constants.AppConstants;
import vn.tiger.notification.constants.enums.NotificationType;
import vn.tiger.notification.constants.enums.NotifyBusinessType;
import vn.tiger.notification.dtos.messages.EmailMessage;
import vn.tiger.notification.dtos.messages.NotificationMessage;
import vn.tiger.notification.dtos.messages.SmsMessage;
import vn.tiger.notification.dtos.request.NotificationInput;
import vn.tiger.notification.factories.NotificationHandlerFactory;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyBusinessProvider {

    final ObjectMapper objectMapper;
    final NotificationHandlerFactory factory;

    public void executeBusinessLogic(String message, NotifyBusinessType notifyBusinessType) {
        try {
            // cast to object
            String type = objectMapper.readTree(message).get(AppConstants.NOTIFY_TYPE_KEY).asText();
            var obj = getNotificationInput(type, message);

            NotifyBusinessService service = factory.getService(notifyBusinessType);
            service.businessLogic(obj);
        } catch (Exception e) {
            log.error("[executeBusinessLogic] error {}", e.getMessage(), e);
        }
    }

    private NotificationInput getNotificationInput(String type, String message) {
        NotificationType notificationType = NotificationType.valueOf(type);
        return switch (notificationType) {
            case NOTIFY -> ObjectMapperUtil.castToObject(message, NotificationMessage.class);
            case SMS -> ObjectMapperUtil.castToObject(message, SmsMessage.class);
            case EMAIL -> ObjectMapperUtil.castToObject(message, EmailMessage.class);
            default -> throw new IllegalArgumentException("Unknown message type: " + type);
        };
    }
}
