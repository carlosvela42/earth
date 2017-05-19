package co.jp.nej.earth.web.restcontroller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.constant.Constant.ErrorCode;
import co.jp.nej.earth.model.enums.OpenProcessMode;
import co.jp.nej.earth.model.form.ProcessRestForm;
import co.jp.nej.earth.model.form.WorkItemChangeForm;
import co.jp.nej.earth.model.ws.OpenProcessResponse;
import co.jp.nej.earth.service.ProcessService;

@RestController
@RequestMapping("/WS")
public class ProcessRestController extends BaseRestController {
    @Autowired
    private ProcessService processService;

    @Autowired
    private MessageSource messageResource;

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/openProcess", method = RequestMethod.POST)
    public OpenProcessResponse openProcess(@RequestAttribute("workspaceId") String workspaceId,
            @RequestAttribute("processId") String processId, @RequestAttribute("workItemId") String workItemId,
            @RequestAttribute("mode") Integer mode, HttpServletRequest request) throws EarthException {
        OpenProcessResponse openProcessResponse = new OpenProcessResponse();
        // 1. Validate Input.
        OpenProcessMode openProcessMode = OpenProcessMode.findOpenModeByMode(mode);
        if (workspaceId == null || workspaceId.isEmpty()) {
            openProcessResponse.setValue(
                    messageResource.getMessage(ErrorCode.E0002, new String[] { "workspaceId" }, Locale.ENGLISH));
            return openProcessResponse;
        }
        if (processId == null || processId.isEmpty()) {
            openProcessResponse.setValue(
                    messageResource.getMessage(ErrorCode.E0002, new String[] { "processId" }, Locale.ENGLISH));
            return openProcessResponse;
        }
        if (workItemId == null || workItemId.isEmpty()) {
            openProcessResponse.setValue(
                    messageResource.getMessage(ErrorCode.E0002, new String[] { "workItemId" }, Locale.ENGLISH));
            return openProcessResponse;
        }
        if (openProcessMode == null) {
            openProcessResponse
                    .setValue(messageResource.getMessage(ErrorCode.E0002, new String[] { "openMode" }, Locale.ENGLISH));
            return openProcessResponse;
        }
        openProcessResponse.setResult(
                processService.openProcess(request.getSession(), workspaceId, processId, workItemId, openProcessMode));
        return openProcessResponse;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/closeProcess", method = RequestMethod.POST)
    public OpenProcessResponse closeProcess(@RequestAttribute("workspaceId") String workspaceId,
            @RequestAttribute("eventId") String eventId, @RequestAttribute("workItemId") String workItemId,
            HttpServletRequest request) throws EarthException {
        OpenProcessResponse openProcessResponse = new OpenProcessResponse();
        // 1. Validate Input.
        if (workspaceId == null || workspaceId.isEmpty()) {
            openProcessResponse.setValue(
                    messageResource.getMessage(ErrorCode.E0002, new String[] { "workspaceId" }, Locale.ENGLISH));
            return openProcessResponse;
        }
        if (workItemId == null || workItemId.isEmpty()) {
            openProcessResponse.setValue(
                    messageResource.getMessage(ErrorCode.E0002, new String[] { "workItemId" }, Locale.ENGLISH));
            return openProcessResponse;
        }
        openProcessResponse
                .setResult(processService.closeProcess(request.getSession(), workspaceId, workItemId, eventId));
        return openProcessResponse;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/getProcess", method = RequestMethod.POST)
    public OpenProcessResponse getProcess(@RequestAttribute("workspaceId") String workspaceId,
            @RequestAttribute("processId") Integer processId, HttpServletRequest request) throws EarthException {
        OpenProcessResponse openProcessResponse = new OpenProcessResponse();
        if (workspaceId == null || workspaceId.isEmpty()) {
            openProcessResponse.setValue(
                    messageResource.getMessage(ErrorCode.E0002, new String[] { "workspaceId" }, Locale.ENGLISH));
            return openProcessResponse;
        }
        if (processId == null) {
            openProcessResponse.setValue(
                    messageResource.getMessage(ErrorCode.E0002, new String[] { "processId" }, Locale.ENGLISH));
            return openProcessResponse;
        }
        openProcessResponse.setValue(processService.getProcess(workspaceId, processId));
        openProcessResponse.setResult(true);
        return openProcessResponse;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/updateProcess", method = RequestMethod.POST)
    public OpenProcessResponse updateProcess(@RequestBody ProcessRestForm form,
            HttpServletRequest request) throws EarthException {
        OpenProcessResponse openProcessResponse = new OpenProcessResponse();
        if (form.getWorkspaceId() == null || form.getWorkspaceId().isEmpty()) {
            openProcessResponse.setValue(
                    messageResource.getMessage(ErrorCode.E0002, new String[] { "workspaceId" }, Locale.ENGLISH));
            return openProcessResponse;
        }
        if (form.getDatProcess().getProcessId() == null) {
            openProcessResponse.setValue(
                    messageResource.getMessage(ErrorCode.E0002, new String[] { "processId" }, Locale.ENGLISH));
            return openProcessResponse;
        }
        if (form.getDatProcess().getWorkItemId() == null || form.getDatProcess().getWorkItemId().isEmpty()) {
            openProcessResponse.setValue(
                    messageResource.getMessage(ErrorCode.E0002, new String[] { "workitemId" }, Locale.ENGLISH));
            return openProcessResponse;
        }
        openProcessResponse.setResult(
                processService.updateProcess(request.getSession(), form.getWorkspaceId(), form.getDatProcess()));
        return openProcessResponse;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/closeAndSaveWorkItem", method = RequestMethod.POST)
    public OpenProcessResponse closeAndSaveWorkItem(@RequestBody WorkItemChangeForm form,
            HttpServletRequest request) throws EarthException {
        OpenProcessResponse openProcessResponse = new OpenProcessResponse();
        if (form.getWorkspaceId() == null || form.getWorkspaceId().isEmpty()) {
            openProcessResponse.setValue(
                    messageResource.getMessage(ErrorCode.E0002, new String[] { "workspaceId" }, Locale.ENGLISH));
            return openProcessResponse;
        }
        if (form.getWorkItem().getWorkitemId() == null || form.getWorkItem().getWorkitemId().isEmpty()) {
            openProcessResponse.setValue(
                    messageResource.getMessage(ErrorCode.E0002, new String[] { "workItemId" }, Locale.ENGLISH));
            return openProcessResponse;
        }
        openProcessResponse.setResult(
                processService.closeAndSaveProcess(request.getSession(), form.getWorkItem(), form.getWorkspaceId()));
        return openProcessResponse;
    }
}