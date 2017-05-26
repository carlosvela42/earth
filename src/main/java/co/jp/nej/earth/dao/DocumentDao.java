package co.jp.nej.earth.dao;

import java.util.List;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Document;

public interface DocumentDao extends BaseDao<Document> {
    List<Document> getAllByWorkspace(String workspaceId, String workitemId, int folderItemNo, String documentNo)
            throws EarthException;
    List<Document> getAll(String workspaceId, String workitemId, int folderItemNo)
            throws EarthException;
}
