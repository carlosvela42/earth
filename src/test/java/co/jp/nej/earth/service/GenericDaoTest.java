package co.jp.nej.earth.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;

import co.jp.nej.earth.BaseTest;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.CtlLogin;
import co.jp.nej.earth.model.sql.QCtlLogin;

/**
 * Test generic DAO.
 * 
 * @author landd
 *
 */
public class GenericDaoTest extends BaseTest {

    private static final Logger LOG = LoggerFactory.getLogger(GenericDaoTest.class);

    private static final QCtlLogin qObject = QCtlLogin.newInstance();

    @Autowired
    UserService userService;

    @Test
    public void testGenericFindAll() throws EarthException {

        List<CtlLogin> ctlLogins = userService.getAllMgrLogin(Constant.EARTH_WORKSPACE_ID, null, null,
                new OrderSpecifier<>(Order.ASC, QCtlLogin.newInstance().userId));

        LOG.info("Found " + (ctlLogins != null ? ctlLogins.size() : 0));
        Assert.assertTrue(ctlLogins != null && ctlLogins.size() > 0);
    }

    // @Test
    public void testGenericFindOne() throws EarthException {

        Map<Path<?>, Object> condition = new HashMap<>();
        condition.put(qObject.sessionId, "YWRtaW58MTQ5MTU2MTU0NDYwNw==");
        // condition.put(qObject.testLong, 98); // false case
        condition.put(qObject.testLong, 99);
        CtlLogin ctlLogin = userService.getCtlLoginDetail(condition);

        // LOG.info("LOGIN TIME: " + ctlLogin.getLoginTime());
        Assert.assertTrue(ctlLogin != null);
    }

    private void prepareDataForDeleteOne() throws EarthException {

        CtlLogin login = new CtlLogin("333", "444", "555", null);

        userService.addCtlLogin(login);
    }

    // @Test
    public void testGenericDeleteOne() throws EarthException {

        prepareDataForDeleteOne();

        Map<Path<?>, Object> condition = new HashMap<>();
        condition.put(qObject.sessionId, "333");
        condition.put(qObject.userId, "444");
        boolean result = userService.deleteCtlLogin(condition);

        // LOG.info("LOGIN TIME: " + ctlLogin.getLoginTime());
        Assert.assertTrue(result);
    }

    private void prepareDataForDeleteList() throws EarthException {

        CtlLogin login = new CtlLogin("1234", "12345", "555", null);

        userService.addCtlLogin(login);

        login = new CtlLogin("2345", "23456", "666", null);

        userService.addCtlLogin(login);
    }

    // @Test
    public void testGenericDeleteList() throws EarthException {

//        prepareDataForDeleteList();
        List<Map<Path<?>, Object>> conditions = new ArrayList<>();

        Map<Path<?>, Object> condition1 = new HashMap<>();
        condition1.put(qObject.sessionId, "1234");
        condition1.put(qObject.userId, "12345");

        Map<Path<?>, Object> condition2 = new HashMap<>();
        condition2.put(qObject.sessionId, "2345");
        condition2.put(qObject.userId, "234567");

        conditions.add(condition1);
        conditions.add(condition2);

        boolean result = userService.deleteCtlLogins(conditions);

        Assert.assertTrue(result);
    }

    // @Test
    public void testGenericAdd() throws EarthException {

        CtlLogin login = new CtlLogin("3333", "444", "555", null);

        boolean result = userService.addCtlLogin(login);

        Assert.assertTrue(result);
    }

    @Test
    public void testGenericUpdate() throws EarthException {

        Map<Path<?>, Object> condition = new HashMap<>();

        condition.put(qObject.sessionId, "3333");

        Map<Path<?>, Object> valueMap = new HashMap<>();

        valueMap.put(qObject.testLong, 777);
        
        valueMap.put(qObject.testDate, new Date());

        boolean result = userService.updateCtlLogin(condition, valueMap);

        Assert.assertTrue(result);
    }
}
