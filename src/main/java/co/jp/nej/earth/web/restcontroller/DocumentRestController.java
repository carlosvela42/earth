package co.jp.nej.earth.web.restcontroller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.ws.ImageResponse;

import co.jp.nej.earth.service.TemplateService;

@RestController
@RequestMapping("/WS")
public class DocumentRestController extends BaseRestController {
    @Autowired
    private TemplateService templateService;

    @CrossOrigin(origins = "*")
    @RequestMapping("/displayImage")
    public ImageResponse displayImage(String jsessionId, String workspaceId, String workitemId, String folderItemNo,
            String documentNo, HttpServletRequest request) {
        ImageResponse imageResponse = new ImageResponse();
        boolean messages;
        boolean isSuccess = false;
        try {
            templateService.getTemplateListInfo(workspaceId);
        } catch (EarthException e) {
            isSuccess = false;
            imageResponse.setMessage(e.getMessage());
        }
        imageResponse.setResult(isSuccess);
        return imageResponse;
    }
}
