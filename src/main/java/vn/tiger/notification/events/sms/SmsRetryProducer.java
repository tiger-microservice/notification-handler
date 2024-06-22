package vn.tiger.notification.events.sms;

import com.tiger.common.utils.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import vn.tiger.notification.dtos.request.NotificationInput;
import vn.tiger.notification.exceptions.BusinessLogicException;
import vn.tiger.notification.exceptions.ErrorCode;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmsRetryProducer {

    final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.email-sending-retry:email-sending-retry}")
    public String topicNotifySendingRetry;

    public void sendNotifyRetry(NotificationInput msgSender) {
        try {
            var key = msgSender.getId() != null ? msgSender.getId().toString() : UUID.randomUUID().toString();
            var completableFuture = kafkaTemplate.send(topicNotifySendingRetry,
                    key,
                    ObjectMapperUtil.castToString(msgSender));
            completableFuture.whenComplete(((sendResult, throwable) -> {
                if (throwable != null) {
                    handleFailure(key, msgSender, throwable);
                } else {
                    handleSuccess(key, msgSender, sendResult);
                }
            }));
        } catch (Exception e) {
            log.error("[sendEmailMsg] error {}", e.getMessage(), e);
            throw new BusinessLogicException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    private void handleSuccess(String key, Object value, SendResult<String, String> sendResult) {
        log.info("Message sent successfully for the key: {} and the value: {}, partition is: {}",
                key, value, sendResult.getRecordMetadata().partition());
    }

    private void handleFailure(String key, Object value, Throwable throwable) {
        log.error("Error sending message and exception is {}", throwable.getMessage(), throwable);
    }

}
