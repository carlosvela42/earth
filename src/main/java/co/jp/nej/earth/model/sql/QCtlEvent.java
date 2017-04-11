package co.jp.nej.earth.model.sql;

import java.sql.NClob;

import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPathBase;

import co.jp.nej.earth.model.entity.CtlEvent;
import co.jp.nej.earth.model.enums.ColumnNames;
import co.jp.nej.earth.model.enums.TableNames;

public class QCtlEvent extends RelationalPathBase<CtlEvent> {

    private static final long serialVersionUID = 1L;

    public final StringPath eventId = createString(ColumnNames.EVEN_TID.toString());
    public final StringPath userId = createString(ColumnNames.USER_ID.toString());
    public final StringPath workitemId = createString(ColumnNames.WORKITEM_ID.toString());
    public final StringPath status = createString(ColumnNames.STATUS.toString());
    public final StringPath taskId = createString(ColumnNames.TASK_ID.toString());
    public final PathBuilder<NClob> workitemData = new PathBuilder<NClob>(NClob.class,
            ColumnNames.WORKITEM_DATA.toString());

    // public final StringPath workitemData =
    // createString(ColumnNames.WORKITEM_DATA.toString());
    public final StringPath lastUpdateTime = createString(ColumnNames.LAST_UPDATE_TIME.toString());

    public static QCtlEvent newInstance() {
        return new QCtlEvent(CtlEvent.class.getSimpleName(), null, TableNames.CTL_EVENT.name());
    }

    public QCtlEvent(String path, String schema, String table) {
        super(CtlEvent.class, PathMetadataFactory.forVariable(path), schema, table);
        addMetadata();
    }

    protected void addMetadata() {
        addMetadata(eventId, ColumnMetadata.named(ColumnNames.EVEN_TID.toString()));
        addMetadata(userId, ColumnMetadata.named(ColumnNames.USER_ID.toString()));
        addMetadata(workitemId, ColumnMetadata.named(ColumnNames.WORKITEM_ID.toString()));
        addMetadata(status, ColumnMetadata.named(ColumnNames.STATUS.toString()));
        addMetadata(taskId, ColumnMetadata.named(ColumnNames.TASK_ID.toString()));
        addMetadata(workitemData, ColumnMetadata.named(ColumnNames.WORKITEM_DATA.toString()));
        addMetadata(lastUpdateTime, ColumnMetadata.named(ColumnNames.LAST_UPDATE_TIME.toString()));
    }

}
