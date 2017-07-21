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
import co.jp.nej.earth.model.Layer;
import co.jp.nej.earth.model.constant.Constant.ErrorCode;
import co.jp.nej.earth.model.ws.GetLayerResponse;
import co.jp.nej.earth.model.ws.LayerRequest;
import co.jp.nej.earth.model.ws.Response;
import co.jp.nej.earth.model.ws.UpdateLayerRequest;
import co.jp.nej.earth.service.LayerService;

@RestController
@RequestMapping("/WS")
public class LayerRestController extends BaseRestController {
    @Autowired
    private LayerService layerService;

    @RequestMapping(value = "/getLayer")
    public Response getLayer(@Valid LayerRequest request, BindingResult bindingResult) throws EarthException {

        return (Response) getRestResponse(request, bindingResult, () -> {
            Layer layer = layerService.getLayerSession(EarthSessionManager.find(request.getSessionId()),
                    request.getWorkspaceId(), request.getWorkItemId(), request.getFolderItemNo(),
                    request.getDocumentNo(), request.getLayerNo());
            if (layer == null) {
                return new GetLayerResponse(messageSource.get(ErrorCode.E0029, new String[] { "folderItem" }));
            }

            return new GetLayerResponse(true, layer);
        });
    }

    @RequestMapping(value = "/updateLayer", method = RequestMethod.POST)
    public Response updateLayer(@Valid @RequestBody UpdateLayerRequest request, BindingResult bindingResult)
            throws EarthException {
        return getRestResponse(request, bindingResult, () -> {
            boolean result = layerService.updateLayerSession(EarthSessionManager.find(request.getSessionId()),
                    request.getWorkspaceId(), request.getWorkItemId(), request.getFolderItemNo(),
                    request.getDocumentNo(), request.getLayer());
            return new Response(result);
        });
    }
}
