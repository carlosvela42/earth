package co.jp.nej.earth.web.restcontroller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.jp.nej.earth.model.ws.OpenProcessResponse;
import co.jp.nej.earth.service.WorkItemService;

@RestController
@RequestMapping("/WS")
public class ProcessRestController extends BaseRestController{
    @Autowired
    private WorkItemService workItemService;

    @Autowired
    private MessageSource messageResource;
    @CrossOrigin(origins = "*")
    @RequestMapping("/openProcess")
    public OpenProcessResponse openProcess(String jsessionId, String workspaceId, String processId, String workItemId) {
        // 1. Validate Input.
        String userTitle = messageResource.getMessage("signup.success", new String[] { "signup.success" },
                Locale.ENGLISH);
        // WorkItem workItem = workItemService.getWorkItemDataStructure(100);
        OpenProcessResponse openProcessResponse = new OpenProcessResponse();
        // openProcessResponse.setResult(workItem == null);
        return openProcessResponse;
    }

    // @RequestMapping("/CloseProcess")
    // public List<WorkItem> CloseProcess(
    // @RequestParam(value = "page", required = false, defaultValue = "0")
    // Integer page) {
    // List<WorkItem> workitems = workItemDao.getWorkItems(page);
    // return workitems;
    // }
}