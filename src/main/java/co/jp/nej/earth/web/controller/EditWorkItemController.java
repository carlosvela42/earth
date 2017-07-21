package co.jp.nej.earth.web.controller;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.DatProcess;
import co.jp.nej.earth.model.Document;
import co.jp.nej.earth.model.Field;
import co.jp.nej.earth.model.FolderItem;
import co.jp.nej.earth.model.Layer;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.TemplateData;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.constant.Constant.Session;
import co.jp.nej.earth.model.entity.MgrCustomTask;
import co.jp.nej.earth.model.entity.MgrTemplate;
import co.jp.nej.earth.model.enums.EventType;
import co.jp.nej.earth.model.enums.TemplateType;
import co.jp.nej.earth.model.enums.Type;
import co.jp.nej.earth.service.CustomTaskService;
import co.jp.nej.earth.service.DocumentService;
import co.jp.nej.earth.service.FolderItemService;
import co.jp.nej.earth.service.LayerService;
import co.jp.nej.earth.service.ProcessService;
import co.jp.nej.earth.service.TemplateService;
import co.jp.nej.earth.service.WorkItemService;
import co.jp.nej.earth.util.EMessageResource;
import co.jp.nej.earth.util.EStringUtil;
import co.jp.nej.earth.util.SessionUtil;
import co.jp.nej.earth.util.TemplateUtil;
import co.jp.nej.earth.util.ValidatorUtil;
import co.jp.nej.earth.web.form.BaseEditWorkItemTemplateForm;
import co.jp.nej.earth.web.form.DocumentTemplateForm;
import co.jp.nej.earth.web.form.EditWorkItemForm;
import co.jp.nej.earth.web.form.FolderItemTemplateForm;
import co.jp.nej.earth.web.form.LayerTemplateForm;
import co.jp.nej.earth.web.form.ProcessTemplateForm;
import co.jp.nej.earth.web.form.WorkItemTemplateForm;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Edit work item data screen
 *
 * @author DaoPQ
 * @version 1.0
 */
@Controller
@RequestMapping("/workItem")
public class EditWorkItemController extends BaseController {

    @Autowired
    private WorkItemService workItemService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private FolderItemService folderItemService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private LayerService layerService;

    @Autowired
    private CustomTaskService customTaskService;

    @Autowired
    private ValidatorUtil validatorUtil;

    @Autowired
    private EMessageResource eMessageResource;

    /** And character string */
    private static final String AND_CHARACTER = "&";

    /**
     * Initial view of work item
     */
    private static final String VIEW_EDIT_WORKITEM = "workitem/workItemEdit";

    /**
     * Redirect work item list
     */
    private static final String WORKITEM_LIST = "workItem";

    /**
     * Partial view for template in work item screen
     */
    private static final String PARTIAL_TEMPLATE = "workitem/partial/template";

    /**
     * Partial view for template in work item screen
     */
    private static final String PARTIAL_TEMPLATE_FIELD = "workitem/partial/templateField";

    /**
     * Error message page
     */
    private static final String ERROR_MESSAGE_PAGE = "workitem/partial/messages";

    /**
     * Key: Template list to display on template screen
     */
    private static final String KEY_TEMPLATE_LIST = "templateList";

    /**
     * Key: Task list to display on template screen
     */
    private static final String KEY_TASK_LIST = "tasks";

    /**
     * Key: Work item to display on screen
     */
    private static final String KEY_WORK_ITEM = "workItem";

    /**
     * Key: Set item type on screen
     */
    private static final String KEY_ITEM_TYPE = "type";

    /**
     * Key: Current template id
     */
    private static final String KEY_CURRENT_TEMPLATE_ID = "currentTemplateId";

    /**
     * Key: Process Id
     */
    private static final String KEY_PROCESS_ID = "processId";

    /**
     * Key: Current task id
     */
    private static final String KEY_CURRENT_TASK_ID = "currentTaskId";

    /**
     * Key: work item id
     */
    private static final String KEY_WORK_ITEM_ID = "workItemId";

    /**
     * Key: Folder Item No
     */
    private static final String KEY_FOLDER_ITEM_NO = "folderItemNo";

    /**
     * Key: Document no
     */
    private static final String KEY_DOCUMENT_NO = "documentNo";

    /**
     * Key: Layer no
     */
    private static final String KEY_LAYER_NO = "layerNo";

    /**
     * Key: OwnerID
     */
    private static final String KEY_OWNER_ID = "ownerId";

    /**
     * Key: Workspace id
     */
    private static final String KEY_WORKSPACE_ID = "workspaceId";

    /**
     * Key: Fields
     */
    private static final String KEY_FIELDS = "fields";

