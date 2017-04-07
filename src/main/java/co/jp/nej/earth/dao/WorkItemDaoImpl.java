/**
 * 
 */
package co.jp.nej.earth.dao;

import org.springframework.stereotype.Repository;

import co.jp.nej.earth.model.WorkItem;

/**
 * @author p-tvo-khanhnv
 *
 */
@Repository
public class WorkItemDaoImpl implements WorkItemDao {

    public WorkItem getWorkItemDataStructure(long workItemId) {
        // TODO Auto-generated method stub
        return null;
    }

    public WorkItem getWorkItemStructure(long workItemId) {
        // TODO Auto-generated method stub
        return null;
    }
    
//    public WorkItem getWorkItemDataStructure(long workItemId) {
//        // 1. Get WorkItem Structure.
//        WorkItem workItem = getWorkItemStructure(workItemId);
//        
//        // 2. Get WorkItem Template
//        List<Long> wkiTemplateIds = new ArrayList<Long>();
//        wkiTemplateIds.add(workItem.getTemplateId());
//        Template wkiTemplate = templateDao.getTemplates("", wkiTemplateIds).get(0);
//        
//        // 3. Get WorkItem Data by Template.
//        QTemplateData qTemplateData = QTemplateData.newInstance("QTemplateData", "spring_boot_test", wkiTemplate);
//        QBean<TemplateData> selectList = Projections.bean(TemplateData.class, qTemplateData.all());
//        TemplateData templateData = queryFactory.getDataTableByTemplate(selectList, wkiTemplate, qTemplateData , null);
//        
//        // 4. Get Documents Templates.
//        List<Long> docTemplateIds = new ArrayList<Long>();
//        List<FolderItem> folderItems = workItem.getFolderItems();
//        for (FolderItem folderItem : folderItems) {
//            List<Document> fDocuments = new ArrayList<Document>(folderItem.getDocuments());
//            for (Document document : fDocuments) {
//                docTemplateIds.add(document.getTemplateId());
//            }
//        }
//
//        List<Template> docTemplates = new ArrayList<Template>();
//        if (docTemplateIds.size() > 0) {
//            docTemplates = templateDao.getTemplate(docTemplateIds);
//        }
//
//        // 5. Get Documents Data by Templates.
////        List<TemplateData> docTemplateDatas = new ArrayList<TemplateData>();
//        Map<Long, TemplateData> docTemplateDatas = new HashMap<Long, TemplateData>();
//        for (Template docTemplate : docTemplates) {
//            QTemplateData qDocTemplateData = QTemplateData.newInstance("QTemplateData", "spring_boot_test", docTemplate);
//            QBean<TemplateData> docSelectList = Projections.bean(TemplateData.class, qDocTemplateData.all());
//            TemplateData docTemplateData = queryFactory.getDataTableByTemplate(docSelectList, docTemplate, qDocTemplateData , null);
//            docTemplateDatas.put(docTemplate.getId(), docTemplateData);
//        }
//
//        // 6. Set Template Data for WorkItem
//        workItem.setTemplateData(templateData);
//        
//        for (int i = 0; i < folderItems.size(); i++) {
//            FolderItem folderItem = folderItems.get(i);
//            List<Document> fDocuments = new ArrayList<Document>(folderItem.getDocuments());
//            for (int j = 0; j < fDocuments.size(); j++) {
//                Document document = fDocuments.get(j);
//                if (docTemplateDatas.containsKey(document.getTemplateId())) {
//                    fDocuments.get(j).setTemplateData(docTemplateDatas.get(document.getTemplateId()));
//                }
//            }
//
//            folderItems.get(i).setDocuments(fDocuments);
//        }
//        workItem.setFolderItems(folderItems);
//        
//        return workItem;
//    }
//
//    public WorkItem getWorkItemStructure(long workItemId) {
//        QWorkItem qWorkItem = QWorkItem.newInstance("QWorkItem", "spring_boot_test", "WORKITEM");
//        QFolder qFolder = QFolder.newInstance("QFolder", "spring_boot_test", "Folder");
//        QFolderItem qFolderItem = QFolderItem.newInstance("QFolderItem", "spring_boot_test", "FOLDERITEM");
//        QDocument qDocument = QDocument.newInstance("QDocument", "spring_boot_test", "DOCUMENT");
//        QLayer qLayer = QLayer.newInstance("QLayer", "spring_boot_test", "LAYER");
//        QTemplate qTemplate = QTemplate.newInstance("QTemplate", "spring_boot_test", "TEMPLATE");
////        QField qField = QField.newInstance("QFsield", "spring_boot_test", "FIELD");
////        QTemplateField qTemplateField = QTemplateField.newInstance("QTemplateField", "spring_boot_test", "TEMPLATEFIELD");
//
//        ResultSet resultSet = queryFactory.select(
//                    qWorkItem.workitemId
//                    ,qWorkItem.templateId
//                    ,qWorkItem.taskStatus
//                    ,qWorkItem.status
//                    ,qWorkItem.lastUpdateTime
//                    ,qFolder.folderId
//                    ,qFolder.folderName
//                    ,qFolderItem.folderItemId
//                    ,qFolderItem.documentId
//                    ,qDocument.documentId
//                    ,qDocument.templateId.as("document_template_id")
//                    ,qLayer.layerId
//                    ,qLayer.documentId)
//                .from(qWorkItem)
//                .innerJoin(qFolder).on(qWorkItem.folderId.eq(qFolder.folderId))
//                .innerJoin(qFolderItem).on(qFolder.folderId.eq(qFolderItem.folderId))
//                .innerJoin(qDocument).on(qDocument.documentId.eq(qFolderItem.documentId))
//                .innerJoin(qLayer).on(qDocument.documentId.eq(qLayer.documentId))
//                .innerJoin(qTemplate).on(qWorkItem.templateId.eq(qTemplate.templateId).or(qDocument.templateId.eq(qTemplate.templateId)))
//                .where(qWorkItem.workitemId.eq(workItemId))
//                .orderBy(qWorkItem.workitemId.asc(), qFolder.folderId.asc(), qFolderItem.folderItemId.asc(), qDocument.documentId.asc(), qLayer.layerId.asc())
//                .getResults();
//
//        WorkItem workItem = new WorkItem();
//        try {
////            Folder folder = new Folder();
//            FolderItem folderItem = new FolderItem();
//            Document document = new Document();
//            List<Layer> layers = new ArrayList<Layer>();
//
//            while (resultSet.next()) {
//                if (resultSet.isFirst()) {
//                    // Set WorkItem info.
//                    workItem.setWorkItemId(resultSet.getLong(WorkItemColumnName.WORKITEM_ID.columnName));
//                    workItem.setTemplateId(resultSet.getLong(WorkItemColumnName.TEMPLATE_ID.columnName));
//                    workItem.setStatus(resultSet.getInt(WorkItemColumnName.STATUS.columnName));
//                    workItem.setTaskStatus(resultSet.getInt(WorkItemColumnName.TASKS_TATUS.columnName));
//                    workItem.setLastUpdateTime(resultSet.getDate(WorkItemColumnName.LAST_UPDATE_TIME.columnName));
//
//                    // Set Folder info.
////                    folder.setId(resultSet.getLong(FolderColumnName.FOLDER_ID.columnName));
////                    folder.setFolderName(resultSet.getString(FolderColumnName.FOLDER_NAME.columnName));
//                }
//
//                // Get FolderItem info.
//                Long folderItemId = resultSet.getLong(FolderItemColumnName.FOLDER_ITEM_ID.columnName);
//                Long documentId = resultSet.getLong(DocumentColumnName.DOCUMENT_ID.columnName);
//                Long docTemplateId = resultSet.getLong("document_template_id");
//                if (folderItem.getFolderItemId() != null && folderItem.getFolderItemId().longValue() != folderItemId.longValue()) {
//                    folderItem.addDocument(document);
//                    workItem.addFolderItem(folderItem);
//                    folderItem = new FolderItem();
//                    document = new Document();
//                } else if (document.getId() != null && document.getId().longValue() != documentId.longValue()) {
//                    folderItem.addDocument(document);
//                    document = new Document();
//                }
//                document.setId(documentId);
//                document.setTemplateId(docTemplateId);
//                folderItem.setFolderItemId(folderItemId);
//                folderItem.setId(resultSet.getLong(FolderColumnName.FOLDER_ID.columnName));
//
//                // Get Layer info.
//                Layer layer = new Layer();
//                layer.setDocumentId(documentId);
//                layer.setId(resultSet.getLong(LayerColumnName.LAYER_ID.columnName));
//                layers.add(layer);
//                document.addLayer(layer);
//            }
//            
//            folderItem.addDocument(document);
//            workItem.addFolderItem(folderItem);
////            workItem.setFolderItems(folder);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return workItem;
//    }
}
