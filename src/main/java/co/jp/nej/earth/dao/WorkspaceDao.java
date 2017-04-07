package co.jp.nej.earth.dao;

import java.util.List;

import co.jp.nej.earth.dto.WorkspaceDto;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.MgrWorkspaceConnect;

public interface WorkspaceDao {
    public List<MgrWorkspaceConnect> getAllMgrConnections() throws EarthException;
    public MgrWorkspaceConnect getMgrConnectionByWorkspaceId(String workspaceId) throws EarthException;
    public List<MgrWorkspace> getAll() throws EarthException;
    public boolean insertOne(MgrWorkspaceConnect mgrWorkspaceConnect) throws EarthException;
    public boolean deleteList(List<String> workspaceIds) throws EarthException;
    public MgrWorkspaceConnect getOne(String workspaceId) throws EarthException;
    public boolean getById(String workspaceId) throws EarthException;
    public boolean UpdateOne(MgrWorkspaceConnect mgrWorkspaceConnect) throws EarthException;
}
