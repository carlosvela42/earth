package co.jp.nej.earth.model.sql;

import co.jp.nej.earth.model.entity.MgrIncrement;
import co.jp.nej.earth.model.enums.ColumnNames;
import co.jp.nej.earth.model.enums.TableNames;


import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;

public class QMgrIncrement extends QBase<MgrIncrement> {

    private static final long serialVersionUID = 1L;

    //public final NumberPath<Integer> id = createNumber(ColumnNames.INCREMENT_ID.toString(), Integer.class);
    public final StringPath incrementType = createString(ColumnNames.INCREMENTTYPE.toString());
    public final NumberPath<Integer> incrementData = createNumber(ColumnNames.INCREMENTDATA.toString(), Integer.class);
    public final StringPath incrementDateTime = createString(ColumnNames.INCREMENTDATETIME.toString());
    public final StringPath sessionId = createString(ColumnNames.SESSION_ID.toString());
    public final StringPath lastUpdateTime = createString(ColumnNames.LAST_UPDATE_TIME.toString());

    public static QMgrIncrement newInstance() {
        return new QMgrIncrement(QMgrIncrement.class.getSimpleName(), null, TableNames.MGR_INCREMENT.name());
    }

    public QMgrIncrement(String path, String schema, String table) {
        super(MgrIncrement.class, PathMetadataFactory.forVariable(path), schema, table);
        addMetadata();
    }

    protected void addMetadata() {
        //addMetadata(incrementType, ColumnMetadata.named(ColumnNames.INCREMENT_ID.toString()));
        addMetadata(incrementType, ColumnMetadata.named(ColumnNames.INCREMENTTYPE.toString()));
        addMetadata(incrementData, ColumnMetadata.named(ColumnNames.INCREMENTDATA.toString()));
        addMetadata(incrementDateTime, ColumnMetadata.named(ColumnNames.INCREMENTDATETIME.toString()));
        addMetadata(sessionId, ColumnMetadata.named(ColumnNames.SESSION_ID.toString()));
        addMetadata(lastUpdateTime, ColumnMetadata.named(ColumnNames.LAST_UPDATE_TIME.toString()));
    }

}
