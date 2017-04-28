package co.jp.nej.earth.util;

import javax.servlet.http.HttpSession;

import co.jp.nej.earth.model.constant.Constant.Session;
import co.jp.nej.earth.model.constant.Constant.View;

public class ViewUtil {

    public static String requiredLoginView(String view, HttpSession session) {
        if (!LoginUtil.isLogin(session)) {
            session.setAttribute(Session.LAST_PATH, view);
            return View.LOGIN;
        }

        if (session.getAttribute(Session.LAST_PATH) != null) {
            session.removeAttribute(Session.LAST_PATH);
        }

        return view;
    }
}