package vn.tiger.notification.repositories.mongoose;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.tiger.notification.entities.mongoose.NotificationSendingHistory;

public interface NotificationHistoryRepository extends MongoRepository<NotificationSendingHistory, String> {
}
