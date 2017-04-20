package co.jp.nej.earth.model.sql;

import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPathBase;

import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.enums.ColumnNames;
import co.jp.nej.earth.model.enums.TableNames;

public class QMgrWorkspace extends RelationalPathBase<MgrWorkspace> {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    public final StringPath workspaceId = createString(ColumnNames.WORKSPACE_ID.toString());
    public final StringPath workspaceName = createString(ColumnNames.WORKSPACE_NAME.toString());
    public final StringPath lastUpdateTime = createString(ColumnNames.LAST_UPDATE_TIME.toString());

    public static QMgrWorkspace newInstance() {
        return new QMgrWorkspace(QMgrWorkspace.class.getSimpleName(), null, TableNames.MGR_WORKSPACE.name());
    }

    public QMgrWorkspace(String path, String schema, String tableName) {
        super(MgrWorkspace.class, PathMetadataFactory.forVariable(path), schema, tableName);
        addMetadata();
    }

    protected void addMetadata() {
        addMetadata(workspaceId, ColumnMetadata.named(ColumnNames.WORKSPACE_ID.toString()));
        addMetadata(workspaceName, ColumnMetadata.named(ColumnNames.WORKSPACE_NAME.toString()));
        addMetadata(lastUpdateTime, ColumnMetadata.named(ColumnNames.LAST_UPDATE_TIME.toString()));
    }
}
