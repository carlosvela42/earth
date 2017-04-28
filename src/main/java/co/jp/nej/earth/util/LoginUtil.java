package co.jp.nej.earth.util;

import java.util.Base64;
import java.util.Date;

import javax.servlet.http.HttpSession;

import co.jp.nej.earth.model.constant.Constant.Session;
import co.jp.nej.earth.model.entity.MgrUser;

/**
 *
 * @author p-tvo-thuynd
 *
 */
public class LoginUtil {
    public static boolean isLogin(HttpSession session) {
        if (session == null || session.getAttribute(Session.USER_INFO) == null) {
            return false;
        }

        return true;
    }

    /**
     * generate token based on userId and current date and time.
     *
     * @param userId id of current user.
     * @param date time when user logs in
     * @return token.
     */
    public static String generateToken(String userId, Date date) {
        return new String(Base64.getEncoder().encode((userId + "|" + date.getTime()).getBytes()));
    }

    /**
     * check whether user logs in or not.
     *
     * @param session HttpSession object.
     * @return boolean value.
     */
    public static boolean checkAuthen(HttpSession session) {
        return session.getAttribute(Session.USER_INFO) != null;
    }

    /**
     * check whether user exists in db or not.
     *
     * @param password.
     * @param mgrUser.
     * @return boolean value.
     */
    public static boolean isUserExisted(String password, MgrUser mgrUser){
        return mgrUser.getPassword().equals(password);
    }
}
