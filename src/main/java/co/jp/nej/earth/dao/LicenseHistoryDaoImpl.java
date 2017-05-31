package co.jp.nej.earth.dao;

import org.springframework.stereotype.Repository;

import co.jp.nej.earth.model.entity.StrCal;

@Repository
public class LicenseHistoryDaoImpl extends BaseDaoImpl<StrCal> implements LicenseHistoryDao {

    public LicenseHistoryDaoImpl() throws Exception {
        super();
    }
}