package co.jp.nej.earth.manager.session;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class EarthSessionManager implements HttpSessionListener {
    private static final Map<String, HttpSession> earthSessions = new HashMap<String, HttpSession>();

    public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        earthSessions.put(session.getId(), session);
    }

    public void sessionDestroyed(HttpSessionEvent event) {
        earthSessions.remove(event.getSession().getId());
    }

    public static HttpSession find(String sessionId) {
        return earthSessions.get(sessionId);
    }

    public static void save(HttpSession session) {
        earthSessions.put(session.getId(), session);
    }
}
