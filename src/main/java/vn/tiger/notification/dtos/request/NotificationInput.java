package vn.tiger.notification.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import vn.tiger.notification.constants.enums.NotificationPriority;
import vn.tiger.notification.constants.enums.NotificationType;
import vn.tiger.notification.constants.enums.ProcessStatus;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationInput {
    UUID id;
    NotificationType type;
    NotificationPriority priority;
    String receive;
    ProcessStatus processStatus;
    String errorMsg;
}
