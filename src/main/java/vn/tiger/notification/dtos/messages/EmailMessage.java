package vn.tiger.notification.dtos.messages;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import vn.tiger.notification.dtos.request.NotificationInput;

@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EmailMessage extends NotificationInput {
    String subject;
    String content;
}
