package co.jp.nej.earth.model.ws;

import co.jp.nej.earth.model.WorkItem;

public class GetWorkitemResponse {
    private WorkItem workitem;

    /**
     * @return the workitem
     */
    public WorkItem getWorkitem() {
        return workitem;
    }

    /**
     * @param workitems the workitem to set
     */
    public void setWorkitem(WorkItem workitem) {
        this.workitem = workitem;
    }
}
