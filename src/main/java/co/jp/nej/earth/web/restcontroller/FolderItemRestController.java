package co.jp.nej.earth.web.restcontroller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.session.EarthSessionManager;
import co.jp.nej.earth.model.FolderItem;
import co.jp.nej.earth.model.constant.Constant.ErrorCode;
import co.jp.nej.earth.model.ws.GetFolderItemRequest;
import co.jp.nej.earth.model.ws.GetFolderItemResponse;
import co.jp.nej.earth.model.ws.Response;
import co.jp.nej.earth.model.ws.UpdateFolderItemRequest;
import co.jp.nej.earth.service.FolderItemService;

@RestController
@RequestMapping("/WS")
public class FolderItemRestController extends BaseRestController {
    @Autowired
    private FolderItemService folderItemService;

    @RequestMapping(value = "/getFolderItem")
    public Response getFolderItem(@Valid GetFolderItemRequest request, BindingResult bindingResult)
            throws EarthException {

        return (Response) getRestResponse(request, bindingResult, () -> {
            FolderItem folderItem = folderItemService.getFolderItemSession(
                    EarthSessionManager.find(request.getSessionId()), request.getWorkspaceId(), request.getWorkItemId(),
                    request.getFolderItemNo());
            if (folderItem == null) {
                return new GetFolderItemResponse(messageSource.get(ErrorCode.E0029, new String[] { "folderItem" }));
            }

            return new GetFolderItemResponse(true, folderItem);
        });
    }

    @RequestMapping(value = "/updateFolderItem", method = RequestMethod.POST)
    public Response updateFolderItem(@Valid @RequestBody UpdateFolderItemRequest request, BindingResult bindingResult)
            throws EarthException {
        return getRestResponse(request, bindingResult, () -> {
            boolean result = folderItemService.updateFolderItemSession(EarthSessionManager.find(request.getSessionId()),
                                            request.getWorkspaceId(), request.getWorkItemId(), request.getFolderItem());
            return new Response(result);
        });
    }
}
