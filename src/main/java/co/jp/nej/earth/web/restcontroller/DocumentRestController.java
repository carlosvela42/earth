package co.jp.nej.earth.web.restcontroller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Document;
import co.jp.nej.earth.model.ws.ImageResponse;
import co.jp.nej.earth.service.DocumentService;

@RestController
@RequestMapping("/WS")
public class DocumentRestController extends BaseRestController {
    @Autowired
    private DocumentService documentService;

    @CrossOrigin(origins = "*")
    @RequestMapping("/displayImage")
    public ImageResponse displayImage(String jsessionId, String workspaceId, String workitemId, String folderItemNo,
            String documentNo, HttpServletRequest request) {
        ImageResponse imageResponse = new ImageResponse();
        boolean isSuccess = false;
        try {
            isSuccess = true;
            List<Document> doc = documentService.getDocumentListInfo(workspaceId, "1", 2, "3");
            imageResponse.setResult(doc);
        } catch (EarthException e) {
            isSuccess = false;
            imageResponse.setMessage(e.getMessage());
        }
        imageResponse.setResult(isSuccess);
        return imageResponse;
    }
}
