package co.jp.nej.earth.dao;

import co.jp.nej.earth.model.entity.StrDataFile;
import org.springframework.stereotype.Repository;

@Repository
public class DataFileDaoImpl extends BaseDaoImpl<StrDataFile> implements DataFileDao {

    public DataFileDaoImpl() throws Exception {
        super();
    }

}
