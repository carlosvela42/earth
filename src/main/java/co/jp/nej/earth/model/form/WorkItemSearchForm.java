package co.jp.nej.earth.model.form;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;

import co.jp.nej.earth.model.WorkItem;

public class WorkItemSearchForm extends RestToken {
    @NotEmpty(message = "E0002,workspaceId")
    private String workspaceId;
    @Valid
    private WorkItem searchCondition;

    /**
     * @return the workspaceId
     */
    public String getWorkspaceId() {
        return workspaceId;
    }

    /**
     * @param workspaceId
     *            the workspaceId to set
     */
    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    /**
     * @return the searchCondition
     */
    public WorkItem getSearchCondition() {
        return searchCondition;
    }

    /**
     * @param searchCondition
     *            the searchCondition to set
     */
    public void setSearchCondition(WorkItem searchCondition) {
        this.searchCondition = searchCondition;
    }

}
