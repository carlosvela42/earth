package co.jp.nej.earth.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.util.ObjectUtils;

import co.jp.nej.earth.model.TemplateAccessRight;
import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.constant.Constant.Session;
import co.jp.nej.earth.model.enums.AccessRight;

public class TemplateUtil {
    public static void saveToSession(HttpSession session, Map<TemplateKey, TemplateAccessRight> templateAccess) {
        session.setAttribute(Session.TEMPLATE_ACCESS_RIGHT_MAP, templateAccess);
    }

    @SuppressWarnings("unchecked")
    public static List<String> getAccessibleTemplates(HttpSession session, String workspaceId) {
        List<String> listTemplate = new ArrayList<String>();
        if (session.getAttribute(Session.TEMPLATE_ACCESS_RIGHT_MAP) != null) {
            Map<TemplateKey, TemplateAccessRight> templateAccessRightMap =
                    (Map<TemplateKey, TemplateAccessRight>) session
                    .getAttribute(Session.TEMPLATE_ACCESS_RIGHT_MAP);
            for (TemplateKey templateKey : templateAccessRightMap.keySet()) {
                if (templateKey.getWorkspaceId().equals(workspaceId)) {
                    listTemplate.add(templateKey.getTemplateId());
                }
            }
        }
        return listTemplate;
    }

    @SuppressWarnings("unchecked")
    public static AccessRight getAuthority(HttpSession session, TemplateKey tKey) {
        if (session.getAttribute(Session.TEMPLATE_ACCESS_RIGHT_MAP) != null) {
            Map<TemplateKey, TemplateAccessRight> templateAccessRightMap =
                    (Map<TemplateKey, TemplateAccessRight>) session
                    .getAttribute(Session.TEMPLATE_ACCESS_RIGHT_MAP);
            for (TemplateKey templateKey : templateAccessRightMap.keySet()) {
                if(templateKey.equals(tKey)){
                    TemplateAccessRight templateAccessRight = templateAccessRightMap.get(templateKey);
                    if (!ObjectUtils.isEmpty(templateAccessRight)) {
                        return templateAccessRight.getAccessRight();
                    }
                }
            }
        }
        return null;
    }
}