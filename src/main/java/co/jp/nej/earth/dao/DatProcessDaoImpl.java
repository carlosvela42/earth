package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.model.DatProcess;
import co.jp.nej.earth.model.sql.QDatProcess;
import co.jp.nej.earth.util.DateUtil;
import co.jp.nej.earth.util.EStringUtil;
import org.springframework.stereotype.Repository;

@Repository
public class DatProcessDaoImpl extends BaseDaoImpl<DatProcess> implements DatProcessDao {

    public DatProcessDaoImpl() throws Exception {
        super();
    }

    @Override
    public long updateProcess(String workspaceId, DatProcess datProcess) throws EarthException {
        if ((EStringUtil.isEmpty(workspaceId)) || (datProcess == null)) {
            throw new EarthException("Invalid parameter");
        }
        datProcess.setLastUpdateTime(DateUtil.getCurrentDateString());
        QDatProcess qDatProcess = QDatProcess.newInstance();
        return ConnectionManager.getEarthQueryFactory(workspaceId)
            .update(qDatProcess)
            .populate(datProcess)
            .where(qDatProcess.processId.eq(datProcess.getProcessId()))
            .where(qDatProcess.workItemId.eq(datProcess.getWorkItemId()))
            .execute();
    }

    @Override
    public Integer getMaxId(String workspaceId, String workItemId) throws EarthException {
        try {
            QDatProcess qDatProcess = QDatProcess.newInstance();
            return ConnectionManager.getEarthQueryFactory(workspaceId).select(qDatProcess.processId.max())
                .from(qDatProcess)
                .where(qDatProcess.workItemId.eq(workItemId))
                .fetchOne();
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }
}
