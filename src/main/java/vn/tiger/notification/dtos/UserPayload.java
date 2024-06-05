package vn.tiger.notification.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserPayload {

    UUID id;
    String slug;
    String username;
    String email;
    String phone;
    String avatar;
    Boolean mfa;
}
