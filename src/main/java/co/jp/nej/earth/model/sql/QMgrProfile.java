package co.jp.nej.earth.model.sql;

import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPathBase;

import co.jp.nej.earth.model.entity.MgrProfile;
import co.jp.nej.earth.model.enums.ColumnNames;
import co.jp.nej.earth.model.enums.TableNames;

public class QMgrProfile extends RelationalPathBase<MgrProfile>{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public final StringPath profileId = createString(ColumnNames.PROFILE_ID.toString());
    public final StringPath availableLicenseCount = createString(ColumnNames.AVAILABLE_LICENSE_COUNT.toString());
    public final StringPath description = createString(ColumnNames.DESCRIPTION.toString());
    public final StringPath ldapIdentifier = createString(ColumnNames.LDAP_IDENTIFIER.toString());
    public final StringPath lastUpdateTime = createString(ColumnNames.LAST_UPDATE_TIME.toString());

    public static QMgrProfile newInstance(){
        return new QMgrProfile(QMgrProfile.class.getSimpleName(), null, TableNames.MGR_PROFILE.name());
    }
    
    public QMgrProfile(String path, String schema, String table){
        super(MgrProfile.class, PathMetadataFactory.forVariable(path), schema, table);
        addMetadata();
    }
    
    protected void addMetadata(){
        addMetadata(profileId, ColumnMetadata.named(ColumnNames.PROFILE_ID.toString()));
        addMetadata(availableLicenseCount, ColumnMetadata.named(ColumnNames.AVAILABLE_LICENSE_COUNT.toString()));
        addMetadata(description, ColumnMetadata.named(ColumnNames.DESCRIPTION.toString()));
        addMetadata(ldapIdentifier, ColumnMetadata.named(ColumnNames.LDAP_IDENTIFIER.toString()));
        addMetadata(lastUpdateTime, ColumnMetadata.named(ColumnNames.LAST_UPDATE_TIME.toString()));
    }
}
