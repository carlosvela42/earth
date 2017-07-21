package co.jp.nej.earth.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.model.DatProcess;
import co.jp.nej.earth.model.Document;
import co.jp.nej.earth.model.FolderItem;
import co.jp.nej.earth.model.Layer;
import co.jp.nej.earth.model.TemplateAccessRight;
import co.jp.nej.earth.model.TemplateData;
import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.TransactionManager;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.WorkItemDictionary;
import co.jp.nej.earth.model.constant.Constant.ErrorCode;
import co.jp.nej.earth.model.entity.MgrTemplate;
import co.jp.nej.earth.model.enums.AccessRight;
import co.jp.nej.earth.model.enums.Operation;
import co.jp.nej.earth.util.EMessageResource;
import co.jp.nej.earth.util.EModelUtil;

public abstract class BaseService {
    private static final Logger LOG = LoggerFactory.getLogger(BaseService.class);

    @Autowired
    protected EMessageResource messageSource;

    public interface ServiceCaller {
        Object execute() throws EarthException;
    }

    protected Object executeTransaction(ServiceCaller caller) throws EarthException {
        String workspaceId = co.jp.nej.earth.model.constant.Constant.EARTH_WORKSPACE_ID;
        return executeTransaction(workspaceId, caller);
    }

    protected Object executeTransaction(String workspaceId, ServiceCaller caller) throws EarthException {
        Object result = null;
        PlatformTransactionManager transactionManager = null;
        TransactionStatus txStatus = null;
        try {
            LOG.info("Start New Transaction with workspaceId=" + workspaceId);
            TransactionDefinition txDef = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
            transactionManager = ConnectionManager.getTransactionManager(workspaceId);
            txStatus = transactionManager.getTransaction(txDef);
            result = caller.execute();
            transactionManager.commit(txStatus);
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            if (transactionManager != null) {
                transactionManager.rollback(txStatus);
            }
            throw ex;
        } finally {
            LOG.info("Finish Transaction.");
        }
        return result;
    }

    protected boolean processCommitTransactions(List<TransactionManager> transactionManagers) {
        for (int i = transactionManagers.size() - 1; i >= 0; i--) {
            transactionManagers.get(i).getManager().commit(transactionManagers.get(i).getTxStatus());
        }

        return true;
    }

    protected boolean processRollBackTransactions(List<TransactionManager> transactionManagers) {
        for (int i = transactionManagers.size() - 1; i >= 0; i--) {
            transactionManagers.get(i).getManager().rollback(transactionManagers.get(i).getTxStatus());
        }
        return true;
    }

    protected Object getDataItemFromSession(HttpSession session, String workItemDictionarySessionKey,
            String itemIndex) throws EarthException {
        // Get work item data from session.
        WorkItemDictionary workItemDics = (WorkItemDictionary) session.getAttribute(workItemDictionarySessionKey);
        if (workItemDics == null || workItemDics.size() == 0) {
            throw new EarthException(messageSource.get(ErrorCode.E1013, new String[] { "workItem" }));
        }

        Object object = workItemDics.get(itemIndex);
        if (object == null) {
            throw new EarthException(messageSource.get(ErrorCode.E1013, new String[] { "data" }));
        }

        return object;
    }

    protected void setDataItemToSession(HttpSession session, String workItemDictionarySessionKey,
            String itemIndex, Object itemValue) throws EarthException {
        // Get work item data from session.
        WorkItemDictionary workItemDics = (WorkItemDictionary) session.getAttribute(workItemDictionarySessionKey);
        if (workItemDics.size() == 0) {
            throw new EarthException(messageSource.get(ErrorCode.E1013, new String[] { "workItem" }));
        }

        workItemDics.put(itemIndex, itemValue);
        session.setAttribute(workItemDictionarySessionKey, workItemDics);
    }

