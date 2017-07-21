package co.jp.nej.earth.dao;

import co.jp.nej.earth.model.entity.StrCal;
import org.springframework.stereotype.Repository;

@Repository
public class LicenseHistoryDaoImpl extends BaseDaoImpl<StrCal> implements LicenseHistoryDao {

    public LicenseHistoryDaoImpl() throws Exception {
        super();
    }
}