package co.jp.nej.earth.model.form;

import org.hibernate.validator.constraints.NotEmpty;

public class EventControlForm extends RestToken {
    @NotEmpty(message = "E0002,workspaceId")
    private String workspaceId;
    @NotEmpty(message = "E0002,eventId")
    private String eventId;

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
     * @return the eventId
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * @param eventId
     *            the eventId to set
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

}
