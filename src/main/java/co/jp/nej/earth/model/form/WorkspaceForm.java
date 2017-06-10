package co.jp.nej.earth.model.form;

import co.jp.nej.earth.model.BaseModel;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.constant.Constant;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class WorkspaceForm extends BaseModel<MgrWorkspace> {

    @NotEmpty(message = "E0001,workspaceId")
    @Pattern(regexp = Constant.Regexp.ALPHABETS_VALIDATION, message = "E0007,workspaceId")
    private String workspaceId;

    @NotEmpty(message = "E0001,schemaName")
    @Pattern(regexp = Constant.Regexp.ALPHABETS_VALIDATION, message = "E0007,schemaName")
    private String schemaName;

    @NotEmpty(message = "E0001,dbUser")
    @Pattern(regexp = Constant.Regexp.ALPHABETS_VALIDATION, message = "E0007,dbUser")
    private String dbUser;

    @NotEmpty(message = "E0001,dbPassword")
    @Pattern(regexp = Constant.Regexp.ALPHABETS_VALIDATION, message = "E0007,dbPassword")
    private String dbPassword;

    @NotEmpty(message = "E0001,owner")
    @Pattern(regexp = Constant.Regexp.ALPHABETS_VALIDATION, message = "E0007,owner")
    private String owner;

    @NotEmpty(message = "E0001,dbServer")
    @Pattern(regexp = Constant.WorkSpace.IP_VALIDATION, message = "E0017,dbServer")
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
