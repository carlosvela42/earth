package co.jp.nej.earth.service;

import co.jp.nej.earth.exception.*;
import co.jp.nej.earth.model.*;
import co.jp.nej.earth.model.enums.*;
import co.jp.nej.earth.model.form.*;
import co.jp.nej.earth.model.ws.*;

import javax.servlet.http.*;
import java.util.*;

/**
 * @author p-tvo-sonta
 */
public interface ProcessService {
  boolean openProcess(HttpSession session, String workspaceId, String processId, String workItemId,
                      OpenProcessMode openMode) throws EarthException;

  boolean closeProcess(HttpSession session, String workspaceId, String workItemId, String eventId)
      throws EarthException;

  DatProcess getProcess(HttpSession session, String workspaceId, Integer processId) throws EarthException;

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
    List<Message> validateProcess(ProcessForm form);

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
  Map<String, Object> getDetail(String workspaceId, String processId) throws EarthException;

  /**
   * update process
   *
   * @param form
   * @return
   * @throws EarthException
   */
  boolean updateOne(ProcessForm form) throws EarthException;
}
