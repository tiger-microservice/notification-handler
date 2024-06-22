package vn.tiger.notification.services.email;

import vn.tiger.notification.constants.enums.NotifyBusinessType;
import vn.tiger.notification.dtos.request.NotificationInput;

public interface EmailBusinessService {

    boolean isNotifyBusinessType(NotifyBusinessType notifyBusinessType);

    void businessLogic(NotificationInput notification);
}
