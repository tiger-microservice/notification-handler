package vn.tiger.notification.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class TimeoutUtil {

    public static void setTimeout(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                log.error("[setTimeout] error {}", e.getMessage(), e);
            }
        }).start();
    }
}
