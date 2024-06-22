package vn.tiger.notification.services;

import vn.tiger.notification.constants.enums.NotificationType;
import vn.tiger.notification.constants.enums.NotifyBusinessType;
import vn.tiger.notification.dtos.request.NotificationInput;

public interface NotificationAbstractService {

    boolean isNotificationType(NotificationType notificationType);

    void businessNotification(NotificationInput input, NotifyBusinessType notifyBusinessType);

}
