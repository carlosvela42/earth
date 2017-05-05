package co.jp.nej.earth.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.constant.Constant.Session;

@Component
public class ValidatorUtil {
    @Autowired
    private EMessageResource eMessageResource;

    public boolean validate(Model model, BindingResult result) {
        List<Message> messages = new ArrayList<Message>();

        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                String messageInput = fieldError.getDefaultMessage();
                String messageCode = EStringUtil.EMPTY;
                String[] params = null;
                if (!EStringUtil.isEmpty(messageInput)) {
                    String[] arrMessages = messageInput.split(",");
                    messageCode = arrMessages[0].trim();
                    if (arrMessages.length > 1) {
                        params = new String[arrMessages.length - 1];
                        for (int i = 1; i < arrMessages.length; i++) {
                            params[i - 1] = arrMessages[i].trim();
                        }
                    }
                }

                messages.add(new Message(fieldError.getCode(), eMessageResource.get(messageCode, params)));
            }

            model.addAttribute(Session.MESSAGES, messages);
            return false;
        }

        return true;
    }
}
