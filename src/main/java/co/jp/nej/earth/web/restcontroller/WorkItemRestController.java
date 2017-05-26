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
import co.jp.nej.earth.manager.session.EarthSessionManager;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.form.WorkItemForm;
import co.jp.nej.earth.model.form.WorkItemSearchForm;
import co.jp.nej.earth.model.form.WorkItemUpdateForm;
import co.jp.nej.earth.model.ws.RestResponse;
import co.jp.nej.earth.service.WorkItemService;
import co.jp.nej.earth.util.ValidatorUtil;

@RestController
@RequestMapping("/WS")
public class WorkItemRestController extends BaseRestController {
    @Autowired
    private WorkItemService workItemService;

    @Autowired
    private ValidatorUtil validatorUtil;

    @RequestMapping(value = "/getWorkitem", method = RequestMethod.POST)
    public RestResponse getWorkitem(@Valid @RequestBody WorkItemForm workItemForm, BindingResult result,
            HttpServletRequest request) throws EarthException {
        List<Message> messages = validatorUtil.validate(result);
        if (messages != null && messages.size() > 0) {
            RestResponse respone = new RestResponse();
            respone.setResult(false);
            respone.setData(messages);
            return respone;
        }
        return getRestResponse(workItemForm.getSessionId(), null, () -> {
            try {
                return workItemService.getWorkItem(EarthSessionManager.find(workItemForm.getSessionId()),
                        workItemForm.getWorkspaceId(), workItemForm.getWorkItemId());
            } catch (EarthException e) {
                RestResponse respone = new RestResponse();
                respone.setResult(false);
                respone.setData(e.getMessage());
                return respone;
            }
        });

    }

    @RequestMapping(value = "/updateWorkItem", method = RequestMethod.POST)
    public RestResponse updateWorkItem(@Valid @RequestBody WorkItemUpdateForm form, BindingResult result,
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
                return workItemService.updateWorkItem(EarthSessionManager.find(form.getSessionId()),
                        form.getWorkspaceId(), form.getWorkItem());
            } catch (EarthException e) {
                RestResponse respone = new RestResponse();
                respone.setResult(false);
                respone.setData(e.getMessage());
                return respone;
            }
        });
    }

    @RequestMapping(value = "/getWorkItemStructure", method = RequestMethod.POST)
    public RestResponse getWorkItemStructure(@Valid @RequestBody WorkItemForm workItemForm, BindingResult result,
            HttpServletRequest request) throws EarthException {
        List<Message> messages = validatorUtil.validate(result);
        if (messages != null && messages.size() > 0) {
            RestResponse respone = new RestResponse();
            respone.setResult(false);
            respone.setData(messages);
            return respone;
        }
        return getRestResponse(workItemForm.getSessionId(), null, () -> {
            try {
                return workItemService.getWorkItemStructure(EarthSessionManager.find(workItemForm.getSessionId()),
                        workItemForm.getWorkspaceId(), workItemForm.getWorkItemId());
            } catch (EarthException e) {
                RestResponse respone = new RestResponse();
                respone.setResult(false);
                respone.setData(e.getMessage());
                return respone;
            }
        });
    }

    @RequestMapping(value = "/searchWorkItems", method = RequestMethod.POST)
    public RestResponse searchWorkItems(@Valid @RequestBody WorkItemSearchForm form, BindingResult result,
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
                return workItemService.searchWorkItems(EarthSessionManager.find(form.getSessionId()),
                        form.getWorkspaceId(), form.getSearchCondition());
            } catch (EarthException e) {
                RestResponse respone = new RestResponse();
                respone.setResult(false);
                respone.setData(e.getMessage());
                return respone;
            }
        });
    }

}