    /**
     * Key: Template data (value)
     */
    private static final String KEY_TEMPLATE_DATA = "mapTemplate";

    /** Key to store template on session */
    private static final String KEY_TEMPLATE_SESSION = "templateSession";

    /**
     * Show edit screen
     *
     * @param model Model model
     * @param request HttpServletRequest
     * @return edit page
     * @throws EarthException Throw EarthException when has error
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(@ModelAttribute("workItemId") String workItemId, Model model, HttpServletRequest request)
        throws EarthException {

        // Validate request parameters
        HttpSession session = request.getSession();
        String workspaceId = SessionUtil.getSearchConditionWorkspaceId(session);

        WorkItem workItem = workItemService.getWorkItemStructureSession(session, workspaceId, workItemId);

        // Get task list
        List<MgrCustomTask> tasks = customTaskService.getAllCustomTasks();
        model.addAttribute(KEY_TASK_LIST, tasks);
        if(!StringUtils.isEmpty(workItem.getTaskId())) {
            model.addAttribute(KEY_CURRENT_TASK_ID, workItem.getTaskId());
        }

        model.addAttribute(KEY_WORK_ITEM, workItem);
        model.addAttribute(Session.SESSION_ID, session.getId());
        initialTemplateSession(session);
        return VIEW_EDIT_WORKITEM;
    }

    /**
     * Load partial template by Id to display on work item screen
     *
     * @param model   Model object
     * @param request HttpServletRequest object
     * @return Partial view template if finish normal, otherwise null
     * @throws EarthException Throw EarthException when has error
     */
    @RequestMapping(value = "/showTemplate", method = RequestMethod.POST)
    public String showTemplate(@Valid @ModelAttribute("EditWorkItemForm") EditWorkItemForm editWorkItemForm,
                               Model model, HttpServletRequest request) throws EarthException {
        String workspaceId = editWorkItemForm.getWorkspaceId();
        String type = editWorkItemForm.getType();

        // Get accessible template by login user
        HttpSession session = request.getSession();

        // Set display template
        String workItemId = editWorkItemForm.getWorkItemId();

        if (TemplateType.isProcess(type)) {
            // Process type
            Integer processId = Integer.valueOf(editWorkItemForm.getProcessId());
            setTemplateOfProcess(model, workspaceId, session, processId, workItemId);
        } else if (TemplateType.isWorkItem(type)) {
            // WorkItem type
            setTemplateOfWorkItem(model, session, workspaceId, workItemId);
        } else if (TemplateType.isFolderItem(type)) {

            // Folder Item type
            String folderItemNo = editWorkItemForm.getFolderItemNo();
            setTemplateOfFolderItem(model, session, workspaceId, workItemId, folderItemNo);
        } else if (TemplateType.isDocument(type)) {

            // Document type
            String folderItemNo = editWorkItemForm.getFolderItemNo();
            String documentNo = editWorkItemForm.getDocumentNo();
            setTemplateOfDocument(model, session, workspaceId, workItemId, folderItemNo, documentNo);
        } else if (TemplateType.isLayer(type)) {

            // Layer type
            String folderItemNo = editWorkItemForm.getFolderItemNo();
            String layerNo = editWorkItemForm.getLayerNo();
            String documentNo = editWorkItemForm.getDocumentNo();
            String ownerId = editWorkItemForm.getOwnerId();
            setTemplateOfLayer(model, session, workspaceId, workItemId, folderItemNo, documentNo, layerNo, ownerId);
        }

        // Setting template list
        List<String> templateIdList = TemplateUtil.getAccessibleTemplates(session, workspaceId);
        List<MgrTemplate> templateList = templateService.getByIdsAndType(workspaceId, templateIdList,
            TemplateType.valueOf(type));
        model.addAttribute(KEY_TEMPLATE_LIST, templateList);
        model.addAttribute(KEY_WORKSPACE_ID, workspaceId);
        model.addAttribute(KEY_ITEM_TYPE, type);

        return PARTIAL_TEMPLATE;
    }

