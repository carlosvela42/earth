package co.jp.nej.earth.util;

import java.util.Base64;
import java.util.Date;

import javax.servlet.http.HttpSession;

import co.jp.nej.earth.model.constant.Constant.Session;
import co.jp.nej.earth.model.entity.MgrUser;

public class LoginUtil {
    public static boolean isLogin(String token) {
        if (token != null) {
            return true;
        }
        return false;
    }

    public static String generateToken(String userId, Date date) {
        return new String(Base64.getEncoder().encode((userId + "|" + date.getTime()).getBytes()));
    }

    public static boolean checkAuthen(HttpSession session) {
        return session.getAttribute(Session.USER_INFO) != null;
    }
    
    public static boolean isUserExisted(String password, MgrUser mgrUser){
        return mgrUser.getPassword().equals(password);
    }
}
