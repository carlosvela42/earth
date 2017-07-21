<#assign contentFooter>
        <@component.updateWorkItemPanel object="workItem" formId="workItemForm"></@component.updateWorkItemPanel>
</#assign>
<#assign script>
<style>
.document-image {
}
.document-image svg {
    max-width:100%;
    max-height:100%;
    vertical-align: middle;
    border: 1px solid red;
}

.document-image image {
    width:100%;
    height:100%;
    max-width:100%;
    max-height:100%;
    vertical-align: middle;
}
</style>
<script src="${rc.getContextPath()}/resources/js/lib/tiff.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/resources/js/lib/pdf.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/svg/svg.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/svg/svg.draw.js"></script>
<script src="${rc.getContextPath()}/resources/js/workitem.js"></script>
<script src="${rc.getContextPath()}/resources/js/imageViewer/model.js"></script>
<script src="${rc.getContextPath()}/resources/js/imageViewer/main.js"></script>
<script src="${rc.getContextPath()}/resources/js/imageViewer/thumbernail.js"></script>

<script> window.baseUrl = "${rc.getContextPath()}" </script>
<script>
    function loadThumbernail(folderItemNo, documentNo, documentType, pageCount) {
        var params = 'workspaceId=' + $("#workspaceId").val()
                     + '&workItemId=' + $("#workItemId").val()
                     + '&folderItemNo=' + folderItemNo
                     + '&documentNo=' + documentNo
                     + '&sessionId=${sessionId}'
        var url='${rc.getContextPath()}/WS/getThumbnail?' + params;
        var elementId = 'document_' + folderItemNo + "_" + documentNo;
        init(elementId, params, documentType, pageCount);
    }
</script>


