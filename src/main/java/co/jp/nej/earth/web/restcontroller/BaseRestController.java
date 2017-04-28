package co.jp.nej.earth.web.restcontroller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

public abstract class BaseRestController {
    @ControllerAdvice
    private static class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {
        JsonpAdvice() {
            super("callback");
        }
    }
}