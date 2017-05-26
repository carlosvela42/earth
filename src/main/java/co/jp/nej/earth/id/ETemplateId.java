package co.jp.nej.earth.id;

import co.jp.nej.earth.exception.EarthException;

public interface ETemplateId {
    String getAutoId(String workspaceId) throws EarthException;
}
