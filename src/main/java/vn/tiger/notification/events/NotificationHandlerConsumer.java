package vn.tiger.notification.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import vn.tiger.notification.constants.enums.NotifyBusinessType;
import vn.tiger.notification.services.handlers.NotifyBusinessProvider;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationHandlerConsumer {

    final NotifyBusinessProvider notifyBusinessProvider;

    @KafkaListener(
            topics = {"${spring.kafka.notify-sending-result:notify-sending-result}"},
            groupId = "notification-handler"
    )
    public void handlerNotifySendingResult(@Payload String message,
                                 @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                 @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.info("[handlerNotifySendingResult] message {}", message);
        log.info("[handlerNotifySendingResult] topic {}", topic);
        log.info("[handlerNotifySendingResult] key {}", key);
        try {
            this.notifyBusinessProvider.executeBusinessLogic(message, NotifyBusinessType.C);
        } catch (Exception e) {
            log.error("[handlerNotifySendingResult] error {}", e.getMessage(), e);
        }
    }

    @KafkaListener(
            topics = {"${spring.kafka.notify-destination-not-in-whitelist:notify-destination-not-in-whitelist}"},
            groupId = "notification-handler"
    )
    public void handlerNotifyNotInWhitelist(@Payload String message,
                                           @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                           @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.info("[handlerNotifyNotInWhitelist] message {}", message);
        log.info("[handlerNotifyNotInWhitelist] topic {}", topic);
        log.info("[handlerNotifyNotInWhitelist] key {}", key);
        try {
            this.notifyBusinessProvider.executeBusinessLogic(message, NotifyBusinessType.A);
        } catch (Exception e) {
            log.error("[handlerNotifySendingResult] error {}", e.getMessage(), e);
        }
    }


    @KafkaListener(
            topics = {"${spring.kafka.notify-not-sending:notify-not-sending}"},
            groupId = "notification-handler"
    )
    public void handlerNotifyNotSending(@Payload String message,
                                            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                            @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.info("[handlerNotifyNotSending] message {}", message);
        log.info("[handlerNotifyNotSending] topic {}", topic);
        log.info("[handlerNotifyNotSending] key {}", key);
        try {
            this.notifyBusinessProvider.executeBusinessLogic(message, NotifyBusinessType.B);
        } catch (Exception e) {
            log.error("[handlerNotifySendingResult] error {}", e.getMessage(), e);
        }
    }

}
