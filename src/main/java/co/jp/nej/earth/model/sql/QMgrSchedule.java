package co.jp.nej.earth.model.sql;

import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPathBase;

import co.jp.nej.earth.model.MgrSchedule;
import co.jp.nej.earth.model.enums.ColumnNames;
import co.jp.nej.earth.model.enums.TableNames;

public class QMgrSchedule extends RelationalPathBase<MgrSchedule> {

    private static final long serialVersionUID = 1L;

    public final StringPath scheduleId = createString(ColumnNames.SCHEDULE_ID.toString());
    public final StringPath processId = createString(ColumnNames.PROCESS_ID.toString());
    public final StringPath taskId = createString(ColumnNames.TASK_ID.toString());
    public final StringPath processName = createString(ColumnNames.PROCESS_NAME.toString());
    public final StringPath startTime = createString(ColumnNames.START_TIME.toString());
    public final StringPath repeatOption = createString(ColumnNames.REPEAT_OPTION.toString());
    public final StringPath endTime = createString(ColumnNames.END_TIME.toString());
    public final StringPath lastUpdateTime = createString(ColumnNames.LAST_UPDATE_TIME.toString());

    public static QMgrSchedule newInstance() {
        return new QMgrSchedule(QMgrSchedule.class.getSimpleName(), null, TableNames.MGR_SCHEDULE.name());
    }

    public QMgrSchedule(String path, String schema, String tableName) {
        super(MgrSchedule.class, PathMetadataFactory.forVariable(path), schema, tableName);
        addMetadata();
    }

    protected void addMetadata() {
        addMetadata(scheduleId, ColumnMetadata.named(ColumnNames.SCHEDULE_ID.toString()));
        addMetadata(processId, ColumnMetadata.named(ColumnNames.PROCESS_ID.toString()));
        addMetadata(taskId, ColumnMetadata.named(ColumnNames.TASK_ID.toString()));
        addMetadata(processName, ColumnMetadata.named(ColumnNames.PROCESS_NAME.toString()));
        addMetadata(startTime, ColumnMetadata.named(ColumnNames.START_TIME.toString()));
        addMetadata(repeatOption, ColumnMetadata.named(ColumnNames.REPEAT_OPTION.toString()));
        addMetadata(endTime, ColumnMetadata.named(ColumnNames.END_TIME.toString()));
        addMetadata(lastUpdateTime, ColumnMetadata.named(ColumnNames.LAST_UPDATE_TIME.toString()));
    }
}
