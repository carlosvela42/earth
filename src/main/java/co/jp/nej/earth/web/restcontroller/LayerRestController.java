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
import co.jp.nej.earth.model.form.LayerForm;
import co.jp.nej.earth.model.form.LayerUpdateForm;
import co.jp.nej.earth.model.ws.RestResponse;
import co.jp.nej.earth.service.LayerService;
import co.jp.nej.earth.util.ValidatorUtil;

@RestController
@RequestMapping("/WS")
public class LayerRestController extends BaseRestController {
    @Autowired
    private LayerService layerService;
    @Autowired
    private ValidatorUtil validatorUtil;

    @RequestMapping(value = "/getLayer", method = RequestMethod.POST)
    public RestResponse getLayer(@Valid @RequestBody LayerForm form, BindingResult result, HttpServletRequest request)
            throws EarthException {
        List<Message> messages = validatorUtil.validate(result);
        if (messages != null && messages.size() > 0) {
            RestResponse respone = new RestResponse();
            respone.setResult(false);
            respone.setData(messages);
            return respone;
        }

        return getRestResponse(form.getSessionId(), null, () -> {
            try {
                return layerService.getLayer(EarthSessionManager.find(form.getSessionId()), form.getWorkspaceId(),
                        form.getWorkitemId(), form.getFolderItemNo(), form.getDocumentNo(), form.getLayerNo());
            } catch (EarthException e) {
                RestResponse respone = new RestResponse();
                respone.setResult(false);
                respone.setData(e.getMessage());
                return respone;
            }
        });
    }

    @RequestMapping(value = "/updateLayer", method = RequestMethod.POST)
    public RestResponse updateLayer(@Valid @RequestBody LayerUpdateForm form, BindingResult result,
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
                return layerService.updateLayer(EarthSessionManager.find(form.getSessionId()), form.getWorkspaceId(),
                        form.getWorkitemId(), form.getFolderItemNo(), form.getDocumentNo(), form.getLayer());
            } catch (EarthException e) {
                RestResponse respone = new RestResponse();
                respone.setResult(false);
                respone.setData(e.getMessage());
                return respone;
            }
        });
    }
}
