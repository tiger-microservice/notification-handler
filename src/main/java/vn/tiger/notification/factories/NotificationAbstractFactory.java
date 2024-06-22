package vn.tiger.notification.factories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.tiger.notification.constants.enums.NotificationType;
import vn.tiger.notification.exceptions.BusinessLogicException;
import vn.tiger.notification.exceptions.ErrorCode;
import vn.tiger.notification.services.NotificationAbstractService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationAbstractFactory {
    final List<NotificationAbstractService> services;

    public NotificationAbstractService getService(NotificationType type) {
        return services.stream().filter(item -> item.isNotificationType(type))
                .findFirst()
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.BEAN_NOT_DEFINED));
    }

}
