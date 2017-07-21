package co.jp.nej.earth.dao;

import co.jp.nej.earth.model.MgrProcess;
import org.springframework.stereotype.Repository;

/**
 * @author p-tvo-sonta
 */
@Repository
public class ProcessDaoImpl extends BaseDaoImpl<MgrProcess> implements ProcessDao {

    public ProcessDaoImpl() throws Exception {
        super();
    }

}
