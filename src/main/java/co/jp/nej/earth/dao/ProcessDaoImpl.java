package co.jp.nej.earth.dao;

import org.springframework.stereotype.Repository;

import co.jp.nej.earth.model.MgrProcess;

/**
 *
 * @author p-tvo-sonta
 *
 */
@Repository
public class ProcessDaoImpl extends BaseDaoImpl<MgrProcess> implements ProcessDao {

    public ProcessDaoImpl() throws Exception {
        super();
    }

}
