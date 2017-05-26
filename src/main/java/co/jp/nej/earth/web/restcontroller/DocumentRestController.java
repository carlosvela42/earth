package co.jp.nej.earth.web.restcontroller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.session.EarthSessionManager;
import co.jp.nej.earth.model.Document;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.form.CloseAndSaveForm;
import co.jp.nej.earth.model.form.CloseImageForm;
import co.jp.nej.earth.model.form.DocumentForm;
import co.jp.nej.earth.model.form.DocumentUpdateForm;
import co.jp.nej.earth.model.form.SaveImageForm;
import co.jp.nej.earth.model.ws.ImageResponse;
import co.jp.nej.earth.model.ws.RestResponse;
import co.jp.nej.earth.service.DocumentService;
import co.jp.nej.earth.util.ValidatorUtil;

@RestController
@RequestMapping("/WS")
public class DocumentRestController extends BaseRestController {
    @Autowired
    private DocumentService documentService;
    @Autowired
    private ValidatorUtil validatorUtil;

  @CrossOrigin(origins = "*")
    @RequestMapping("/displayImage")
    public ImageResponse displayImage(String jsessionId, String workspaceId, String workitemId, String folderItemNo,
            String documentNo, HttpServletRequest request) {
        ImageResponse imageResponse = new ImageResponse();
        boolean isSuccess = false;
        try {
            isSuccess = true;
            List<Document> doc = documentService.getDocumentListInfo(
                    workspaceId, workitemId, Integer.parseInt(folderItemNo), documentNo);
            imageResponse.setResult(doc);
        } catch (EarthException e) {
            isSuccess = false;
            imageResponse.setMessage(e.getMessage());
        }
        imageResponse.setResult(isSuccess);
        return imageResponse;
    }
   @RequestMapping(value = "/getDocument", method = RequestMethod.POST)
    public RestResponse getDocument(@Valid @RequestBody DocumentForm form, BindingResult result,
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
                return documentService.getDocument(EarthSessionManager.find(form.getSessionId()),
                        form.getWorkspaceId(), form.getWorkitemId(), form.getFolderItemNo(), form.getDocumentNo());
            } catch (EarthException e) {
                RestResponse respone = new RestResponse();
                respone.setResult(false);
                respone.setData(e.getMessage());
                return respone;
            }
        });

    }

    @RequestMapping(value = "/updateDocument", method = RequestMethod.POST)
    public RestResponse updateDocument(@Valid @RequestBody DocumentUpdateForm form, BindingResult result,
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
                return documentService.updateDocument(EarthSessionManager.find(form.getSessionId()),
                        form.getWorkspaceId(), form.getWorkitemId(), form.getFolderItemNo(), form.getDocument());
            } catch (EarthException e) {
                RestResponse respone = new RestResponse();
                respone.setResult(false);
                respone.setData(e.getMessage());
                return respone;
            }
        });
    }

    @RequestMapping(value = "/displayImage1")
    public RestResponse displayImage1(@ModelAttribute("DocumentForm") DocumentForm form, BindingResult result,
            HttpServletRequest request) throws EarthException {
        List<Message> messages = validatorUtil.validate(result);
        if (messages != null && messages.size() > 0) {
            RestResponse respone = new RestResponse();
            respone.setResult(false);
            respone.setData(messages);
            return respone;
        }

        return getRestResponse(form.getSessionId(), form, () -> {
                return documentService.displayImage(EarthSessionManager.find(form.getSessionId()),
                        form.getWorkspaceId(), form.getWorkitemId(), form.getFolderItemNo(), form.getDocumentNo());
        });
    }

    @RequestMapping(value = "/saveImage", method = RequestMethod.POST)
    public RestResponse saveImage(@Valid @RequestBody SaveImageForm form, BindingResult result,
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
                return documentService.saveImage(EarthSessionManager.find(form.getSessionId()), form.getWorkspaceId(),
                        form.getDocument());
            } catch (EarthException e) {
                RestResponse respone = new RestResponse();
                respone.setResult(false);
                respone.setData(e.getMessage());
                return respone;
            }
        });

    }

    @RequestMapping(value = "/closeImage", method = RequestMethod.POST)
    public RestResponse closeImage(@Valid @RequestBody CloseImageForm form, BindingResult result,
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
                return documentService.closeImage(EarthSessionManager.find(form.getSessionId()), form.getWorkspaceId(),
                        form.getWorkitemId());
            } catch (EarthException e) {
                RestResponse respone = new RestResponse();
                respone.setResult(false);
                respone.setData(e.getMessage());
                return respone;
            }
        });
    }

    @RequestMapping(value = "/saveAndCloseImages", method = RequestMethod.POST)
    public RestResponse saveAndCloseImages(@Valid @RequestBody CloseAndSaveForm form, BindingResult result,
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
                return documentService.saveAndCloseImages(EarthSessionManager.find(form.getSessionId()),
                        form.getWorkspaceId(), form.getWorkitemId(), form.getDocImages());
            } catch (EarthException e) {
                RestResponse respone = new RestResponse();
                respone.setResult(false);
                respone.setData(e.getMessage());
                return respone;
            }
        });
    }

    @RequestMapping(value = "/closeWithoutSavingImage", method = RequestMethod.POST)
    public RestResponse closeWithoutSavingImage(@Valid @RequestBody CloseImageForm form, BindingResult result,
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
                return documentService.closeWithoutSavingImage(EarthSessionManager.find(form.getSessionId()),
                        form.getWorkspaceId(), form.getWorkitemId());
            } catch (EarthException e) {
                RestResponse respone = new RestResponse();
                respone.setResult(false);
                respone.setData(e.getMessage());
                return respone;
            }
        });
    }

    @RequestMapping(value = "/getThumbnail", method = RequestMethod.POST)
    public RestResponse getThumbnail(@Valid @RequestBody DocumentForm form, BindingResult result,
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
                return documentService.getThumbnail(EarthSessionManager.find(form.getSessionId()),
                        form.getWorkspaceId(), form.getWorkitemId(), form.getFolderItemNo(), form.getDocumentNo());
            } catch (EarthException e) {
                RestResponse respone = new RestResponse();
                respone.setResult(false);
                respone.setData(e.getMessage());
                return respone;
            }
        });
    }
}