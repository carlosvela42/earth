package co.jp.nej.earth.dao;

import java.util.List;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Layer;

public interface LayerDao extends BaseDao<Layer> {
    List<Layer> getAll(String workspaceId, String workitemId, int folderItemNo, int documentNo)
            throws EarthException;
}