    /**
     * Update process
     *
     * @param form    ProcessForm object
     * @param result BindingResult object
     * @param model   Model object
     * @param request HttpServletRequest object
     * @return list work item screen if ok, otherwise system error screen
     */
    @RequestMapping(value = "/updateProcess", method = RequestMethod.POST)
    public String updateProcess(@Valid @ModelAttribute("ProcessTemplateForm") ProcessTemplateForm form,
                                BindingResult result, Model model, HttpServletRequest request,
                                HttpServletResponse response) {
        try {
            // Validate request parameter
            // Validate common
            form.validateForm();

            String resultValidate = validateUpdateTemplate(result, model, response, form);
            if (!StringUtils.isEmpty(resultValidate)) {
                return resultValidate;
            }

            // ======================================//
            //  Set data to Process Map
            // ======================================//
            // Set Template manager
            DatProcess processMap = new DatProcess();
            processMap.setProcessId(Integer.valueOf(form.getProcessId()));
            processMap.setWorkItemId(form.getWorkItemId());

            // Set template
            String templateId = form.getTemplateId();
            if (StringUtils.isEmpty(templateId)) {
                processMap.setTemplateId(null);
                processMap.setMgrTemplate(null);
                processMap.setProcessData(null);
            } else {
                processMap.setTemplateId(form.getTemplateId());
                processMap.setMgrTemplate(copyFormToMgrTemplate(form));

                // Set Template Data
                processMap.setProcessData(copyDataToTemplateData(form));
            }

            // Update data to session
            boolean updateStatus = processService.updateProcessSession(request.getSession(), form.getWorkspaceId(),
                processMap);

            // Set error response
            if(!updateStatus) {
                return setUpdateError(response, model);
            }

            // Close and save data
            closeAndSave(form.getEventType(), request.getSession(), form.getWorkItemId(), form.getWorkspaceId());

        } catch (Exception ex) {
            return setExceptionResponse(response, model, ex);
        }

        return null;
    }

