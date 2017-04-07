package co.jp.nej.earth.model.sql;

import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPathBase;

import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.enums.ColumnNames;
import co.jp.nej.earth.model.enums.TableNames;

public class QWorkItem extends RelationalPathBase<WorkItem> {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public final StringPath workItemId = createString(ColumnNames.WORKITEM_ID.toString());
    public final StringPath taskId = createString(ColumnNames.TASK_ID.toString());
    public final StringPath templateId = createString(ColumnNames.TEMPLATE_ID.toString());
    public final NumberPath<Integer> lastHistoryNo = createNumber(ColumnNames.LAST_HISTORY_NO.toString(), Integer.class);
    public final StringPath lastUpdateTime = createString(ColumnNames.LAST_UPDATE_TIME.toString());

    public static QWorkItem newInstance() {
        return new QWorkItem(QWorkItem.class.getSimpleName(), null, TableNames.DAT_WORKITEM.name());
    }
    
    public QWorkItem(String path, String schema, String tableName) {
        super(WorkItem.class, PathMetadataFactory.forVariable(path), schema, tableName);
        addMetadata();
    }
    
    protected void addMetadata() {
        addMetadata(workItemId, ColumnMetadata.named(ColumnNames.WORKITEM_ID.toString()));
        addMetadata(taskId, ColumnMetadata.named(ColumnNames.TASK_ID.toString()));
        addMetadata(templateId, ColumnMetadata.named(ColumnNames.TEMPLATE_ID.toString()));
        addMetadata(lastHistoryNo, ColumnMetadata.named(ColumnNames.LAST_HISTORY_NO.toString()));
        addMetadata(lastUpdateTime, ColumnMetadata.named(ColumnNames.LAST_UPDATE_TIME.toString()));
    }
}
