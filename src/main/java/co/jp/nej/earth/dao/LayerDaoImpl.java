package co.jp.nej.earth.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.model.Layer;
import co.jp.nej.earth.model.sql.QLayer;

@Repository
public class LayerDaoImpl extends BaseDaoImpl<Layer> implements LayerDao {

    public LayerDaoImpl() throws Exception {
        super();
    }

    public List<Layer> getAll(String workspaceId, String workitemId, int folderItemNo, int documentNo)
            throws EarthException {
        QLayer qLayer = QLayer.newInstance();
        List<Layer> layers = ConnectionManager.getEarthQueryFactory(workspaceId).select(qLayer).from(qLayer)
                .where(qLayer.workitemId.eq(workitemId)
                        .and(qLayer.folderItemNo.eq(folderItemNo).and(qLayer.documentNo.eq(documentNo))))
                .fetch();
        return layers;
    }
}
