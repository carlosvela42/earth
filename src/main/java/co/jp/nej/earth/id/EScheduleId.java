package co.jp.nej.earth.id;

import co.jp.nej.earth.exception.EarthException;

public interface EScheduleId {
    String getAutoId(String workspaceId) throws EarthException;
}
