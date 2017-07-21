<#if type??>
<input type="hidden" id="type" value='${type}'/>
</#if>
<#if workspaceId??>
<input type="hidden" id="workspaceId" value='${workspaceId}'/>
</#if>
<#if processId??>
<input type="hidden" id="processId" value='${processId}'/>
</#if>
<#if workItemId??>
<input type="hidden" id="workItemId" value='${workItemId}'/>
    <#if processId??>
        <input type="hidden" id="currentNode" value="${type}${processId!''}"/>
    <#elseif layerNo?? && documentNo?? && folderItemNo??>
        <input type="hidden" id="currentNode" value="${type}${folderItemNo}${'_' + documentNo}${'_' + layerNo}"/>
    <#elseif documentNo?? && folderItemNo??>
        <input type="hidden" id="currentNode" value="${type}${folderItemNo}${'_' + documentNo}"/>
    <#elseif folderItemNo??>
        <input type="hidden" id="currentNode" value="${type}${folderItemNo}"/>
    <#else>
        <input type="hidden" id="currentNode" value="${type}${workItemId}"/>
    </#if>
</#if>
<#if folderItemNo??>
<input type="hidden" id="folderItemNo" value='${folderItemNo}'/>
</#if>
<#if documentNo??>
<input type="hidden" id="documentNo" value='${documentNo}'/>
</#if>
<#if layerNo??>
<input type="hidden" id="layerNo" value='${layerNo}'/>
</#if>
<#if ownerId??>
<input type="hidden" id="ownerId" value='${ownerId}'/>
</#if>
<#if currentTemplateId??>
<input type="hidden" id="currentTemplateId" value='${currentTemplateId}'/>
</#if>
<input type="hidden" id="accessRight" value=''/>
<#if templateList??>
<div class="row" style="margin:10px">
    <div class="col-md-4">
        <label for="templateList" style="line-height: 0px;">${e.get('template.default')}</label>
    </div>
    <div class="col-md-8" style="padding: 0;">
        <div class="form-group">
            <select id="templateList" name="templateList" class="form-control" onchange="setTemplateField()">
                <option value="">  </option>
                <#list templateList as template>
                    <#if template.templateId??>
                    <#-- START: Binding work item data -->
                        <option value="${template.templateId}"
                            <#if template.templateField??>
                                templateField='${template.templateField}'
                            </#if>
                            <#if template.templateTableName??>
                                templateTableName='${template.templateTableName}'
                            </#if>
                            <#if template.templateType??>
                                templateType='${template.templateType}'
                            </#if>
                        >
                        ${template.templateName}
                        </option>
                    </#if>
                </#list>
            </select>
        </div>
    </div>
</div>
</#if>

<div class="row" style="margin:10px" id="div_template">
<#if fields??>
    <table class="clientSearch table_list">
        <thead>
        <tr class="table_header">
            <td style="min-width: 100px">${e.get('field.name')}</td>
            <td style="min-width: 100px">${e.get('field.description')}</td>
            <td>${e.get('field.settingValue')}</td>
        </tr>
        </thead>
        <tbody id="processTbody">
            <#list fields as field>
            <#assign
                isRequired = field.required???then(field.required?string("required",""),"")
                isEncrypted = field.encrypted???then(field.encrypted?string("encrypted",""),"")
            >
            <tr class="${isRequired!''}">
                <td>
                    <#if field.name??>
                        <label class="control-label">
                        ${field.name}
                        </label>
                    </#if>
                    <input type="hidden" name="fieldName" value="${field.name???then(field.name,'')}">
                </td>
                <td>
                ${field.description???then(field.description,'')}
                </td>
                <td style="padding: 0;">
                    <input type="text"
                           name="fieldValue"
                           value="${mapTemplate???then(mapTemplate[field.name]???then(mapTemplate[field.name],''),'')}"
                            <#if field.size??>
                               size="${field.size}" maxlength="${field.size}"
                            </#if>
                           class="fieldValue form-control ${isRequired!''} ${isEncrypted!''} ${field.name???then(field.name,'')}"
                           style="border-radius: 0px;background-color: transparent;"/>
                </td>
            </tr>
            </#list>
        </tbody>
    </table>
</#if>
</div>