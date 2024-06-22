package vn.tiger.notification.repositories.mongoose;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.tiger.notification.entities.mongoose.NotificationUserReceive;

public interface NotificationUserReceiveRepository extends MongoRepository<NotificationUserReceive, String> {
}
