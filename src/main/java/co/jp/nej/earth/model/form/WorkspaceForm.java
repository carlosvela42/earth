package co.jp.nej.earth.model.form;

import org.hibernate.validator.constraints.NotEmpty;

import co.jp.nej.earth.model.BaseModel;
import co.jp.nej.earth.model.MgrWorkspace;

public class WorkspaceForm extends BaseModel<MgrWorkspace> {

    @NotEmpty(message = "E0001,workspace.id")
    // @Pattern(regexp = Constant.Regexp.ALPHABETS_VALIDATION, message = "E0011,workspaceId")
    private String workspaceId;

    @NotEmpty(message = "E0001,workspace.schemaName")
    // @Pattern(regexp = Constant.Regexp.ALPHABETS_VALIDATION, message = "E0011,schemaName")
    private String schemaName;

    @NotEmpty(message = "E0001,workspace.dbUser")
    // @Pattern(regexp = Constant.Regexp.ALPHABETS_VALIDATION, message = "E0011,dbUser")
    private String dbUser;

    @NotEmpty(message = "E0001,workspace.dbPassword")
    // @Pattern(regexp = Constant.Regexp.ALPHABETS_VALIDATION, message = "E0011,dbPassword")
    private String dbPassword;

    @NotEmpty(message = "E0001,workspace.owner")
    // @Pattern(regexp = Constant.Regexp.ALPHABETS_VALIDATION, message = "E0011,owner")
    private String owner;

    @NotEmpty(message = "E0001,workspace.dbServer")
    // @Pattern(regexp = Constant.WorkSpace.IP_VALIDATION, message = "E0011,dbServer")
    private String dbServer;

    private String dbType;

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDbServer() {
        return dbServer;
    }

    public void setDbServer(String dbServer) {
        this.dbServer = dbServer;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }
}