    protected WorkItemDictionary createWorkItemDictionaries(WorkItem workItem, Map<String, MgrTemplate> templates,
                                                            Map<String, TemplateData> templateDataMap,
                                                            TemplateAccessRight templateAccessRight) {
        WorkItemDictionary workItemDics = new WorkItemDictionary();
        TemplateKey templateKey = new TemplateKey(workItem.getWorkspaceId(), workItem.getTemplateId());

        workItemDics.put(EModelUtil.getWorkItemIndex(workItem.getWorkitemId()), workItem);
        workItem.setMgrTemplate(templates.get(workItem.getTemplateId()));
        workItem.setWorkItemData(templateDataMap.get(EModelUtil.getWorkItemIndex(workItem.getWorkitemId())));

        // Set access right
        workItem.setAccessRight(templateAccessRight.get(templateKey));

        DatProcess processMap = workItem.getDataProcess();
        workItemDics.put(EModelUtil.getProcessIndex(String.valueOf(processMap.getProcessId())),
                workItem.getDataProcess());
        processMap.setMgrTemplate(templates.get(processMap.getTemplateId()));
        processMap.setProcessData(
                templateDataMap.get(EModelUtil.getProcessIndex(String.valueOf(processMap.getProcessId()))));

        // Set access right
        templateKey.setTemplateId(processMap.getTemplateId());
        processMap.setAccessRight(templateAccessRight.get(templateKey));

        List<FolderItem> folderItems = workItem.getFolderItems();
        for (FolderItem ft : folderItems) {
            workItemDics.put(EModelUtil.getFolderItemIndex(ft.getWorkitemId(), ft.getFolderItemNo()), ft);
            ft.setMgrTemplate(templates.get(ft.getTemplateId()));
            ft.setFolderItemData(templateDataMap
                    .get(EModelUtil.getFolderItemIndex(ft.getWorkitemId(), ft.getFolderItemNo())));
            // Set access right
            templateKey.setTemplateId(ft.getTemplateId());
            ft.setAccessRight(templateAccessRight.get(templateKey));
            if (workItem.getAccessRight().getValue() < ft.getAccessRight().getValue()) {
                ft.setAccessRight(workItem.getAccessRight());
            }

            List<Document> documents = ft.getDocuments();
            for (Document doc : documents) {
                workItemDics.put(EModelUtil.getDocumentIndex(doc.getWorkitemId(), doc.getFolderItemNo(),
                        doc.getDocumentNo()), doc);
                doc.setMgrTemplate(templates.get(doc.getTemplateId()));
                doc.setDocumentData(templateDataMap.get(EModelUtil.getDocumentIndex(doc.getWorkitemId(),
                        doc.getFolderItemNo(), doc.getDocumentNo())));
                // Set access right
                templateKey.setTemplateId(doc.getTemplateId());
                doc.setAccessRight(templateAccessRight.get(templateKey));
                if (ft.getAccessRight().getValue() < doc.getAccessRight().getValue()) {
                    doc.setAccessRight(ft.getAccessRight());
                }

                List<Layer> layers = doc.getLayers();
                for (Layer layer : layers) {
                    workItemDics.put(
                            EModelUtil.getLayerIndex(layer.getWorkitemId(), layer.getFolderItemNo(),
                                    layer.getDocumentNo(), layer.getLayerNo()), layer);
                    layer.setMgrTemplate(templates.get(layer.getTemplateId()));
                    layer.setLayerData(templateDataMap.get(
                            EModelUtil.getLayerIndex(layer.getWorkitemId(), layer.getFolderItemNo(),
                                    layer.getDocumentNo(), layer.getLayerNo())));
                    // Set access right
                    templateKey.setTemplateId(layer.getTemplateId());
                    layer.setAccessRight(templateAccessRight.get(templateKey));
                    if (doc.getAccessRight().getValue() < layer.getAccessRight().getValue()) {
                        layer.setAccessRight(doc.getAccessRight());
                    }
                }
            }
        }

        return workItemDics;
    }

    protected WorkItemDictionary createWorkItemDictionaries(WorkItem workItem) {
        WorkItemDictionary workItemDics = new WorkItemDictionary();

        workItemDics.put(EModelUtil.getWorkItemIndex(workItem.getWorkitemId()), workItem);

        DatProcess processMap = workItem.getDataProcess();
        workItemDics.put(EModelUtil.getProcessIndex(String.valueOf(processMap.getProcessId())),
                workItem.getDataProcess());

        List<FolderItem> folderItems = workItem.getFolderItems();
        for (FolderItem ft : folderItems) {
            workItemDics.put(EModelUtil.getFolderItemIndex(ft.getWorkitemId(), ft.getFolderItemNo()),
                    ft);

            List<Document> documents = ft.getDocuments();
            for (Document doc : documents) {
                workItemDics.put(EModelUtil.getDocumentIndex(doc.getWorkitemId(), doc.getFolderItemNo(),
                        doc.getDocumentNo()), doc);

                List<Layer> layers = doc.getLayers();
                for (Layer layer : layers) {
                    workItemDics.put(
                            EModelUtil.getLayerIndex(layer.getWorkitemId(), layer.getFolderItemNo(),
                                    layer.getDocumentNo(), layer.getLayerNo()),
                            layer);
                }
            }
        }

        return workItemDics;
    }

    protected boolean isOpennedWorkItem(HttpSession session, String workItemDictionarySessionKey) {
        WorkItemDictionary workItemDics = (WorkItemDictionary) session.getAttribute(workItemDictionarySessionKey);
        return workItemDics != null && workItemDics.size() > 0;
    }

    protected void checkPermission(Object object, Operation operation) throws EarthException {
        boolean havePermission = false;
        AccessRight accessRight = null;
        if (object instanceof WorkItem) {
            WorkItem workItem = (WorkItem) object;
            accessRight = workItem.getAccessRight();
        } else if (object instanceof DatProcess) {
            DatProcess process = (DatProcess) object;
            accessRight = process.getAccessRight();
        } else if (object instanceof FolderItem) {
            FolderItem ft = (FolderItem) object;
            accessRight = ft.getAccessRight();
        } else if (object instanceof Document) {
            Document doc = (Document) object;
            accessRight = doc.getAccessRight();
        } else if (object instanceof Layer) {
            Layer layer = (Layer) object;
            accessRight = layer.getAccessRight();
        }

        if (accessRight == null) {
            accessRight = AccessRight.NONE;
        }

        if (Operation.SEARCH_INFO.equal(operation)) {
            havePermission = accessRight.goe(AccessRight.SO);
        }

        if (Operation.GET_DATA.equal(operation)) {
            havePermission = accessRight.goe(AccessRight.RO);
        }

        if (Operation.UPDATE_DATA.equal(operation)) {
            havePermission = accessRight.goe(AccessRight.RW);
        }

        if (Operation.DELETE_DATA.equal(operation)) {
            havePermission = accessRight.eq(AccessRight.FULL);
        }

        if (!havePermission) {
            throw new EarthException(messageSource.get(ErrorCode.E0025));
        }
    }
}
