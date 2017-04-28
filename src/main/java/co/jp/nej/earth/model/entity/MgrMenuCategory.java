package co.jp.nej.earth.model.entity;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.jp.nej.earth.model.BaseModel;
import co.jp.nej.earth.model.sql.QMgrMenuCategory;
import co.jp.nej.earth.util.DateUtil;

/**
 *
 * @author p-tvo-thuynd
 *
 */
public class MgrMenuCategory extends BaseModel<MgrMenuCategory> implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(MgrMenuCategory.class);
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String functionCategoryId;
    private String functionCategoryName;
    private int functionSortNo;

    public MgrMenuCategory() {

        LOG.debug("Call to blank constructor");
        this.setqObj(QMgrMenuCategory.newInstance());
        this.setLastUpdateTime(DateUtil.getCurrentDateString());
    }

    public MgrMenuCategory(String functionCategoryId, String functionCategoryName, int functionSortNo) {
        this();
        LOG.debug("Call to (functionCategoryId, functionCategoryName, functionSortNo) constructor");
        this.functionCategoryId = functionCategoryId;
        this.functionCategoryName = functionCategoryName;
        this.functionSortNo = functionSortNo;
    }

    public String getFunctionCategoryId() {
        return functionCategoryId;
    }
    public void setFunctionCategoryId(String functionCategoryId) {
        this.functionCategoryId = functionCategoryId;
    }
    public String getFunctionCategoryName() {
        return functionCategoryName;
    }
    public void setFunctionCategoryName(String functionCategoryName) {
        this.functionCategoryName = functionCategoryName;
    }
    public int getFunctionSortNo() {
        return functionSortNo;
    }
    public void setFunctionSortNo(int functionSortNo) {
        this.functionSortNo = functionSortNo;
    }
}
