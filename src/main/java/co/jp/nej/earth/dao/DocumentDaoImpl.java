package co.jp.nej.earth.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.model.Document;
import co.jp.nej.earth.model.Layer;
import co.jp.nej.earth.model.sql.QDocument;
import co.jp.nej.earth.model.sql.QLayer;

@Repository
public class DocumentDaoImpl extends BaseDaoImpl<Document> implements DocumentDao {

    public DocumentDaoImpl() throws Exception {
        super();
    }

    public List<Document> getAllByWorkspace(String workspaceId, String workitemId, int folderItemNo, String documentNo)
            throws EarthException {
        QDocument qDocument = QDocument.newInstance();
        QLayer qLayer = QLayer.newInstance();
        List<Document> documents = ConnectionManager.getEarthQueryFactory(workspaceId).select(qDocument).from(qDocument)
                .where(qDocument.workitemId.eq(workitemId).and(qDocument.folderItemNo.eq(folderItemNo))).fetch();
        if (documents != null && documents.size() > 0) {
            for (Document document : documents) {
                List<Layer> layers = ConnectionManager.getEarthQueryFactory(workspaceId).select(qLayer).from(qLayer)
                        .where(qLayer.workitemId.eq(document.getWorkitemId()).and(qLayer.folderItemNo
                                .eq(document.getFolderItemNo()).and(qLayer.documentNo.eq(document.getDocumentNo()))))
                        .fetch();
                document.setLayers(layers);
            }
        }
        return documents;
    }

    public List<Document> getAll(String workspaceId, String workitemId, int folderItemNo) throws EarthException {
        QDocument qDocument = QDocument.newInstance();
        List<Document> documents = ConnectionManager.getEarthQueryFactory(workspaceId).select(qDocument).from(qDocument)
                .where(qDocument.workitemId.eq(workitemId).and(qDocument.folderItemNo.eq(folderItemNo)))
                .orderBy(qDocument.documentNo.asc()).fetch();
        return documents;
    }
}
