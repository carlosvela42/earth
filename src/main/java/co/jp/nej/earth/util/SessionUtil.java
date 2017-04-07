package co.jp.nej.earth.util;

import javax.servlet.http.HttpServletRequest;

public class SessionUtil {
    public static boolean connect(HttpServletRequest request, String token) {
//        HttpSession session = request.getSession();
//        List<String> tokenList = new ArrayList<>(session.getAttribute(Session.SESSION_TOKEN_KEY));
//        if(tokenList.contains(token)) {
//            return true;
//        }
        return false;
    }
}
