package co.jp.nej.earth.model.sql;

import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPathBase;

import co.jp.nej.earth.model.Layer;
import co.jp.nej.earth.model.enums.ColumnNames;
import co.jp.nej.earth.model.enums.TableNames;

public class QLayer extends RelationalPathBase<Layer> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public final StringPath workitemId = createString(ColumnNames.WORKITEM_ID.toString());
    public final NumberPath<Integer> folderItemNo = createNumber(ColumnNames.FOLDER_ITEM_NO.toString(), Integer.class);
    public final NumberPath<Integer> documentNo = createNumber(ColumnNames.DOCUMENT_NO.toString(), Integer.class);
    public final NumberPath<Integer> layerNo = createNumber(ColumnNames.LAYER_NO.toString(), Integer.class);
    public final StringPath templateId = createString(ColumnNames.TEMPLATE_ID.toString());
    public final StringPath ownerId = createString(ColumnNames.OWNER_ID.toString());
    public final StringPath annotations = createString(ColumnNames.ANNOTATIONS.toString());
    public final StringPath lastUpdateTime = createString(ColumnNames.LAST_UPDATE_TIME.toString());

    public static QLayer newInstance() {
        return new QLayer(QLayer.class.getSimpleName(), null, TableNames.DAT_LAYER.name());
    }

    public QLayer(String path, String schema, String tableName) {
        super(Layer.class, PathMetadataFactory.forVariable(path), schema, tableName);
        addMetadata();
    }

    protected void addMetadata() {
        addMetadata(workitemId, ColumnMetadata.named(ColumnNames.WORKITEM_ID.toString()));
        addMetadata(folderItemNo, ColumnMetadata.named(ColumnNames.FOLDER_ITEM_NO.toString()));
        addMetadata(documentNo, ColumnMetadata.named(ColumnNames.DOCUMENT_NO.toString()));
        addMetadata(layerNo, ColumnMetadata.named(ColumnNames.LAYER_NO.toString()));
        addMetadata(templateId, ColumnMetadata.named(ColumnNames.TEMPLATE_ID.toString()));
        addMetadata(ownerId, ColumnMetadata.named(ColumnNames.OWNER_ID.toString()));
        addMetadata(annotations, ColumnMetadata.named(ColumnNames.ANNOTATIONS.toString()));
        addMetadata(lastUpdateTime, ColumnMetadata.named(ColumnNames.LAST_UPDATE_TIME.toString()));
    }
}
