package vn.tiger.notification.repositories.mongoose;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.tiger.notification.entities.mongoose.EmailSendingHistory;

public interface EmailSendingHistoryRepository extends MongoRepository<EmailSendingHistory, String> {
}
