package co.jp.nej.earth.id;

import co.jp.nej.earth.exception.EarthException;

public interface EProcessId {
    int getAutoId(String workspaceId) throws EarthException;
}
