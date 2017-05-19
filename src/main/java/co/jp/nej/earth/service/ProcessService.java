package co.jp.nej.earth.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.DatProcess;
import co.jp.nej.earth.model.MgrProcess;
import co.jp.nej.earth.model.ProcessMap;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.enums.OpenProcessMode;
import co.jp.nej.earth.model.form.DeleteProcessForm;
import co.jp.nej.earth.model.form.ProcessForm;
import co.jp.nej.earth.model.ws.Response;

/**
 *
 * @author p-tvo-sonta
 *
 */
public interface ProcessService {
    boolean openProcess(HttpSession session, String workspaceId, String processId, String workItemId,
            OpenProcessMode openMode) throws EarthException;

    boolean closeProcess(HttpSession session, String workspaceId, String workItemId, String eventId)
            throws EarthException;

    DatProcess getProcess(String workspaceId, Integer processId) throws EarthException;

    boolean updateProcess(HttpSession session, String workspaceId, ProcessMap datProcess) throws EarthException;

    boolean closeAndSaveProcess(HttpSession session, WorkItem workItem, String workspacedId) throws EarthException;

    /**
     * get all process by workspace
     *
     * @param workspaceId
     * @return
     * @throws EarthException
     */
    List<MgrProcess> getAllByWorkspace(String workspaceId) throws EarthException;

    /**
     * check validation action delete process
     *
     * @param form
     * @return
     */
    Response validateDeleteAction(DeleteProcessForm form);

    /**
     * delete list process
     *
     * @param form
     * @return
     * @throws EarthException
     */
    boolean deleteList(DeleteProcessForm form) throws EarthException;

    /**
     * check validation action insert or update process
     *
     * @param form
     * @return
     */
    Response validateProcess(ProcessForm form);

    /**
     * insert process
     *
     * @param form
     * @return
     * @throws EarthException
     */
    boolean insertOne(ProcessForm form) throws EarthException;

    /**
     * get process detail
     *
     * @param workspaceId
     * @param processId
     * @return
     * @throws EarthException
     */
    Map<String, Object> getDetail(String workspaceId, int processId) throws EarthException;

    /**
     * update process
     *
     * @param form
     * @return
     * @throws EarthException
     */
    boolean updateOne(ProcessForm form) throws EarthException;
}
