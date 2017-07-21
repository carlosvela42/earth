package co.jp.nej.earth.web.restcontroller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.ws.Response;
import co.jp.nej.earth.model.ws.UnlockEventRequest;
import co.jp.nej.earth.service.EventControlService;

@RestController
@RequestMapping("/WS")
public class EventRestController extends BaseRestController {
    @Autowired
    private EventControlService eventControlService;

    @RequestMapping(value = "/unlockEventControl", method = RequestMethod.POST)
    public Response unlockEventControl(@Valid @RequestBody UnlockEventRequest request, BindingResult bindingResult)
            throws EarthException {
        return (Response)getRestResponse(request, bindingResult, () -> {
            boolean result = eventControlService.unlockEventControl(request.getWorkspaceId(), request.getEventId());
            return new Response(result);
        });
    }
}