    /**
     * Update work item
     *
     * @param form ProcessForm object
     * @param result BindingResult object
     * @param model Model object
     * @param request HttpServletRequest object
     * @return list work item screen if ok, otherwise system error screen
     */
    @RequestMapping(value = "/updateWorkItem", method = RequestMethod.POST)
    public String updateWorkItem(@Valid @ModelAttribute("WorkItemTemplateForm") WorkItemTemplateForm form
        , BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            // Validate request parameter
            // Validate common
            form.validateForm();

            // Validate template data
            String resultValidate = validateUpdateTemplate(result, model, response, form);
            if (!StringUtils.isEmpty(resultValidate)) {
                return resultValidate;
            }

            // ======================================//
            //  Set data to Process Map
            // ======================================//
            // Set Template manager
            WorkItem workItem = new WorkItem();
            workItem.setWorkitemId(form.getWorkItemId());
            workItem.setTaskId(form.getTask());

            // Set template
            String templateId = form.getTemplateId();
            if (StringUtils.isEmpty(templateId)) {
                workItem.setTemplateId(null);
                workItem.setMgrTemplate(null);
                workItem.setWorkItemData(null);
            } else {
                workItem.setTemplateId(form.getTemplateId());
                workItem.setMgrTemplate(copyFormToMgrTemplate(form));

                // Set Template Data
                workItem.setWorkItemData(copyDataToTemplateData(form));
            }

            // Update data to session
            boolean updateStatus = workItemService.updateWorkItemSession(request.getSession(), form.getWorkspaceId(),
                workItem);

            // Set error response
            if(!updateStatus) {
                return setUpdateError(response, model);
            }

            // Close and save data
            closeAndSave(form.getEventType(), request.getSession(), form.getWorkItemId(), form.getWorkspaceId());
        } catch (Exception ex) {
            return setExceptionResponse(response, model, ex);
        }
        return null;
    }

    /**
     * Update folder item
     *
     * @param form    FolderItemTemplateForm object
     * @param result Binding result object
     * @param model   Model object
     * @param request HttpServletRequest object
     * @return list work item screen if update ok, otherwise system error screen
     */
    @RequestMapping(value = "/updateFolderItem", method = RequestMethod.POST)
    public String updateFolderItem(@Valid @ModelAttribute("FolderItemTemplateForm") FolderItemTemplateForm form,
                                   BindingResult result, Model model, HttpServletRequest request,
                                   HttpServletResponse response) {
        try {
            // Validate common
            form.validateForm();

            // Validate template data
            String resultValidate = validateUpdateTemplate(result, model, response, form);
            if (!StringUtils.isEmpty(resultValidate)) {
                return resultValidate;
            }

                // ======================================//
            //  Set data to Folder Item
            // ======================================//
            FolderItem folderItem = new FolderItem();
            folderItem.setFolderItemNo(form.getFolderItemNo());
            folderItem.setWorkitemId(form.getWorkItemId());

            // Set template
            String templateId = form.getTemplateId();
            if (StringUtils.isEmpty(templateId)) {
                folderItem.setTemplateId(null);
                folderItem.setMgrTemplate(null);
                folderItem.setFolderItemData(null);
            } else {
                folderItem.setTemplateId(form.getTemplateId());
                folderItem.setMgrTemplate(copyFormToMgrTemplate(form));

                // Set Template Data
                folderItem.setFolderItemData(copyDataToTemplateData(form));
            }

            // Update data to session
            boolean updateStatus = folderItemService.updateFolderItemSession(request.getSession(),
                form.getWorkspaceId(), form.getWorkItemId(), folderItem);

            // Set error response
            if(!updateStatus) {
                return setUpdateError(response, model);
            }

            // Close and save data
            closeAndSave(form.getEventType(), request.getSession(), form.getWorkItemId(), form.getWorkspaceId());
        } catch (Exception ex) {
            return setExceptionResponse(response, model, ex);
        }
        return null;
    }

    /**
     * Update folder item
     *
     * @param form    FolderItemTemplateForm object
     * @param result BindingResult object
     * @param model   Model object
     * @param request HttpServletRequest object
     * @return list work item screen if update ok, otherwise system error screen
     */
    @RequestMapping(value = "/updateDocument", method = RequestMethod.POST)
    public String updateDocument(@Valid @ModelAttribute("DocumentTemplateForm") DocumentTemplateForm form,
                                   BindingResult result, Model model, HttpServletRequest request,
                                   HttpServletResponse response) {
        try {
            // Validate common
            form.validateForm();

            // Validate template data
            String resultValidate = validateUpdateTemplate(result, model, response, form);
            if (!StringUtils.isEmpty(resultValidate)) {
                return resultValidate;
            }

            // ======================================//
            //  Set data to Document
            // ======================================//
            Document document = new Document();
            document.setDocumentNo(form.getDocumentNo());

            // Set template
            String templateId = form.getTemplateId();
            if (StringUtils.isEmpty(templateId)) {
                document.setTemplateId(null);
                document.setMgrTemplate(null);
                document.setDocumentData(null);
            } else {
                document.setTemplateId(form.getTemplateId());
                document.setMgrTemplate(copyFormToMgrTemplate(form));

                // Set Template Data
                document.setDocumentData(copyDataToTemplateData(form));
            }

            // Update data to session
            boolean updateStatus = documentService.updateDocumentSession(request.getSession(), form.getWorkspaceId(),
                form.getWorkItemId(), form.getFolderItemNo(), document);

            // Set error response
            if(!updateStatus) {
                return setUpdateError(response, model);
            }

            // Close and save data
            closeAndSave(form.getEventType(), request.getSession(), form.getWorkItemId(), form.getWorkspaceId());
        } catch (Exception ex) {
            return setExceptionResponse(response, model, ex);
        }

        return null;
    }

    /**
     * Update folder item
     *
     * @param form FolderItemTemplateForm object
     * @param result BindingResult object
     * @param model Model object
     * @param request HttpServletRequest object
     * @return list work item screen if update ok, otherwise system error screen
     */
    @RequestMapping(value = "/updateLayer", method = RequestMethod.POST)
    public String updateLayer(@Valid @ModelAttribute("LayerTemplateForm") LayerTemplateForm form,
                                 BindingResult result, Model model, HttpServletRequest request,
                                 HttpServletResponse response) {
        try {
            // Validate common
            form.validateForm();

            // Validate template data
            String resultValidate = validateUpdateTemplate(result, model, response, form);
            if (!StringUtils.isEmpty(resultValidate)) {
                return resultValidate;
            }

            // ======================================//
            //  Set data to Document
            // ======================================//
            Layer layer = new Layer();
            layer.setLayerNo(form.getLayerNo());

            // Set template
            String templateId = form.getTemplateId();
            if (StringUtils.isEmpty(templateId)) {
                layer.setTemplateId(null);
                layer.setMgrTemplate(null);
                layer.setLayerData(null);
            } else {
                layer.setTemplateId(form.getTemplateId());
                layer.setMgrTemplate(copyFormToMgrTemplate(form));

                // Set Template Data
                layer.setLayerData(copyDataToTemplateData(form));
            }

            // Update data to session
            boolean updateStatus = layerService.updateLayerSession(request.getSession(), form.getWorkspaceId(),
                form.getWorkItemId(), form.getFolderItemNo(), form.getDocumentNo(), layer);

            // Set error response
            if(!updateStatus) {
                return setUpdateError(response, model);
            }

            // Close and save data
            closeAndSave(form.getEventType(), request.getSession(), form.getWorkItemId(), form.getWorkspaceId());
        } catch (Exception ex) {
            return setExceptionResponse(response, model, ex);
        }
        return null;
    }

    /**
     * Load template field
     *
     * @param model Model object
     * @param request HttpServletRequest object
     * @param response HttpServletResponse object
     * @return Partial template field
     */
    @RequestMapping(value = "/setTemplateField", method = RequestMethod.POST)
    public String setTemplateField(Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            String field = request.getParameter("templateField");
            String type = request.getParameter("type");
            String templateId = request.getParameter("templateId");
            String processId = request.getParameter("processId");
            String workItemId = request.getParameter("workItemId");
            String folderItemNo = request.getParameter("folderItemNo");
            String documentNo = request.getParameter("documentNo");

            // UPGRADE Validate more parameters here

            // validate request parameter
            if (StringUtils.isEmpty(field)) {
                response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                List<Message> messages = new ArrayList<>();
                messages.add(new Message("field", "Invalid parameter templateField = null"));
                model.addAttribute(Constant.Session.MESSAGES, messages);
                return ERROR_MESSAGE_PAGE;
            }

            model.addAttribute(KEY_FIELDS, convertJsonToList(field));

            // Get template from session
            Map<String, Map<String, Object>> templateSession = getTemplateFromSession(request.getSession());
            StringBuilder templateKey = new StringBuilder(type).append(AND_CHARACTER);
            if (TemplateType.isProcess(type)) {
                templateKey.append(processId).append(AND_CHARACTER).append(templateId);
            } else if (TemplateType.isWorkItem(type)) {
                templateKey.append(workItemId).append(AND_CHARACTER).append(templateId);
            } else if (TemplateType.isFolderItem(type)) {
                templateKey.append(folderItemNo).append(AND_CHARACTER).append(templateId);
            } else if (TemplateType.isDocument(type)) {
                templateKey.append(folderItemNo).append(AND_CHARACTER).append(documentNo).append(AND_CHARACTER)
                        .append(templateId);
            }
            if ((templateSession != null) && (templateSession.containsKey(templateKey.toString()))) {
                model.addAttribute(KEY_TEMPLATE_DATA, templateSession.get(templateKey.toString()));
            }

            return PARTIAL_TEMPLATE_FIELD;
        } catch (Exception ex) {
            return setExceptionResponse(response, model, ex);
        }
    }

    /**
     * Close edit work item
     *
     * @param request HttpServletRequest object
     * @return Partial template field
     */
    @RequestMapping(value = "/closeEdit", method = RequestMethod.POST)
    public String close(HttpServletRequest request) {
        try {
            String workItemId = request.getParameter(KEY_WORK_ITEM_ID);
            String workspaceId = SessionUtil.getSearchConditionWorkspaceId(request.getSession());

            if (EStringUtil.isEmpty(workItemId) || (EStringUtil.isEmpty(workspaceId))) {
                throw new EarthException("Invalid parameter workItemId or workspaceId is null");
            }

            workItemService.closeWorkItem(request.getSession(), workspaceId, workItemId);
            return redirectToList(WORKITEM_LIST);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // =========================================================//
    // Private method
    // =========================================================//

    /**
     * Set response when has exception
     *
     * @param response HttpServletResponse response
     * @param model Model model
     * @param ex Exception
     * @return Error page
     */
    private String setExceptionResponse(HttpServletResponse response, Model model, Exception ex) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(ex.toString(), ex.getMessage()));
        model.addAttribute(Constant.Session.MESSAGES, messages);

        return ERROR_MESSAGE_PAGE;
    }

    /**
     * Get Template of process
     *
     * @param workspaceId Workspace ID
     * @param session HttpSession object
     * @param workItemId WorkItem ID
     * @throws EarthException Throw this exception when has any error
     */
    private void setTemplateOfProcess(Model model, String workspaceId, HttpSession session, Integer processId, String
        workItemId) throws EarthException {
        DatProcess data = processService.getProcessSession(session, workspaceId, workItemId, processId);

        String templateId = data.getTemplateId();
        if(!StringUtils.isEmpty(templateId)) {
            model.addAttribute(KEY_CURRENT_TEMPLATE_ID, templateId);
            if(data.getProcessData() != null && (data.getProcessData().getDataMap() != null)) {
                model.addAttribute(KEY_TEMPLATE_DATA, data.getProcessData().getDataMap());
                String key = new StringBuilder(TemplateType.PROCESS.toString())
                        .append(AND_CHARACTER)
                        .append(processId)
                        .append(AND_CHARACTER)
                        .append(templateId).toString();
                storeTemplateToSession(session, key, data.getProcessData().getDataMap());
            }
            if(data.getMgrTemplate() != null) {
                model.addAttribute(KEY_FIELDS, data.getMgrTemplate().getTemplateFields());
            }
        }

        model.addAttribute(KEY_PROCESS_ID, processId);
        model.addAttribute(KEY_WORK_ITEM_ID, workItemId);
    }

    /**
     * Get Template of process
     *
     * @param workspaceId Workspace ID
     * @param session HttpSession object
     * @param workItemId WorkItem ID
     * @throws EarthException Throw this exception when has any error
     */
    private void setTemplateOfWorkItem(Model model, HttpSession session, String workspaceId, String workItemId)
            throws EarthException {
        WorkItem data = workItemService.getWorkItemSession(session, workspaceId, workItemId);
        if (data == null) {
            return;
        }

        String templateId = data.getTemplateId();
        if(!StringUtils.isEmpty(templateId)) {
            model.addAttribute(KEY_CURRENT_TEMPLATE_ID, templateId);
            if(data.getWorkItemData() != null && (data.getWorkItemData().getDataMap() != null)) {
                model.addAttribute(KEY_TEMPLATE_DATA, data.getWorkItemData().getDataMap());
                String key = new StringBuilder(TemplateType.WORKITEM.toString())
                        .append(AND_CHARACTER)
                        .append(workItemId)
                        .append(AND_CHARACTER)
                        .append(templateId).toString();
                storeTemplateToSession(session, key, data.getWorkItemData().getDataMap());
            }
            if(data.getMgrTemplate() != null) {
                model.addAttribute(KEY_FIELDS, data.getMgrTemplate().getTemplateFields());
            }
        }
        model.addAttribute(KEY_WORK_ITEM_ID, workItemId);
    }


    /**
     * Get Template of FolderItem
     *
     * @param workspaceId Workspace ID
     * @param session HttpSession object
     * @param workItemId WorkItem ID
     * @param folderItemNo Folder Item No
     * @throws EarthException Throw this exception when has any error
     */
    private void setTemplateOfFolderItem(Model model, HttpSession session, String workspaceId, String workItemId,
                                         String folderItemNo) throws EarthException {
        FolderItem data = folderItemService.getFolderItemSession(session, workspaceId, workItemId, folderItemNo);
        if (data == null) {
            return;
        }

        String templateId = data.getTemplateId();
        if(!StringUtils.isEmpty(templateId)) {
            model.addAttribute(KEY_CURRENT_TEMPLATE_ID, templateId);
            if(data.getFolderItemData() != null && (data.getFolderItemData().getDataMap() != null)) {
                model.addAttribute(KEY_TEMPLATE_DATA, data.getFolderItemData().getDataMap());
                String key = new StringBuilder(TemplateType.FOLDERITEM.toString())
                        .append(AND_CHARACTER)
                        .append(folderItemNo)
                        .append(AND_CHARACTER)
                        .append(templateId).toString();
                storeTemplateToSession(session, key, data.getFolderItemData().getDataMap());
            }
            if(data.getMgrTemplate() != null) {
                model.addAttribute(KEY_FIELDS, data.getMgrTemplate().getTemplateFields());
            }
        }

        model.addAttribute(KEY_WORK_ITEM_ID, workItemId);
        model.addAttribute(KEY_FOLDER_ITEM_NO, folderItemNo);
    }

    /**
     * Get Template of Document
     *
     * @param workspaceId Workspace ID
     * @param session HttpSession object
     * @param workItemId WorkItem ID
     * @param folderItemNo Folder Item No
     * @param documentNo Document NO!
     * @throws EarthException Throw this exception when has any error
     */
    private void setTemplateOfDocument(Model model, HttpSession session, String workspaceId, String workItemId,
                                       String folderItemNo, String documentNo) throws EarthException {
        Document data = documentService.getDocumentSession(session, workspaceId, workItemId, folderItemNo, documentNo);
        if (data == null) {
            return;
        }

        String templateId = data.getTemplateId();
        if(!StringUtils.isEmpty(templateId)) {
            model.addAttribute(KEY_CURRENT_TEMPLATE_ID, templateId);
            if(data.getDocumentData() != null && (data.getDocumentData().getDataMap() != null)) {
                model.addAttribute(KEY_TEMPLATE_DATA, data.getDocumentData().getDataMap());
                String key = new StringBuilder(TemplateType.DOCUMENT.toString())
                        .append(AND_CHARACTER)
                        .append(folderItemNo)
                        .append(AND_CHARACTER)
                        .append(documentNo)
                        .append(AND_CHARACTER)
                        .append(templateId).toString();
                storeTemplateToSession(session, key, data.getDocumentData().getDataMap());
            }
            if(data.getMgrTemplate() != null) {
                model.addAttribute(KEY_FIELDS, data.getMgrTemplate().getTemplateFields());
            }
        }

        model.addAttribute(KEY_WORK_ITEM_ID, workItemId);
        model.addAttribute(KEY_FOLDER_ITEM_NO, folderItemNo);
        model.addAttribute(KEY_DOCUMENT_NO, documentNo);
    }

    /**
     * Get Template of Layer
     *
     * @param workspaceId Workspace ID
     * @param session HttpSession object
     * @param workItemId WorkItem ID
     * @param folderItemNo Folder Item No
     * @param documentNo Document NO
     * @param layerNo Layer NO
     * @param ownerId Owner ID
     * @throws EarthException Throw this exception when has any error
     */
    private void setTemplateOfLayer(Model model, HttpSession session, String workspaceId, String workItemId,
                                    String folderItemNo, String documentNo, String layerNo, String ownerId) throws
        EarthException {
        Layer data = layerService.getLayerSession(session, workspaceId, workItemId, folderItemNo, documentNo, layerNo);
        if (data == null) {
            return;
        }

        String templateId = data.getTemplateId();
        if(!StringUtils.isEmpty(templateId)) {
            model.addAttribute(KEY_CURRENT_TEMPLATE_ID, templateId);
            if(data.getLayerData() != null && (data.getLayerData().getDataMap() != null)) {
                model.addAttribute(KEY_TEMPLATE_DATA, data.getLayerData().getDataMap());
                storeTemplateToSession(session,TemplateType.LAYER + "&" + templateId, data.getLayerData()
                    .getDataMap());
            }
            if(data.getMgrTemplate() != null) {
                model.addAttribute(KEY_FIELDS, data.getMgrTemplate().getTemplateFields());
            }
        }

        model.addAttribute(KEY_WORK_ITEM_ID, workItemId);
        model.addAttribute(KEY_FOLDER_ITEM_NO, folderItemNo);
        model.addAttribute(KEY_DOCUMENT_NO, documentNo);
        model.addAttribute(KEY_LAYER_NO, layerNo);
        model.addAttribute(KEY_OWNER_ID, ownerId);
    }

    /**
     * Convert String with json format to List
     *
     * @param jsonValue JSONValue of object that need to cast to
     * @return List of Field
     * @throws EarthException Throw this exception when has any error
     */
    private List<Field> convertJsonToList(String jsonValue) throws EarthException {
        try {
            return new ObjectMapper().readValue(jsonValue, new TypeReference<List<Field>>() {
            });
        } catch (IOException e) {
            throw new EarthException(e);
        }
    }

    /**
     * Copy data template from form to entity
     *
     * @param form BaseEditWorkItemTemplateForm
     * @return MgrTemplate object
     * @throws EarthException Throw this exception when has any error
     */
    private MgrTemplate copyFormToMgrTemplate(BaseEditWorkItemTemplateForm form) throws
            EarthException {
        if (form == null) {
            return null;
        }
        MgrTemplate mgrTemplate = new MgrTemplate();
        mgrTemplate.setTemplateId(form.getTemplateId());
        mgrTemplate.setTemplateName(form.getTemplateName());
        mgrTemplate.setTemplateTableName(form.getTemplateTableName());
        mgrTemplate.setTemplateType(form.getTemplateType());

        String templateField = form.getTemplateField();
        List<Field> templateFields = null;
        if (templateField != null) {
            templateFields = convertJsonToList(templateField);
        }
        mgrTemplate.setTemplateField(templateField);
        if (templateFields != null) {
            mgrTemplate.setTemplateFields(templateFields);
        }

        return mgrTemplate;
    }

    /**
     * Set data to Template Data
     *
     * @param form BaseEditWorkItemTemplateForm
     * @return TemplateData object
     * @throws EarthException Throw this exception when has any error
     */
    @SuppressWarnings("unchecked")
    private TemplateData copyDataToTemplateData(BaseEditWorkItemTemplateForm form) throws
        EarthException, IOException {
        if (form == null) {
            return null;
        }
        TemplateData templateData = new TemplateData();
        List<?> listDataMap = new ObjectMapper().readValue(form.getTemplateData(), new TypeReference<List<?>>() {
        });
        Map<String, Object> dataMap = new HashMap();
        for (Object object : listDataMap) {
            if (object instanceof LinkedHashMap) {
                dataMap.putAll((LinkedHashMap) object);
            }
        }

        templateData.setDataMap(dataMap);

        return templateData;
    }

    /**
     * Validate form before save template
     *
     * @param result BindingResult object
     * @param model Model object
     * @param response HttpServletResponse object
     * @return True if validate success, otherwise false
     */
    @SuppressWarnings("unchecked")
    private String validateUpdateTemplate(BindingResult result, Model model, HttpServletResponse response,
                                          BaseEditWorkItemTemplateForm form) throws IOException, EarthException {
        if (form == null || (EStringUtil.isEmpty(form.getTemplateId()))) {
            return EStringUtil.EMPTY;
        }
        // Validate common
        List<Message> messages = validatorUtil.validate(result);

        List<Field> templateFields = null;
        if (form.getTemplateField() != null) {
            templateFields = convertJsonToList(form.getTemplateField());
        }
        List<?> listDataMap = new ObjectMapper().readValue(form.getTemplateData(), new TypeReference<List<?>>() {
        });
        Map<String, Object> dataMap = new HashMap<>();
        for (Object object : listDataMap) {
            if (object instanceof LinkedHashMap) {
                dataMap.putAll((LinkedHashMap) object);
            }
        }

        if (CollectionUtils.isEmpty(templateFields)) {
            return EStringUtil.EMPTY;
        }

        String fieldName;
        String fieldValue;
        int size = 0;
        for (Field field: templateFields) {
            if (field == null) {
                continue;
            }
            fieldName = field.getName();
            if (fieldName == null) {
                continue;
            }
            fieldValue = String.valueOf(dataMap.get(fieldName));
            if (field.getSize() != null) {
                size = field.getSize();
            }
            if ((field.getRequired()) && (EStringUtil.isEmpty(fieldValue))) {
                messages.add(new Message(fieldName,
                    eMessageResource.get(Constant.ErrorCode.E0001, new String[]{fieldName})));
            } else if (fieldValue.length() > size) {
                messages.add(new Message(fieldName,
                    eMessageResource.get(Constant.ErrorCode.E0026, new String[]{fieldName, String.valueOf(size)})));
            } else if ((Type.isNumber(field.getType())) && (!EStringUtil.isEmpty(fieldValue))
                && (!org.apache.commons.lang3.StringUtils.isNumeric(String.valueOf(dataMap.get(fieldName))))) {
                messages.add(new Message(fieldName, eMessageResource.get(Constant.ErrorCode.E0008,
                    new String[]{fieldName})));
            }
        }

        if (!CollectionUtils.isEmpty(messages)) {
            model.addAttribute(Constant.Session.MESSAGES, messages);
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return ERROR_MESSAGE_PAGE;
        }
        return EStringUtil.EMPTY;
    }

    /**
     * Initial template on session
     *
     * @param session HttpSession object
     */
    private void initialTemplateSession(HttpSession session) {
        Map<String, Map<String, Object>> mapTemplate = new HashMap<>();
        session.setAttribute(KEY_TEMPLATE_SESSION, mapTemplate);
    }

    /**
     * Initial template on session
     *
     * @param session HttpSession object
     */
    private void removeTemplateSession(HttpSession session) {
        session.removeAttribute(KEY_TEMPLATE_SESSION);
    }

    /**
     * Store Template to session
     *
     * @param session HttpSession object
     * @param key Session key
     * @param templateValue Value of template
     */
    @SuppressWarnings("unchecked")
    private void storeTemplateToSession(HttpSession session, String key, Map<String, Object> templateValue) {
        if (session.getAttribute(KEY_TEMPLATE_SESSION) instanceof Map) {
            Map<String, Map<String, Object>> mapTemplate = (Map<String, Map<String, Object>>)session.getAttribute(
                KEY_TEMPLATE_SESSION);
            if ((templateValue != null) && (!mapTemplate.containsKey(key))) {
                mapTemplate.put(key, templateValue);
            }
        }
    }

    /**
     * Get Template data from session
     *
     * @param session HttpSession object
     * @return Session data
     */
    @SuppressWarnings("unchecked")
    private Map<String, Map<String, Object>> getTemplateFromSession(HttpSession session) {
        if (session.getAttribute(KEY_TEMPLATE_SESSION) instanceof Map) {
            return (Map<String, Map<String, Object>>)session.getAttribute(KEY_TEMPLATE_SESSION);
        }
        return null;
    }

    /**
     * Close and save data to database when click button save
     *
     * @param eventType Event type when call action
     * @param session HttpSession object
     * @param workItemId WorkItem ID
     * @param workSpaceId Workspace ID
     * @throws EarthException Throw this exception when has any error
     */
    private void closeAndSave(String eventType, HttpSession session, String workItemId, String workSpaceId)
        throws EarthException {
        // Close and save data, remove template data on session
        if (EventType.isButtonClick(eventType)) {
            workItemService.closeAndSaveWorkItem(session, workItemId, workSpaceId);
            removeTemplateSession(session);
        }
    }

    /**
     * Set error response when error occur
     *
     * @param response HttpServletResponse object
     * @return Error page
     */
    private String setUpdateError(HttpServletResponse response, Model model) {
        response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("Update", "Update Process fail"));
        model.addAttribute(Constant.Session.MESSAGES, messages);
        return ERROR_MESSAGE_PAGE;
    }
}
