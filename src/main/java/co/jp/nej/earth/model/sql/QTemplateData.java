package co.jp.nej.earth.model.sql;

import java.util.List;

import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPathBase;

import co.jp.nej.earth.model.Field;
import co.jp.nej.earth.model.TemplateData;
import co.jp.nej.earth.model.enums.ColumnNames;

public class QTemplateData extends RelationalPathBase<TemplateData> {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final StringPath processId = createString(ColumnNames.PROCESS_ID.toString());
    public final StringPath workItemId = createString(ColumnNames.WORKITEM_ID.toString());
    public final NumberPath<Integer> folderItemNo = createNumber(ColumnNames.FOLDER_ITEM_NO.toString(), Integer.class);
    public final NumberPath<Integer> documentNo = createNumber(ColumnNames.DOCUMENT_NO.toString(), Integer.class);
    public final NumberPath<Integer> layerNo = createNumber(ColumnNames.LAYER_NO.toString(), Integer.class);
    
    public final NumberPath<Integer> historyNo = createNumber(ColumnNames.HISTORY_NO.toString(), Integer.class);
    public final StringPath lastUpdateTime = createString(ColumnNames.LAST_UPDATE_TIME.toString());

    
    public static QTemplateData newInstance(String tableName, List<Field> fields) {
        return new QTemplateData(QTemplateData.class.getSimpleName(), null, tableName, fields);
    }
    
    
    public QTemplateData(String path, String schema, String table, List<Field> fields) {
        super(TemplateData.class, PathMetadataFactory.forVariable(path), schema, table);
    }

	protected void addMetadata(List<Field> fields) {
    	addMetadata(historyNo, ColumnMetadata.named(ColumnNames.HISTORY_NO.toString()));
    	addMetadata(lastUpdateTime, ColumnMetadata.named(ColumnNames.LAST_UPDATE_TIME.toString()));
        for (Field field : fields) {
            StringPath cPath = createString(field.getName());
            addMetadata(cPath, ColumnMetadata.named(field.getName()).ofType(field.getType()));
        }
    }
}