package vn.tiger.notification.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import vn.tiger.notification.dtos.request.NotificationInput;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMsgSender extends NotificationInput {

    String content;
}
