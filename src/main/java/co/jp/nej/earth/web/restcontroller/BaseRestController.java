package co.jp.nej.earth.web.restcontroller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.session.EarthSessionManager;
import co.jp.nej.earth.model.constant.Constant.ErrorCode;
import co.jp.nej.earth.model.ws.RestResponse;
import co.jp.nej.earth.util.EMessageResource;

public abstract class BaseRestController {
    private static final Logger LOG = LoggerFactory.getLogger(BaseRestController.class);

    @Autowired
    private EMessageResource messageSource;

    @ControllerAdvice
    private static class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {
        JsonpAdvice() {
            super("callback");
        }
    }

    public interface WebServiceCaller {
        RestResponse getResult() throws EarthException;
    }

    protected RestResponse getRestResponse(String sessionId, Object objInput, WebServiceCaller caller) {
        RestResponse response = new RestResponse();
        try {
            LOG.info("Earth Spring Rest Webservice:");
            LOG.info("Request:" + new ObjectMapper().writeValueAsString(objInput));

            HttpSession session = EarthSessionManager.find(sessionId);
            if (session == null) {
                response.setResult(false);
                response.setData(messageSource.get(ErrorCode.SESSION_INVALID, null));
            } else {
                response = caller.getResult();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            response.setResult(false);
            response.setData(e.getMessage());
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