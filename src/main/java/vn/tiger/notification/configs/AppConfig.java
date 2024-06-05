package vn.tiger.notification.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiger.common.utils.ObjectMapperUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Slf4j
@Configuration
@EnableCaching
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return ObjectMapperUtil.objectMapper();
    }

}
