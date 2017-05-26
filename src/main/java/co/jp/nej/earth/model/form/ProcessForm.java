package co.jp.nej.earth.model.form;

import co.jp.nej.earth.model.*;

/**
 *
 * @author p-tvo-sonta
 *
 */
public class ProcessForm {
    private MgrProcess process;
    private StrageFile strageFile;
    private StrageDb strageDb;
    private String workspaceId;
    private String fileExtention;

    /**
     * @return the process
     */
    public MgrProcess getProcess() {
        return process;
    }

    /**
     * @param process
     *            the process to set
     */
    public void setProcess(MgrProcess process) {
        this.process = process;
    }

    /**
     * @return the strageFile
     */
    public StrageFile getStrageFile() {
        return strageFile;
    }

    /**
     * @param strageFile
     *            the strageFile to set
     */
    public void setStrageFile(StrageFile strageFile) {
        this.strageFile = strageFile;
    }

    /**
     * @return the strageDb
     */
    public StrageDb getStrageDb() {
        return strageDb;
    }

    /**
     * @param strageDb
     *            the strageDb to set
     */
    public void setStrageDb(StrageDb strageDb) {
        this.strageDb = strageDb;
    }

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
     * @return the fileExtention
     */
    public String getFileExtention() {
        return fileExtention;
    }

    /**
     * @param fileExtention the fileExtention to set
     */
    public void setFileExtention(String fileExtention) {
        this.fileExtention = fileExtention;
    }

}
