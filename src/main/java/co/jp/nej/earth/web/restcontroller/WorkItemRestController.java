package co.jp.nej.earth.web.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.ws.GetWorkitemResponse;
import co.jp.nej.earth.service.WorkItemService;

@RestController
@RequestMapping("/WS")
public class WorkItemRestController {
    @Autowired
    private WorkItemService workItemService;

    private static final int NUM_100 = 100;

    @RequestMapping("/GetWorkitem")
    public GetWorkitemResponse getWorkitem(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page) {
        WorkItem workitem = workItemService.getWorkItemDataStructure(NUM_100);

        GetWorkitemResponse workitemResponse = new GetWorkitemResponse();
        workitemResponse.setWorkitem(workitem);
        return workitemResponse;
    }

    // @RequestMapping("/OpenWorkitem")
    // public List<WorkItem> OpenWorkitem(
    // @RequestParam(value = "page", required = false, defaultValue = "0")
    // Integer page) {
    // List<WorkItem> workitems = workItemDao.getWorkItems(page);
    // return workitems;
    // }
    //
    //
    // @RequestMapping("/CloseWorkitem")
    // public List<WorkItem> CloseWorkitem(
    // @RequestParam(value = "page", required = false, defaultValue = "0")
    // Integer page) {
    // List<WorkItem> workitems = workItemDao.getWorkItems(page);
    // return workitems;
    // }
}