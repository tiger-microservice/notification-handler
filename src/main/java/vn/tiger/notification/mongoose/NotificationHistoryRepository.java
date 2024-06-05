package vn.tiger.notification.mongoose;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.tiger.notification.entities.mongoose.NotificationHistory;

public interface NotificationHistoryRepository extends MongoRepository<NotificationHistory, String> {
}
