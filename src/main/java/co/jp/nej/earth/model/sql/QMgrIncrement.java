package co.jp.nej.earth.model.sql;

import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPathBase;

import co.jp.nej.earth.model.entity.MgrIncrement;
import co.jp.nej.earth.model.enums.ColumnNames;
import co.jp.nej.earth.model.enums.TableNames;

public class QMgrIncrement extends RelationalPathBase<MgrIncrement> {

    private static final long serialVersionUID = 1L;

    public final NumberPath<Integer> processId = createNumber(ColumnNames.PROCESS_ID.toString(), Integer.class);
    public final StringPath templateId = createString(ColumnNames.TEMPLATE_ID.toString());
    public final NumberPath<Integer> workspaceId = createNumber(ColumnNames.WORKSPACE_ID.toString(), Integer.class);
    public final NumberPath<Integer> dataDirectoryId = createNumber(ColumnNames.DATA_DIRECTORY_ID.toString(),
                                                                    Integer.class);
    public final NumberPath<Integer> siteId = createNumber(ColumnNames.SITE_ID.toString(), Integer.class);
    public final NumberPath<Integer> scheduleId = createNumber(ColumnNames.SCHEDULE_ID.toString(), Integer.class);

    public final StringPath lastUpdateTime = createString(ColumnNames.LAST_UPDATE_TIME.toString());

    public static QMgrIncrement newInstance() {
        return new QMgrIncrement(QMgrIncrement.class.getSimpleName(), null, TableNames.MGR_INCREMENT.name());
    }

    public QMgrIncrement(String path, String schema, String table) {
        super(MgrIncrement.class, PathMetadataFactory.forVariable(path), schema, table);
        addMetadata();
    }

    protected void addMetadata() {
        addMetadata(processId, ColumnMetadata.named(ColumnNames.PROCESS_ID.toString()));
        addMetadata(templateId, ColumnMetadata.named(ColumnNames.TEMPLATE_ID.toString()));
        addMetadata(workspaceId, ColumnMetadata.named(ColumnNames.WORKSPACE_ID.toString()));
        addMetadata(dataDirectoryId, ColumnMetadata.named(ColumnNames.DATA_DIRECTORY_ID.toString()));
        addMetadata(siteId, ColumnMetadata.named(ColumnNames.SITE_ID.toString()));
        addMetadata(scheduleId, ColumnMetadata.named(ColumnNames.SCHEDULE_ID.toString()));
        addMetadata(lastUpdateTime, ColumnMetadata.named(ColumnNames.LAST_UPDATE_TIME.toString()));
    }

}
