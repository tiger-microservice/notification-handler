package vn.tiger.notification.services.handlers;

import com.tiger.common.utils.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.tiger.notification.constants.enums.NotifyBusinessType;
import vn.tiger.notification.dtos.request.NotificationInput;
import vn.tiger.notification.factories.NotificationHandlerFactory;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyBusinessProvider {

    final NotificationHandlerFactory factory;

    public void executeBusinessLogic(String message, NotifyBusinessType notifyBusinessType) {
        try {
            // cast to object
            var obj = ObjectMapperUtil.castToObject(message, NotificationInput.class);

            NotifyBusinessService service = factory.getService(notifyBusinessType);
            service.businessLogic(obj);
        } catch (Exception e) {
            log.error("[executeBusinessLogic] error {}", e.getMessage(), e);
        }
    }
}
