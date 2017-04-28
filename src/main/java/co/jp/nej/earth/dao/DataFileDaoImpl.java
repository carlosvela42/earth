package co.jp.nej.earth.dao;

import org.springframework.stereotype.Repository;

import co.jp.nej.earth.model.entity.StrDataFile;

@Repository
public class DataFileDaoImpl extends BaseDaoImpl<StrDataFile> implements DataFileDao {

    public DataFileDaoImpl() throws Exception {
        super();
    }

}
