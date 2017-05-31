package co.jp.nej.earth.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class EMessageResource {

    @Autowired
    private MessageSource messageSource;

    public String get(String errorCode) {
        return messageSource.getMessage(errorCode, new Object[]{}, Locale.JAPAN);
    }

    public String get(String errorCode, Object[] args) {
        return messageSource.getMessage(errorCode, args, Locale.JAPAN);
    }

}
