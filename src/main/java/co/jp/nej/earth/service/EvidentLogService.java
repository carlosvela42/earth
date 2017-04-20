package co.jp.nej.earth.service;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.StrLogAccess;

import java.util.List;

public interface EvidentLogService {

    List<StrLogAccess> getListByWorkspaceId(String workspaceId) throws EarthException;
}
