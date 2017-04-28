package co.jp.nej.earth.dao;

import org.springframework.stereotype.Repository;

import co.jp.nej.earth.model.FolderItem;

@Repository
public class FolderItemDaoImpl extends BaseDaoImpl<FolderItem> implements FolderItemDao {

    public FolderItemDaoImpl() throws Exception {
        super();
    }

}
