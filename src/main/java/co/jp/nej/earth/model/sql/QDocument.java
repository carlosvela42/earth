package co.jp.nej.earth.model.sql;

import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPathBase;

import co.jp.nej.earth.model.Document;
import co.jp.nej.earth.model.enums.ColumnNames;
import co.jp.nej.earth.model.enums.TableNames;

public class QDocument extends RelationalPathBase<Document> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final StringPath workItemId = createString(ColumnNames.WORKITEM_ID.toString());
    public final NumberPath<Integer> folderItemNo = createNumber(ColumnNames.FOLDER_ITEM_NO.toString(), Integer.class);
    public final NumberPath<Integer> documentNo = createNumber(ColumnNames.DOCUMENT_NO.toString(), Integer.class);
    public final StringPath templateId = createString(ColumnNames.TEMPLATE_ID.toString());
    public final NumberPath<Integer> pageCount = createNumber(ColumnNames.PAGE_COUNT.toString(), Integer.class);
    public final StringPath viewInformation = createString(ColumnNames.VIEW_INFORMATION.toString());
    public final StringPath documentType = createString(ColumnNames.DOCUMENT_TYPE.toString());
    public final StringPath lastUpdateTime = createString(ColumnNames.LAST_UPDATE_TIME.toString());

    public static QDocument newInstance() {
        return new QDocument(QDocument.class.getSimpleName(), null, TableNames.DAT_DOCUMENT.name());
    }

    public QDocument(String path, String schema, String tableName) {
        super(Document.class, PathMetadataFactory.forVariable(path), schema, tableName);
        addMetadata();
    }

    protected void addMetadata() {
        addMetadata(workItemId, ColumnMetadata.named(ColumnNames.WORKITEM_ID.toString()));
        addMetadata(folderItemNo, ColumnMetadata.named(ColumnNames.FOLDER_ITEM_NO.toString()));
        addMetadata(documentNo, ColumnMetadata.named(ColumnNames.DOCUMENT_NO.toString()));
        addMetadata(templateId, ColumnMetadata.named(ColumnNames.TEMPLATE_ID.toString()));
        addMetadata(pageCount, ColumnMetadata.named(ColumnNames.PAGE_COUNT.toString()));
        addMetadata(viewInformation, ColumnMetadata.named(ColumnNames.VIEW_INFORMATION.toString()));
        addMetadata(documentType, ColumnMetadata.named(ColumnNames.DOCUMENT_TYPE.toString()));
        addMetadata(lastUpdateTime, ColumnMetadata.named(ColumnNames.LAST_UPDATE_TIME.toString()));
    }
}
