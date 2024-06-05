package vn.tiger.notification.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationStatus {
    UNREAD(0), READ(1), NONE(-1);
    private Integer status;
}
