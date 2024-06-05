package vn.tiger.notification.configs.locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class Translator {

    private static ResourceBundleMessageSource messageSource;

    @Autowired
    public Translator(ResourceBundleMessageSource messageSource) {
        Translator.messageSource = messageSource;
    }

    public String toMessage(String msgCode, String... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(msgCode, args, locale);
    }
}
