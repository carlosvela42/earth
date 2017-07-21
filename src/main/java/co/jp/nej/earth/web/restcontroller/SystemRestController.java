package co.jp.nej.earth.web.restcontroller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.ws.Response;
import co.jp.nej.earth.model.ws.UpdateSystemDateRequest;
import co.jp.nej.earth.service.SystemConfigurationService;

public class SystemRestController extends BaseRestController {
    @Autowired
    private SystemConfigurationService systemConfigurationService;

    @RequestMapping(value = "/updateSystemDate", method = RequestMethod.POST)
    public Response updateSystemDate(@Valid @RequestBody UpdateSystemDateRequest request, BindingResult bindingResult)
            throws EarthException {
        return getRestResponse(request, bindingResult, () -> {
            boolean result = systemConfigurationService.updateSystemConfig(request.getDateInput());
            return new Response(result);
        });
    }

}
