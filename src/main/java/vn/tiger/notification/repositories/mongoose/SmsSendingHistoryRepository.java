package vn.tiger.notification.repositories.mongoose;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.tiger.notification.entities.mongoose.SmsSendingHistory;

public interface SmsSendingHistoryRepository extends MongoRepository<SmsSendingHistory, String> {
}
