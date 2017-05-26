package co.jp.nej.earth.web.restcontroller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.form.UpdateSystemDateForm;
import co.jp.nej.earth.model.ws.RestResponse;
import co.jp.nej.earth.service.SystemConfigurationService;
import co.jp.nej.earth.util.ValidatorUtil;

public class SystemRestController extends BaseRestController {
    @Autowired
    private SystemConfigurationService systemConfigurationService;
    @Autowired
    private ValidatorUtil validatorUtil;

    @RequestMapping(value = "/updateSystemDate", method = RequestMethod.POST)
    public RestResponse updateSystemDate(@Valid @RequestBody UpdateSystemDateForm form, BindingResult result,
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
                return systemConfigurationService.updateSystemConfig(form.getDateInput());
            } catch (EarthException e) {
                RestResponse respone = new RestResponse();
                respone.setResult(false);
                respone.setData(e.getMessage());
                return respone;
            }
        });

    }

}
