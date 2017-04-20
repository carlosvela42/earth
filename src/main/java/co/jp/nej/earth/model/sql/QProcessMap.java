package co.jp.nej.earth.model.sql;

import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPathBase;

import co.jp.nej.earth.model.ProcessMap;
import co.jp.nej.earth.model.enums.ColumnNames;
import co.jp.nej.earth.model.enums.TableNames;

public class QProcessMap extends RelationalPathBase<ProcessMap> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public final StringPath processId = createString(ColumnNames.PROCESS_ID.toString());
    public final StringPath workItemId = createString(ColumnNames.WORKITEM_ID.toString());
    public final StringPath templateId = createString(ColumnNames.TEMPLATE_ID.toString());
    public final NumberPath<Float> processVersion = createNumber(ColumnNames.PROCESS_VERSION.toString(), Float.class);
    public final StringPath lastUpdateTime = createString(ColumnNames.LAST_UPDATE_TIME.toString());

    public static QProcessMap newInstance() {
        return new QProcessMap(QProcessMap.class.getSimpleName(), null, TableNames.DAT_PROCESS.name());
    }

    public QProcessMap(String path, String schema, String tableName) {
        super(ProcessMap.class, PathMetadataFactory.forVariable(path), schema, tableName);
        addMetadata();
    }

    protected void addMetadata() {
        addMetadata(processId, ColumnMetadata.named(ColumnNames.PROCESS_ID.toString()));
        addMetadata(workItemId, ColumnMetadata.named(ColumnNames.WORKITEM_ID.toString()));
        addMetadata(templateId, ColumnMetadata.named(ColumnNames.TEMPLATE_ID.toString()));
        addMetadata(processVersion, ColumnMetadata.named(ColumnNames.PROCESS_VERSION.toString()));
        addMetadata(lastUpdateTime, ColumnMetadata.named(ColumnNames.LAST_UPDATE_TIME.toString()));
    }
}
