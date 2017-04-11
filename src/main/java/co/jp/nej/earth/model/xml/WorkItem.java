package co.jp.nej.earth.model.xml;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "WorkItem")
public class WorkItem {
    private long workSpaceId;
    private long workItemId;
    private long taskId;
    private String template;
    private List<FolderItem> folderItems;
    private Map<String, String> attributes;

    public long getWorkSpaceId() {
        return workSpaceId;
    }

    @XmlAttribute
    public void setWorkSpaceId(long workSpaceId) {
        this.workSpaceId = workSpaceId;
    }

    public long getWorkItemId() {
        return workItemId;
    }

    @XmlElement
    public void setWorkItemId(long workItemId) {
        this.workItemId = workItemId;
    }

    public long getTaskId() {
        return taskId;
    }

    @XmlElement
    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getTemplate() {
        return template;
    }

    @XmlElement
    public void setTemplate(String template) {
        this.template = template;
    }

    public List<FolderItem> getFolderItems() {
        return folderItems;
    }

    @XmlElement(name = "FolderItems")
    public void setFolderItems(List<FolderItem> folderItems) {
        this.folderItems = folderItems;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    @XmlElement(name = "Attributes")
    @XmlJavaTypeAdapter(MapAdapter.class)
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

}
