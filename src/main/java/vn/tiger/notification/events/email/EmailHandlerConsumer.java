package vn.tiger.notification.events.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import vn.tiger.notification.constants.enums.NotifyBusinessType;
import vn.tiger.notification.services.NotificationProvider;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailHandlerConsumer {

    final NotificationProvider notificationProvider;

    @KafkaListener(
            topics = {"${spring.kafka.notify-sending-result:notify-sending-result}"},
            groupId = "notification-handler"
    )
    public void handlerEmailSendingResult(@Payload String message,
                                          @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                          @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.info("[handlerEmailSendingResult] message {}", message);
        log.info("[handlerEmailSendingResult] topic {}", topic);
        log.info("[handlerEmailSendingResult] key {}", key);
        try {
            this.notificationProvider.executeBusinessLogic(message, NotifyBusinessType.A);
        } catch (Exception e) {
            log.error("[handlerNotifySendingResult] error {}", e.getMessage(), e);
        }
    }

    @KafkaListener(
            topics = {
                    "${spring.kafka.notify-destination-not-in-whitelist:notify-destination-not-in-whitelist}"
            },
            groupId = "notification-handler"
    )
    public void handlerEmailNotSending(@Payload String message,
                                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                       @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.info("[handlerEmailNotSending] message {}", message);
        log.info("[handlerEmailNotSending] topic {}", topic);
        log.info("[handlerEmailNotSending] key {}", key);
        try {
            this.notificationProvider.executeBusinessLogic(message, NotifyBusinessType.B);
        } catch (Exception e) {
            log.error("[handlerNotifySendingResult] error {}", e.getMessage(), e);
        }
    }

}
