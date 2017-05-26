package co.jp.nej.earth.web.restcontroller;

import co.jp.nej.earth.exception.*;
import co.jp.nej.earth.manager.session.*;
import co.jp.nej.earth.model.*;
import co.jp.nej.earth.model.enums.*;
import co.jp.nej.earth.model.form.*;
import co.jp.nej.earth.model.ws.*;
import co.jp.nej.earth.service.*;
import co.jp.nej.earth.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import javax.validation.*;
import java.util.*;

@RestController
@RequestMapping("/WS")
public class ProcessRestController extends BaseRestController {
    @Autowired
    private ProcessService processService;

    @Autowired
    private ValidatorUtil validatorUtil;

    @RequestMapping(value = "/openProcess", method = RequestMethod.GET)
    public RestResponse openProcess(OpenProcessForm form, BindingResult result,
            HttpServletRequest request) throws EarthException {
        RestResponse respone = new RestResponse();
        List<Message> messages = validatorUtil.validate(result);
        if (messages != null && messages.size() > 0) {
            respone.setResult(false);
            respone.setData(messages);
            return respone;
        }

        return getRestResponse(form.getSessionId(), null, () -> {
            try {
                respone.setResult(processService.openProcess(EarthSessionManager.find(form.getSessionId()),
                        form.getWorkspaceId(), null, form.getWorkItemId(),
                        OpenProcessMode.findOpenModeByMode(form.getMode())));
                return respone;
            } catch (EarthException e) {
                respone.setResult(false);
                respone.setData(e.getMessage());
                return respone;
            }
        });
    }

    @RequestMapping(value = "/closeProcess", method = RequestMethod.POST)
    public RestResponse closeProcess(@Valid CloseProcessForm form, BindingResult result,
            HttpServletRequest request) throws EarthException {
        RestResponse respone = new RestResponse();
        List<Message> messages = validatorUtil.validate(result);
        if (messages != null && messages.size() > 0) {
            respone.setResult(false);
            respone.setData(messages);
            return respone;
        }

        return getRestResponse(form.getSessionId(), null, () -> {
            try {
                respone.setResult(processService.closeProcess(EarthSessionManager.find(form.getSessionId()),
                        form.getWorkspaceId(), form.getWorkitemId(), form.getEventId()));
                return respone;
            } catch (EarthException e) {
                respone.setResult(false);
                respone.setData(e.getMessage());
                return respone;
            }
        });
    }

    @RequestMapping(value = "/getProcess", method = RequestMethod.POST)
    public RestResponse getProcess(@Valid GetProcessForm form, BindingResult result,
            HttpServletRequest request) throws EarthException {

        RestResponse respone = new RestResponse();
        List<Message> messages = validatorUtil.validate(result);
        if (messages != null && messages.size() > 0) {
            respone.setResult(false);
            respone.setData(messages);
            return respone;
        }

        return getRestResponse(form.getSessionId(), null, () -> {
            try {
                respone.setData(processService.getProcess(EarthSessionManager.find(form.getSessionId()),
                        form.getWorkspaceId(), form.getProcessId()));
                return respone;
            } catch (EarthException e) {
                respone.setResult(false);
                respone.setData(e.getMessage());
                return respone;
            }
        });
    }

    @RequestMapping(value = "/updateProcess", method = RequestMethod.POST)
    public RestResponse updateProcess(@Valid ProcessRestForm form, BindingResult result,
            HttpServletRequest request) throws EarthException {

        RestResponse respone = new RestResponse();
        List<Message> messages = validatorUtil.validate(result);
        if (messages != null && messages.size() > 0) {
            respone.setResult(false);
            respone.setData(messages);
            return respone;
        }

        return getRestResponse(form.getSessionId(), null, () -> {
            try {
                respone.setResult(processService.updateProcess(EarthSessionManager.find(form.getSessionId()),
                        form.getWorkspaceId(), form.getDatProcess()));
                return respone;
            } catch (EarthException e) {
                respone.setResult(false);
                respone.setData(e.getMessage());
                return respone;
            }
        });
    }

    @RequestMapping(value = "/closeAndSaveWorkItem", method = RequestMethod.POST)
    public RestResponse closeAndSaveWorkItem(WorkItemChangeForm form, BindingResult result,
            HttpServletRequest request) throws EarthException {

        RestResponse respone = new RestResponse();
        List<Message> messages = validatorUtil.validate(result);
        if (messages != null && messages.size() > 0) {
            respone.setResult(false);
            respone.setData(messages);
            return respone;
        }

        return getRestResponse(form.getSessionId(), null, () -> {
            try {
                respone.setResult(processService.closeAndSaveProcess(EarthSessionManager.find(form.getSessionId()),
                        form.getWorkItem(), form.getWorkspaceId()));
                return respone;
            } catch (EarthException e) {
                respone.setResult(false);
                respone.setData(e.getMessage());
                return respone;
            }
        });
    }
}