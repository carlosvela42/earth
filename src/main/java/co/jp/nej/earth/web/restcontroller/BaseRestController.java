package co.jp.nej.earth.web.restcontroller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.session.EarthSessionManager;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.constant.Constant.ErrorCode;
import co.jp.nej.earth.model.ws.Request;
import co.jp.nej.earth.model.ws.Response;
import co.jp.nej.earth.util.EMessageResource;
import co.jp.nej.earth.util.ValidatorUtil;

public abstract class BaseRestController {
    private static final Logger LOG = LoggerFactory.getLogger(BaseRestController.class);

    @Autowired
    protected EMessageResource messageSource;
    @Autowired
    private ValidatorUtil validatorUtil;

    @ControllerAdvice
    private static class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {
        JsonpAdvice() {
            super("callback");
        }
    }

    public interface WebServiceCaller {
        Response getResponse() throws EarthException;
    }

    protected Response getRestResponse(Request request, BindingResult bindingResult, WebServiceCaller caller) {
        Response response = new Response();
        try {
            LOG.info("Earth Spring Rest Webservice:");
            LOG.info("Request Content:" + new ObjectMapper().writeValueAsString(request));

            // Validation data input.
            List<Message> messages = validatorUtil.validate(bindingResult);
            if (messages != null && messages.size() > 0) {
                response.setMessages(messages);
            }

            // Connecting session.
            HttpSession session = EarthSessionManager.find(request.getSessionId());
            if (session == null) {
                response.setMessages(messageSource.get(ErrorCode.SESSION_INVALID, null));
            } else {
                response = caller.getResponse();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            response.setMessages(e.getCause() + ":" + e.getMessage());
        } finally {
            try {
                LOG.info("Response:" + new ObjectMapper().writeValueAsString(response));
            } catch (JsonProcessingException e) {
                LOG.info("JsonProcessingException:", e);
            }
        }
        return response;
    }
}