package co.jp.nej.earth.util;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class EMessageResource {

    @Autowired
    private MessageSource messageSource;

    public String get(String errorCode, Object[] args) {
        return messageSource.getMessage(errorCode, args, Locale.JAPAN);
    }
}
