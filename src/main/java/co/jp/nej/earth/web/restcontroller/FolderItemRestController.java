package co.jp.nej.earth.web.restcontroller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.form.FolderItemForm;
import co.jp.nej.earth.model.form.FolderItemUpdateForm;
import co.jp.nej.earth.model.ws.RestResponse;
import co.jp.nej.earth.service.FolderItemService;
import co.jp.nej.earth.util.ValidatorUtil;

@RestController
@RequestMapping("/WS")
public class FolderItemRestController extends BaseRestController{
    @Autowired
    private FolderItemService folderItemService;

    @Autowired
    private ValidatorUtil validatorUtil;

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/getFolderItem", method = RequestMethod.POST)
    public @ResponseBody RestResponse getFolderItem(@Valid @RequestBody FolderItemForm form, BindingResult result,
            HttpServletRequest request) throws EarthException {
        List<Message> messages = validatorUtil.validate(result);
        if (messages.size() > 0) {
            RestResponse respone = new RestResponse();
            respone.setResult(false);
            respone.setData(messages);
            return respone;
        }

        return folderItemService.getFolderItem(request.getSession(), form.getWorkspaceId(), form.getWorkitemId(),
                form.getFolderItemNo());
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/updateFolderItem", method = RequestMethod.POST)
    public @ResponseBody RestResponse updateFolderItem(@Valid @RequestBody FolderItemUpdateForm form,
            BindingResult result, HttpServletRequest request) throws EarthException {
        List<Message> messages = validatorUtil.validate(result);
        if (messages.size() > 0) {
            RestResponse respone = new RestResponse();
            respone.setResult(false);
            respone.setData(messages);
            return respone;
        }

        return folderItemService.updateFolderItem(request.getSession(), form.getWorkspaceId(), form.getWorkitemId(),
                form.getFolderItem());
    }
}
