package co.jp.nej.earth.web.restcontroller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.form.EventControlForm;
import co.jp.nej.earth.model.ws.RestResponse;
import co.jp.nej.earth.service.EventControlService;
import co.jp.nej.earth.util.ValidatorUtil;

@RestController
@RequestMapping("/WS")
public class EventRestController extends BaseRestController {
    @Autowired
    private EventControlService eventControlService;
    @Autowired
    private ValidatorUtil validatorUtil;

    @RequestMapping(value = "/unlockEventControl", method = RequestMethod.POST)
    public RestResponse unlockEventControl(@Valid @RequestBody EventControlForm form, BindingResult result,
            HttpServletRequest request) throws EarthException {
        List<Message> messages = validatorUtil.validate(result);
        if (messages != null && messages.size() > 0) {
            RestResponse respone = new RestResponse();
            respone.setResult(false);
            respone.setData(messages);
            return respone;
        }

        return getRestResponse(form.getSessionId(), null, () -> {
            try {
                return eventControlService.unlockEventControl(form.getWorkspaceId(), form.getEventId());
            } catch (EarthException e) {
                RestResponse respone = new RestResponse();
                respone.setResult(false);
                respone.setData(e.getMessage());
                return respone;
            }
        });

    }
}
