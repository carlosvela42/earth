package co.jp.nej.earth.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.Column;
import co.jp.nej.earth.model.Row;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.WorkItemListDTO;
import co.jp.nej.earth.model.enums.ColumnNames;
import co.jp.nej.earth.model.sql.QCtlEvent;
import co.jp.nej.earth.model.sql.QDatProcess;
import co.jp.nej.earth.model.sql.QMgrSchedule;
import co.jp.nej.earth.model.sql.QMgrTemplate;
import co.jp.nej.earth.model.sql.QWorkItem;
import co.jp.nej.earth.web.form.SearchForm;

/**
 * @author p-tvo-khanhnv
 *
 */
@Repository
public class WorkItemDaoImpl extends BaseDaoImpl<WorkItem> implements WorkItemDao {

  public WorkItemDaoImpl() throws Exception {
    super();
  }

  public WorkItem getWorkItemDataStructure(long workItemId) {
    // TODO Auto-generated method stub
    return null;
  }

  public WorkItem getWorkItemStructure(long workItemId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<WorkItemListDTO> getWorkItemsByWorkspace(String workspaceId) throws EarthException {
    QWorkItem qWorkItem = QWorkItem.newInstance();
    QMgrTemplate qMgrTemplate = QMgrTemplate.newInstance();
    QMgrSchedule qMgrSchedule = QMgrSchedule.newInstance();
    QCtlEvent qCtlEvent = QCtlEvent.newInstance();
    List<WorkItemListDTO> workItems = new ArrayList<>();
    try {
      EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(workspaceId);
      ResultSet resultSet = earthQueryFactory
          .select(qWorkItem.workitemId, qCtlEvent.status, qMgrSchedule.taskId, qMgrTemplate.templateId,
              qMgrTemplate.templateName)
          .from(qWorkItem).innerJoin(qMgrTemplate).on(qWorkItem.templateId.eq(qMgrTemplate.templateId))
          .innerJoin(qMgrSchedule).on(qWorkItem.taskId.eq(qMgrSchedule.taskId)).innerJoin(qCtlEvent)
          .on(qWorkItem.workitemId.eq(qCtlEvent.workitemId)).getResults();
      while (resultSet.next()) {
        WorkItemListDTO workItem = new WorkItemListDTO();
        workItem.setStatusLock(resultSet.getString(ColumnNames.STATUS.toString()));
        workItem.setWorkitemId(resultSet.getString(ColumnNames.WORKITEM_ID.toString()));
        workItem.setTaskName(resultSet.getString(ColumnNames.TASK_ID.toString()));
        workItem.setTemplateId(resultSet.getString(ColumnNames.TEMPLATE_ID.toString()));
        workItem.setTemplateName(resultSet.getString(ColumnNames.TEMPLATE_NAME.toString()));
        workItems.add(workItem);
      }
      return workItems;
    } catch (Exception ex) {
      throw new EarthException(ex);
    }
  }

  @Override
  public long unlock(List<String> workItemId, String workspaceId) throws EarthException {
    try {
      QCtlEvent qCtlEvent = QCtlEvent.newInstance();
      return ConnectionManager.getEarthQueryFactory(workspaceId).update(qCtlEvent).set(qCtlEvent.status, "0")
          .where(qCtlEvent.workitemId.in(workItemId)).execute();
    } catch (Exception ex) {
      throw new EarthException(ex.getMessage());
    }
  }

  @Override
  public List<Row> getRowValue(SearchForm searchForm, List<Column> columns, String workspaceId) throws EarthException {
    List<Row> rowList = new ArrayList<Row>();
    try {
      String strQuery = "SELECT * FROM " + searchForm.getTemplateTableName();
      EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(workspaceId);
      Statement stmt = earthQueryFactory.getConnection().createStatement();
      ResultSet rs = stmt.executeQuery(strQuery);
      while (rs.next()) {
        List<Column> listCol = new ArrayList<>();
        Row row = new Row();
        Column col;
        for (int i = 0; i < columns.size(); i++) {
          col = new Column();
          col.setName(columns.get(i).getName());
          col.setValue(rs.getString(columns.get(i).getName()));
          listCol.add(col);
        }
        row.setWorkitemId(rs.getString("WORKITEMID"));
        row.setColumns(listCol);
        rowList.add(row);
      }
    } catch (Exception ex) {
      throw new EarthException(ex.getMessage());
    }
    return rowList;
  }

  @Override
  public Integer getProcessIdByWorkItem(String workspaceId, String workitemId) throws EarthException {
    try {
      QDatProcess qDatProcess = QDatProcess.newInstance();
      return ConnectionManager.getEarthQueryFactory(workspaceId).select(qDatProcess.processId)
          .where(qDatProcess.workitemId.eq(workitemId)).fetchOne();
    } catch (Exception ex) {
      throw new EarthException(ex.getMessage());
    }
  }
}
