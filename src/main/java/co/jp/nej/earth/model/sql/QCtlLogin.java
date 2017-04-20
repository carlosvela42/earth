package co.jp.nej.earth.model.sql;

import java.util.Date;

import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPathBase;

import co.jp.nej.earth.model.entity.CtlLogin;
import co.jp.nej.earth.model.enums.ColumnNames;
import co.jp.nej.earth.model.enums.TableNames;

public class QCtlLogin extends RelationalPathBase<CtlLogin> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public final StringPath sessionId = createString(ColumnNames.SESSION_ID.toString());
    public final StringPath userId = createString(ColumnNames.USER_ID.toString());
    public final StringPath loginTime = createString(ColumnNames.LOGIN_TIME.toString());
    public final StringPath logoutTime = createString(ColumnNames.LOGOUT_TIME.toString());
    public final StringPath lastUpdatedTime = createString(ColumnNames.LAST_UPDATE_TIME.toString());

    // Just for test
    public final NumberPath<Long> testLong = createNumber("testLong", Long.class);
    public final DatePath<Date> testDate = createDate("testDate", Date.class);

    public static QCtlLogin newInstance() {
        return new QCtlLogin(QCtlLogin.class.getSimpleName(), null, TableNames.CTL_LOGIN.name());
    }

    public QCtlLogin(String path, String schema, String table) {
        super(CtlLogin.class, PathMetadataFactory.forVariable(path), schema, table);
        addMetadata();

        // create primary key of Q_Object
        createPrimaryKey(sessionId);
    }

    protected void addMetadata() {
        addMetadata(sessionId, ColumnMetadata.named(ColumnNames.SESSION_ID.toString()));
        addMetadata(userId, ColumnMetadata.named(ColumnNames.USER_ID.toString()));
        addMetadata(loginTime, ColumnMetadata.named(ColumnNames.LOGIN_TIME.toString()));
        addMetadata(logoutTime, ColumnMetadata.named(ColumnNames.LOGOUT_TIME.toString()));
        addMetadata(lastUpdatedTime, ColumnMetadata.named(ColumnNames.LAST_UPDATE_TIME.toString()));
        addMetadata(testLong, ColumnMetadata.named("TESTLONG"));
        addMetadata(testDate, ColumnMetadata.named("TESTDATE"));
    }
}
