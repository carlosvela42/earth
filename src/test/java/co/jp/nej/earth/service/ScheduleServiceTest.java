package co.jp.nej.earth.service;

import co.jp.nej.earth.BaseTest;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MgrSchedule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ScheduleServiceTest extends BaseTest {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleServiceTest.class);

    private static final int RECORD = 0;
    private static final String WORKSPACE_ID = "003";

    private List<MgrSchedule> mgrSchedules = new ArrayList<>();

    @Before
    public void before() throws EarthException {

    }


    @Autowired
    private ScheduleService scheduleService;

    @Test
    public void getAll() throws EarthException {
        List<MgrSchedule> mgrSchedules = scheduleService.getSchedulesByWorkspaceId(WORKSPACE_ID);
        LOG.info("Get All schedules : " + mgrSchedules.size() + " records");
        Assert.assertTrue(mgrSchedules.size() >= RECORD);
    }

//    @Test
//    public void deleteList() throws EarthException {
//       boolean delete=scheduleService.deleteList(WORKSPACE_ID, Arrays.asList("1","2"));
//        LOG.info("Delete Schedule : " + delete);
//        Assert.assertTrue(delete);
//    }

}
