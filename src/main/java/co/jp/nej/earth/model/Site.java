package co.jp.nej.earth.model;

import co.jp.nej.earth.model.sql.QSite;
import co.jp.nej.earth.util.DateUtil;

/**
 *
 * @author p-tvo-sonta
 *
 */
public class Site extends BaseModel<Site> {
    /**
     * serial number
     */
    private static final long serialVersionUID = 1L;
    private Integer siteId;
    private Integer dataDirectoryId;

    public Site() {
        this.setqObj(QSite.newInstance());
        this.setLastUpdateTime(DateUtil.getCurrentDateString());
    }

    /**
     * @return the siteId
     */
    public Integer getSiteId() {
        return siteId;
    }

    /**
     * @param siteId
     *            the siteId to set
     */
    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    /**
     * @return the dataDirectoryId
     */
    public Integer getDataDirectoryId() {
        return dataDirectoryId;
    }

    /**
     * @param dataDirectoryId
     *            the dataDirectoryId to set
     */
    public void setDataDirectoryId(Integer dataDirectoryId) {
        this.dataDirectoryId = dataDirectoryId;
    }

}