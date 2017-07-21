package co.jp.nej.earth.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import co.jp.nej.earth.model.TemplateAccessRight;
import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.constant.Constant.Session;
import co.jp.nej.earth.model.enums.AccessRight;

public class TemplateUtil {
    public static void saveToSession(HttpSession session, TemplateAccessRight templateAccess) {
        session.setAttribute(Session.TEMPLATE_ACCESS_RIGHT_MAP, templateAccess);
    }

    public static List<String> getAccessibleTemplates(HttpSession session, String workspaceId) {
        List<String> listTemplate = new ArrayList<String>();

        TemplateAccessRight templateAccessRight = getAuthorityFromSession(session);
        if (templateAccessRight != null) {
            for (TemplateKey templateKey : templateAccessRight.getTemplatesAccessRights().keySet()) {
                if (templateKey.getWorkspaceId().equals(workspaceId)) {
                    listTemplate.add(templateKey.getTemplateId());
                }
            }
        }
        return listTemplate;
    }

    public static AccessRight getAuthority(HttpSession session, TemplateKey tKey) {
        TemplateAccessRight templateAccessRight = getAuthorityFromSession(session);
        if (templateAccessRight != null) {
            return templateAccessRight.get(tKey);
        }
        return AccessRight.NONE;
    }

    public static TemplateAccessRight getAuthorityFromSession(HttpSession session) {
        return (TemplateAccessRight) session.getAttribute(Session.TEMPLATE_ACCESS_RIGHT_MAP);
    }
}