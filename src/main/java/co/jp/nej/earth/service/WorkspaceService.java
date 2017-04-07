package co.jp.nej.earth.service;

import java.util.List;

import co.jp.nej.earth.dto.WorkspaceDto;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.MgrWorkspaceConnect;

public interface WorkspaceService {
    public List<MgrWorkspaceConnect> getAllWorkspaceConnections() throws EarthException;
    public MgrWorkspaceConnect getMgrConnectionByWorkspaceId(String workspaceId) throws EarthException;
    public List<MgrWorkspace> getAll() throws EarthException;
    public boolean insertOne(MgrWorkspaceConnect mgrWorkspaceConnect) throws EarthException;
    public MgrWorkspaceConnect getDetail(String workspaceId) throws EarthException;
    public boolean deleteList(List<String> workspaceIds) throws EarthException;
    public List<Message> validateInsert(MgrWorkspaceConnect mgrWorkspaceConnect) throws EarthException;
    public List<Message> validateUpdate(MgrWorkspaceConnect mgrWorkspaceConnect) throws EarthException;
    public boolean updateOne(MgrWorkspaceConnect mgrWorkspaceConnect) throws EarthException;
}
