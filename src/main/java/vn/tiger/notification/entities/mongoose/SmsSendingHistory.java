package vn.tiger.notification.entities.mongoose;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;


@Data
@Builder
@Document(collection = "sms_sending_histories")
@CompoundIndex(name = "sms_sending_receive_type", def = "{'receive': 1,  'process_status': 1}")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SmsSendingHistory {
    @Id
    String id;
    @Field(value = "user_id")
    String userId;
    @Field(value = "receive")
    String receive;
    @Field(value = "status")
    Integer status = 0; //  0: unread, 1: read
    @Field(value = "process_status")
    String processStatus; // success, error
    @Field(value = "process_message")
    String processMessage; // error msg
    @Field(value = "retry_number")
    Integer retryNumber = 0; //  0: unread, 1: read
    @Field(value = "message_json")
    String messageJson;
    @Field(value = "created_date")
    LocalDateTime createdDate;
    @Field(value = "created_user")
    String createdUser;
    @Field(value = "updated_date")
    LocalDateTime updatedDate;
    @Field(value = "updated_user")
    String updatedUser;
}
