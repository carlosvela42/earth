package co.jp.nej.earth.service;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import co.jp.nej.earth.BaseTest;

public class DBunitTest extends BaseTest {

    @Autowired
    private ProcessService service;

    @Test
    public void getAllByWorkspace() throws Exception {
        String workspaceId = "003";
        File fileInput = new File("src\\test\\resources\\processList.xml");
        this.excuteTest(workspaceId, fileInput, () -> {
            try {
                //Assert.assertEquals(service.getAllByWorkspace(workspaceId).size(), 2);
            } catch (Exception e) {
                throw e;
            }
        });
    }

    // for many workspace
    @Test
    public void getAllByWorkspace2() throws Exception {
        String workspaceId = "003";
        File fileInput = new File("src\\test\\resources\\processList.xml");
        setUpDatabase(workspaceId, fileInput);
        //Assert.assertEquals(service.getAllByWorkspace(workspaceId).size(), 2);
        deleteAllData(workspaceId, fileInput);
    }

}
