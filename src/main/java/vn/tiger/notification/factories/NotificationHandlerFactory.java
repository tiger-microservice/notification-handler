package vn.tiger.notification.factories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vn.tiger.notification.constants.enums.NotifyBusinessType;
import vn.tiger.notification.exceptions.BusinessLogicException;
import vn.tiger.notification.exceptions.ErrorCode;
import vn.tiger.notification.services.handlers.NotifyBusinessService;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationHandlerFactory {

    final List<NotifyBusinessService> services;

    public NotifyBusinessService getService(NotifyBusinessType type) {
        return services.stream().filter(item -> item.isNotifyBusinessType(type))
                .findFirst()
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.BEAN_NOT_DEFINED));
    }
}
