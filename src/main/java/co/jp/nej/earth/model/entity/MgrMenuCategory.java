package co.jp.nej.earth.model.entity;

import java.io.Serializable;

/**
 * 
 * @author p-tvo-thuynd
 *
 */
public class MgrMenuCategory implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String functionCategoryId;
    private String functionCategoryName;
    private int functionSortNo;
    
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
