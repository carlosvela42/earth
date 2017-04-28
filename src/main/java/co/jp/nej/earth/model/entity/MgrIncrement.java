package co.jp.nej.earth.model.entity;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.jp.nej.earth.model.BaseModel;
import co.jp.nej.earth.model.sql.QMgrIncrement;
import co.jp.nej.earth.util.DateUtil;

public class MgrIncrement extends BaseModel<MgrIncrement> implements Serializable {

    /**
     * Serial version
     */
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(MgrIncrement.class);

    private Integer processId;
    private String templateId;
    private Integer workspaceId;
    private Integer dataDirectoryId;
    private Integer siteId;
    private Integer scheduleId;

    public MgrIncrement() {

        LOG.debug("Call to blank constructor");
        this.setqObj(QMgrIncrement.newInstance());
        this.setLastUpdateTime(DateUtil.getCurrentDateString());
    }

    public Integer getProcessId() {
        return processId;
    }

    public void setProcessId(Integer processId) {
        this.processId = processId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Integer getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
    }

    public Integer getDataDirectoryId() {
        return dataDirectoryId;
    }

    public void setDataDirectoryId(Integer dataDirectoryId) {
        this.dataDirectoryId = dataDirectoryId;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }
}
