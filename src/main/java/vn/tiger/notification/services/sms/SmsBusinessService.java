package vn.tiger.notification.services.sms;

import vn.tiger.notification.constants.enums.NotifyBusinessType;
import vn.tiger.notification.dtos.request.NotificationInput;

public interface SmsBusinessService {

    boolean isNotifyBusinessType(NotifyBusinessType notifyBusinessType);

    void businessLogic(NotificationInput notification);
}
