package co.jp.nej.earth.model.sql;

import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPathBase;

import co.jp.nej.earth.model.entity.MgrWorkitemId;
import co.jp.nej.earth.model.enums.ColumnNames;
import co.jp.nej.earth.model.enums.TableNames;

public class QMgrWorkitemId extends RelationalPathBase<MgrWorkitemId> {

    private static final long serialVersionUID = 1L;

    public final StringPath issueDate = createString(ColumnNames.ISSUE_DATE.toString());
    public final NumberPath<Integer> count = createNumber(ColumnNames.COUNT.toString(), Integer.class);
    public final StringPath lastUpdateTime = createString(ColumnNames.LAST_UPDATE_TIME.toString());

    public static QMgrWorkitemId newInstance() {
        return new QMgrWorkitemId(QMgrWorkitemId.class.getSimpleName(), null, TableNames.MGR_WORKITEMID.name());
    }

    public QMgrWorkitemId(String path, String schema, String table) {
        super(MgrWorkitemId.class, PathMetadataFactory.forVariable(path), schema, table);
        addMetadata();
    }

    protected void addMetadata() {
        addMetadata(issueDate, ColumnMetadata.named(ColumnNames.ISSUE_DATE.toString()));
        addMetadata(count, ColumnMetadata.named(ColumnNames.COUNT.toString()));
        addMetadata(lastUpdateTime, ColumnMetadata.named(ColumnNames.LAST_UPDATE_TIME.toString()));
    }
}
