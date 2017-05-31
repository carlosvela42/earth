package co.jp.nej.earth.util;

import co.jp.nej.earth.exception.*;
import co.jp.nej.earth.model.*;
import co.jp.nej.earth.model.constant.*;
import co.jp.nej.earth.service.*;
import org.springframework.ui.*;

import javax.servlet.http.*;
import java.util.*;

public class SessionUtil {
    public static boolean connect(HttpServletRequest request, String token) {
//        HttpSession session = request.getSession();
//        List<String> tokenList = new ArrayList<>(session.getAttribute(Session.SESSION_TOKEN_KEY));
//        if(tokenList.contains(token)) {
//            return true;
//        }
        return false;
    }

    public static void loadWorkspaces(WorkspaceService service, Model model, HttpServletRequest request) throws
        EarthException {
        //Set workspaceId if request contains this parameter
        String workspaceId = (String) request.getParameter("workspaceId");
        if (workspaceId == null) {
            workspaceId = (String) request.getAttribute("workspaceId");
        }

        //reverse workspaceId if session contain
        if (workspaceId == null) {
            workspaceId = (String) SessionUtil.getSearchConditionWorkspaceId(request.getSession());
        }

        // get all work space
        List<MgrWorkspace> workspaces = service.getAll();
        // if workspace id from parameter is null
        if (workspaceId == null || workspaceId.isEmpty()) {
            workspaceId = workspaces.get(0).getWorkspaceId();
        }

        String workspaceName="";
        for(MgrWorkspace workspace : workspaces) {
            if(workspace.getWorkspaceId().equals(workspaceId)) {
                workspaceName = workspace.getWorkspaceName();
                break;
            }
        }

        model.addAttribute("workspaces", workspaces);
        model.addAttribute("workspaceId", workspaceId);
        model.addAttribute("workspaceName", workspaceName);
        setSearchConditionWorkspace(request.getSession(), workspaceId);
    }

    public static void setSearchConditionWorkspace(HttpSession session, String workspaceId) {
        Map<String, Object> searchCondition = getSearchCondition(session);
        searchCondition.put("workspaceId", workspaceId);
    }

    public static String getSearchConditionWorkspaceId(HttpSession session) {
        Map<String, Object> searchCondition = getSearchCondition(session);
        return (String) searchCondition.get("workspaceId");
    }

    public static String getSearchConditionWorkspace(HttpSession session) {
        Map<String, Object> searchCondition = getSearchCondition(session);
        return (String) searchCondition.get("workspaceId");
    }

    private static Map<String, Object> getSearchCondition(HttpSession session) {
        Map<String, Object> searchCondition =
            (Map<String, Object>) session.getAttribute(Constant.Session.LIST_SEARCH_CONDITION);
        if(searchCondition == null){
            searchCondition = new HashMap<>();
            session.setAttribute(Constant.Session.LIST_SEARCH_CONDITION, searchCondition);
        }
        return searchCondition;
    }
}
