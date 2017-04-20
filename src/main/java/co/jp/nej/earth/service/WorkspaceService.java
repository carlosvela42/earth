package co.jp.nej.earth.service;

import java.util.List;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.MgrWorkspaceConnect;

public interface WorkspaceService {
    List<MgrWorkspaceConnect> getAllWorkspaceConnections() throws EarthException;

    MgrWorkspaceConnect getMgrConnectionByWorkspaceId(String workspaceId) throws EarthException;

    List<MgrWorkspace> getAll() throws EarthException;

    boolean insertOne(MgrWorkspaceConnect mgrWorkspaceConnect) throws EarthException;

    MgrWorkspaceConnect getDetail(String workspaceId) throws EarthException;

    List<Message> deleteList(List<String> workspaceIds) throws EarthException;

    List<Message> validateInsert(MgrWorkspaceConnect mgrWorkspaceConnect) throws EarthException;

    List<Message> validateUpdate(MgrWorkspaceConnect mgrWorkspaceConnect) throws EarthException;

    boolean updateOne(MgrWorkspaceConnect mgrWorkspaceConnect) throws EarthException;
}