<link rel="stylesheet" media="screen" href="${rc.getContextPath()}/resources/css/workitem/style.css">
</#assign>
<@standard.standardPage title="EDITWORKITEM" script=script contentFooter=contentFooter imageLink="tool">
<div id="errorMessage"></div>
<div class="board-wrapper">
    <div class="board_template board-half" id="div_treeview">
    <#-- START: Binding work item data -->
        <#if workItem??>
            <input type="hidden" id="workspaceId" value='${workItem.workspaceId???then(workItem.workspaceId,'')}' />
            <input type="hidden" id="workItemId" value='${workItem.workitemId???then(workItem.workitemId,'')}' />
            <div id="treeview5" class="treeview">
                <ul class="list-group">
                    <#if workItem.dataProcess??>
                        <#if workItem.dataProcess.accessRight??>
                            <#assign accessRight="${workItem.dataProcess.accessRight.name()}">
                        </#if>
                        <#if workItem.dataProcess.processId??>
                            <#assign processId="${workItem.dataProcess.processId}">
                        </#if>
                        <li class="list-group-item node-treeview5 node-selected PROCESS${processId!''} ${(accessRight=="SO")?string('disabled','')}"
                            processId="${processId!''}"
                            type="PROCESS" onload="showTemplate(this)"
                            <#if accessRight == "RO" || accessRight == "RW" || accessRight == "FULL">
                                onclick="showTemplate(this)"
                            </#if>
                            accessRight="${accessRight???then(accessRight, 'NONE')}"
                        >
                            <span class="icon node-icon process"></span>
                            <#if workItem.dataProcess.mgrTemplate??>
                            ${workItem.dataProcess.mgrTemplate.templateName}
                            </#if>
                        </li>
                    </#if>
                    <#if workItem.accessRight??>
                        <#assign accessRight="${workItem.accessRight.name()}">
                    </#if>
                    <li class="list-group-item node-treeview5 WORKITEM${workItem.workitemId}"
                        type="WORKITEM"
                        <#if accessRight == "RO" || accessRight == "RW" || accessRight == "FULL">
                            onclick="showTemplate(this)"
                        </#if>
                        accessRight="${accessRight???then(accessRight, 'NONE')}"
                    >
                        <span class="indent"></span>
                        <span class="icon node-icon workItem"></span>
                        <#if workItem.mgrTemplate??>
                            ${workItem.mgrTemplate.templateName}
                        </#if>
                    </li>
                    <#-- START: Binding folder data -->
                    <#if workItem.folderItems??>
                        <#list workItem.folderItems as folderItem>
                            <#assign accessRight="NONE">
                            <#if folderItem.accessRight??>
                                <#assign accessRight="${folderItem.accessRight.name()}">
                            </#if>
                            <li folderItemNo="${folderItem.folderItemNo!''}"
                                class="${(accessRight=="SO")?string('disabled','')} list-group-item node-treeview5 FOLDERITEM${folderItem.folderItemNo!''}"
                                type="FOLDERITEM"
                                <#if accessRight == "RO" || accessRight == "RW" || accessRight == "FULL">
                                    onclick="showTemplate(this)"
                                </#if>
                                accessRight="${accessRight}"
                            >
                                <span class="indent"></span><span class="indent"></span>
                                <span class="icon folder"></span>
                                <#if folderItem.mgrTemplate??>
                                    ${folderItem.mgrTemplate.templateName???then(folderItem.mgrTemplate.templateName,"")}
                                </#if>
                            </li>
                            <#--<#if folderItem.documents??>
                                <#list folderItem.documents as document>
                                    <#assign
                                    folderItemNo = document.folderItemNo???then(document.folderItemNo,'')
                                    documentNo = document.documentNo???then(document.documentNo,'')
                                    >
                                    <li class="list-group-item node-treeview5 DOCUMENT${folderItemNo!''}${documentNo???then('_' + documentNo, '')}"
                                        documentNo="${documentNo!''}"
                                        type="DOCUMENT"
                                        onclick="showTemplate(this)"
                                        <#if document.accessRight??>
                                        accessRight="${document.accessRight.name()}"
                                        </#if>
                                    >
                                        <span class="indent"></span><span class="indent"></span><span
                                            class="indent"></span>
                                        <span class="icon node-icon glyphicon glyphicon-book"></span>
                                        <#if document.mgrTemplate??>
                                        ${document.mgrTemplate.templateName???then(document.mgrTemplate.templateName,"")}
                                        </#if>
                                    </li>
                                    <#if document.layers??>
                                        <#list document.layers as layer>
                                            <#assign
                                                folderItemNo = layer.folderItemNo???then(layer.folderItemNo, '')
                                                documentNo = layer.documentNo???then(layer.documentNo, '')
                                                layerNo = layer.layerNo???then(layer.layerNo, '')
                                            >
                                            <li class="list-group-item node-treeview5 LAYER${folderItemNo!''}${documentNo???then('_' + documentNo, '')}${layerNo???then('_' + layerNo, '')}"
                                                folderItemNo="${folderItemNo!''}"
                                                documentNo="${documentNo!''}"
                                                layerNo="${layerNo!''}"
                                                ownerId="${layer.ownerId???then(layer.ownerId,'')}"
                                                type="LAYER"
                                                onclick="showTemplate(this)">
                                                <span class="indent"></span><span class="indent"></span>
                                                <span class="indent"></span><span class="indent"></span>
                                                <span class="icon node-icon glyphicon glyphicon-leaf"></span>
                                                <#if layer.mgrTemplate??>
                                                ${layer.mgrTemplate.templateName}
                                                </#if>
                                            </li>
                                        </#list>
                                    </#if>
                                </#list>
                            </#if>-->
                        </#list>
                    </#if>
                <#-- END: Binding folder data -->
                </ul>
            </div>
            <div id="document_area">
                <#if workItem.folderItems??>
                    <#list workItem.folderItems as folderItem>
                        <fieldset id="folder${folderItem???then(folderItem.folderItemNo???then(folderItem.folderItemNo,''),'')}"
                             class="document-image">
                            <#if folderItem.documents??>
                                <#list folderItem.documents as document>
                                    <#if document.accessRight??>
                                        <#assign accessRight="${document.accessRight.name()}">
                                    </#if>
                                    <#if accessRight == "RO" || accessRight == "RW" || accessRight == "FULL">
                                    <div id="document_${folderItem.folderItemNo???then(folderItem.folderItemNo,'')
                                    }_${document.documentNo???then(document.documentNo,'')}" style="cursor: pointer;">
                                        <script>
                                            loadThumbernail('${folderItem.folderItemNo???then(folderItem.folderItemNo,'')}'
                                            , '${document.documentNo???then(document.documentNo,'')}'
                                            , '${document.documentType???then(document.documentType,'')}'
                                            , '${document.pageCount???then(document.pageCount,'0')}');
                                        </script>
                                    </div>
                                    <#else>
                                        <img src="${rc.getContextPath()}/resources/images/workitem/thumberNailNone.png">
                                    </#if>
                                    <#assign
                                        folderItemNo = document.folderItemNo???then(document.folderItemNo,'')
                                        documentNo = document.documentNo???then(document.documentNo,'')
                                    >
                                    <#if document.mgrTemplate??>
                                        <#if document.mgrTemplate.templateName??>
                                        <label
                                              class="document-link DOCUMENT${folderItemNo!''}${documentNo???then('_' +
                                              documentNo,'')} ${(accessRight=="SO")?string('disabled','')}"
                                              documentNo="${documentNo!''}"
                                              folderItemNo="${folderItemNo!''}"
                                              parentNode="FOLDERITEM${folderItem.folderItemNo!''}"
                                              type="DOCUMENT"
                                              <#if accessRight == "RO" || accessRight == "RW" || accessRight == "FULL">
                                                onclick="showTemplate(this)"
                                              </#if>
                                              accessRight="${accessRight???then(accessRight, 'NONE')}"
                                            >
                                            <#if accessRight == "RO" || accessRight == "RW" || accessRight == "FULL">
                                                <a href="#">${document.mgrTemplate.templateName}</a>
                                            <#else>
                                                ${document.mgrTemplate.templateName}
                                            </#if>
                                        </label>
                                        </#if>
                                    </#if>
                                </#list>
                            </#if>
                        </fieldset>
                    </#list>
                </#if>
            </div>
        </#if>
    <#-- /END: Binding work item data -->
    </div>
    <div class="board-split"></div>
    <div class="board_template board-half"  style="position: relative;">
        <fieldset id="template_area_fieldSet">
        <div  id="template_area">
        </div>
        </fieldset>
        <div >
        <#if tasks??>
            <div class="row" id="task_list" style="
                                            position: absolute;
                                            bottom: 0;
                                            width: 100%;
                                            padding-right: 15px ;">
                <div class="col-md-4">
                    <label for="taskList" style="line-height: 0px;">${e.get('task.name')}</label>
                </div>
                <div class="col-md-8"  style="padding: 0;">
                    <div class="form-group">
                        <select id="taskList" name="taskList" class="form-control">
                            <#list tasks as task>
                                <#if task.customTaskId??>
                                    <#assign selectedtask = (task.customTaskId==(currentTaskId???then(currentTaskId,"")))?then
                                    ("selected","")>
                                    <option value="${task.customTaskId}" ${selectedtask}>
                                    ${task.customTaskName}
                                    </option>
                                </#if>
                            </#list>
                        </select>
                    </div>
                </div>
            </div>
        </#if>
        </div>
    </div>

</div>
<#--Modal-->
<div class="modal fade" id="closeEditWorkItemModal" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" onclick="return cancel('profile');">
                    X
                </button>
                <span class="modal-title title_popup">
                ${e.get('workItem')}
                </span>
            </div>
            <div class="modal-body">
                ${e.get('I0001')}
            </div>
            <div class="modal-footer modal-footer-popup">
                <button type="submit" class="btn btn-default btn_cancel_popup btn_popup" id="btnModalCancelWorkItem"
                        data-dismiss="modal">
                    ${e.get('button.cancel')}
                </button>
                <button align="center" colspan="3" class="btn btn-default btn_save_popup btn_popup"
                        id="btnModalSaveWorkItem"
                        onclick="saveTemplateFromModal()">
                ${e.get('button.save')}
                </button>
            </div>
        </div>
    </div>
</div>
</@standard.standardPage>