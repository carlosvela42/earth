package co.jp.nej.earth.service;

import java.util.List;
import java.util.Map;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MgrProcess;
import co.jp.nej.earth.model.form.DeleteProcessForm;
import co.jp.nej.earth.model.form.ProcessForm;
import co.jp.nej.earth.model.ws.Response;

/**
 *
 * @author p-tvo-sonta
 *
 */
public interface ProcessService {
    String openProcess(String token, String workspaceId, String processId, String workItemId);

    String closeProcess(String token, String workspaceId, String processId);

    Process getProcess(String token, String workspaceId, String processId);

    String updateProcess(String token, String workspaceId, Process process);

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
