
package co.jp.nej.earth.model.entity;

import java.io.Serializable;

import co.jp.nej.earth.model.MenuLink;

public class MgrMenu implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String functionId;
    private String functionName;
    private String functionCategoryId;
    private String functionCategoryName;
    private int functionSortNo;
    private String functionInformation;
    private MenuLink menuInformation;

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
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

    public String getFunctionInformation() {
        return functionInformation;
    }

    public void setFunctionInformation(String functionInformation) {
        this.functionInformation = functionInformation;
    }

    public MenuLink getMenuInformation() {
        return menuInformation;
    }

    public void setMenuInformation(MenuLink menuInformation) {
        this.menuInformation = menuInformation;
    }
}
