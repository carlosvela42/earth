package co.jp.nej.earth.web.restcontroller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.session.EarthSessionManager;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.constant.Constant.ErrorCode;
import co.jp.nej.earth.model.ws.GetDocumentResponse;
import co.jp.nej.earth.model.ws.GetLayerResponse;
import co.jp.nej.earth.model.ws.GetWorkItemRequest;
import co.jp.nej.earth.model.ws.GetWorkItemResponse;
import co.jp.nej.earth.model.ws.Response;
import co.jp.nej.earth.model.ws.SearchWorkItemRequest;
import co.jp.nej.earth.model.ws.WorkItemUpdateRequest;
import co.jp.nej.earth.service.WorkItemService;

@RestController
@RequestMapping("/WS")
public class WorkItemRestController extends BaseRestController {
    @Autowired
    private WorkItemService workItemService;

    @RequestMapping(value = "/getWorkItem")
    public Response getWorkItem(@Valid GetWorkItemRequest request, BindingResult result)
            throws EarthException {
        return (Response) getRestResponse(request, result, () -> {
            WorkItem workItem = workItemService.getWorkItemSession(EarthSessionManager.find(request.getSessionId()),
                    request.getWorkspaceId(), request.getWorkItemId());

            if (workItem == null) {
                return new GetLayerResponse(messageSource.get(ErrorCode.E0029, new String[] { "workItem" }));
            }

            return new GetWorkItemResponse(true, workItem);
        });

    }

    @RequestMapping(value = "/updateWorkItem", method = RequestMethod.POST)
    public Response updateWorkItem(@Valid @RequestBody WorkItemUpdateRequest request, BindingResult bindingResult)
            throws EarthException {
        return getRestResponse(request, bindingResult, () -> {
            boolean result = workItemService.updateWorkItemSession(EarthSessionManager.find(request.getSessionId()),
                    request.getWorkspaceId(), request.getWorkItem());
            return new Response(result);
        });
    }

    @RequestMapping(value = "/getWorkItemStructure")
    public Response getWorkItemStructure(@Valid GetWorkItemRequest request, BindingResult bindingResult)
            throws EarthException {
        return (Response) getRestResponse(request, bindingResult, () -> {
            WorkItem workItem = workItemService.getWorkItemStructureSession(
                    EarthSessionManager.find(request.getSessionId()), request.getWorkspaceId(),
                    request.getWorkItemId());
            if (workItem == null) {
                return new GetLayerResponse(messageSource.get(ErrorCode.E0029, new String[] { "workItem" }));
            }

            return new GetWorkItemResponse(true, workItem);
        });
    }

    @RequestMapping(value = "/searchWorkItems")
    public Response searchWorkItems(@Valid SearchWorkItemRequest request, BindingResult bindingResult)
            throws EarthException {
        return (Response) getRestResponse(request, bindingResult, () -> {
            WorkItem workItem = new WorkItem(request.getWorkItemId(), request.getTaskId(), request.getTemplateId(),
                    request.getLastHistoryNo(), request.getLastUpdateTime());

            List<WorkItem> workItems = workItemService.searchWorkItems(EarthSessionManager.find(request.getSessionId()),
                    request.getWorkspaceId(), workItem);
            if (workItems.size() == 0) {
                return new GetDocumentResponse(messageSource.get(ErrorCode.E0029, new String[] { "workItem" }));
            }

            return new GetWorkItemResponse(true, workItems);
        });
    }
}