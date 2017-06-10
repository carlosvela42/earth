<#assign script>
<script src="${rc.getContextPath()}/resources/js/template.js"></script>
</#assign>

<#assign contentFooter>
    <@component.detailUpdatePanel object="template" formId="templateForm"></@component.detailUpdatePanel>
</#assign>

<@standard.standardPage title=e.get('template.edit') contentFooter=contentFooter script=script>
    <#assign isPersisted = (templateForm.lastUpdateTime??)>
    <#assign formAction = isPersisted?then('updateOne', 'insertOne')>
<div class="board-wrapper board-full">
    <form action="${rc.getContextPath()}/template/${formAction}"
          object="templateForm" method="post" id="templateForm">
        <#include "../common/messages.ftl">
        <input type="hidden" id="workspaceId" name="workspaceId" value="${templateForm.workspaceId!""}">
        <div class="row">
            <div class="col-md-6">
                <table class="table_form">
                    <tr>
                        <td width="50%">${e.get('template.id')}</td>
                        <td><input type="text" id="txtTemplate" name="templateId"
                                   value="${templateForm.templateId!""}" readonly="readonly"></td>
                    </tr>
                    <tr>
                        <td>${e.get('template.name')}</td>
                        <td><input type="text" id="txtTemplateName" name="templateName"
                                   value="${templateForm.templateName!""}" ></td>
                    </tr>
                    <tr>
                        <td>${e.get('template.tableName')}</td>
                        <td><input type="text" id="txtTemplateTableName"
                                   value="${templateForm.templateTableName!""}" name="templateTableName"></td>
                    </tr>
                </table>
            </div>
        </div>

        <div class="region">${e.get('template.definition')}</div>
        <div class="region">
            <button id="removeField" type="button" class="btn btn_remove" id="deleteButton"
                    onclick="return delRow('user');">
                <@spring.message code='button.delete'/></button>
        </div>

        <table id="fieldList" class="clientSearch table_list no_search">
            <thead>
            <tr class="table_header">
                <td class=""><input type="checkbox" class="deleteAllCheckBox" /></td>
                <td>
                    <button type="button" class="icon btn_add" id="addField" data-target="#addFieldModal"></button>
                </td>
                <td>${e.get('field.name')}</td>
                <td>${e.get('field.description')}</td>
                <td>${e.get('field.type')}</td>
                <td>${e.get('field.size')}</td>
                <td>${e.get('field.required')}</td>
                <td>${e.get('field.encrypted')}</td>
            </tr>
            </thead>
            <tbody id="userTbody" class="table_body">
                <#if templateForm?? >
                    <#if templateForm.templateFields??>
                    <#list templateForm.templateFields as field>
                        <tr  fieldId="${field.name}" data-row-id="${field?index}" class="template-row">
                            <td><input type='checkbox' id='${field?index}' name='DeleteRow' class="deleteCheckBox"></td>
                            <td class="text_center"><span class="icon icon_edit"  onclick='editTemplateRow(${field?index})
                                    '></span></td>
                            <td>${field.name!""}
                                <input  type='hidden' name='templateFields[${field?index}].name' data-name="name"
                                                         value='${field.name!""}'/>
                            </td>
                            <td>${field.description!""}
                                <input  type='hidden' name='templateFields[${field?index}].description'
                                                                data-name="description"
                                                                value='${field.description!""}'/>
                            </td>
                            <td>${h.getDisplayType(field.type, fieldTypes)!""}
                                <input type='hidden' name='templateFields[${field?index}].type'
                                                        data-name="type"
                                                        value='${field.type!""}'/>
                            </td>
                            <td> ${field.size!""}
                                <input  type='hidden' name='templateFields[${field?index}].size'
                                                          data-name="size"
                                                          value='${field.size?c!""}'/>
                            </td>
                            <td>
                                <input  type='checkbox' ${field.required?then("checked", "")}/>
                                <input  type='hidden' name='templateFields[${field?index}].required' data-name="required"
                                        value="${field.required?then('true', 'false')}">
                            </td>

                            <td>
                                <input  type='checkbox'  ${field.encrypted?then("checked", "")}/>
                                <input  type='hidden' name='templateFields[${field?index}].encrypted' data-name="encrypted"
                                        value="${field.encrypted?then('true', 'false')}">
                            </td>
                        </tr>
                    </#list>
                </#if>
                </#if>
            </tbody>
        </table>
    </form>

    <!-- Modal -->
    <div class="modal fade" id="addFieldModal" role="dialog">
        <div class="modal-dialog">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">${e.get("file.edit")}</h4>
                </div>
                <div class="modal-body">
                    <div id="modal-messages"></div>

                    <table class="table_list">
                        <thead class="table_header table_same_color">
                        <tr>
                            <td width="20%">${e.get('field.name')}</td>
                            <td width="20%">${e.get('field.description')}</td>
                            <td width="20%">${e.get('field.type')}</td>
                            <td width="20%">${e.get('field.size')}</td>
                            <td width="10%">${e.get('field.required')}</td>
                            <td width="10%">${e.get('field.encrypted')}</td>
                        </tr>
                        </thead>
                        <tbody class="table_body">

                        </tbody>
                    </table>
                    <div class="text-center">
                        <button type="button" class="btn btn-default btn_cancel" data-dismiss="modal">
                        ${e.get('button.cancel')}
                        </button>
                        <button align="center" colspan="3" class="btn btn-default btn_save" id="addRow">
                        ${e.get('button.save')}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script id="template-row-template" type="text/x-handlebars-template">
    <tr data-row-id="{{i}}" class="template-row">
        <td><input type='checkbox' id='{{i}}' name='DeleteRow'></td>
        <td><span class="icon icon_edit"  onclick='editTemplateRow({{i}})'></span></td>
        <td>{{name}} <input  type='hidden' name='templateFields[{{i}}].name' data-name="name" value='{{name}}'/></td>
        <td>{{description}} <input  type='hidden'name='templateFields[{{i}}].description' data-name="description"
                                    value='{{description}}'/></td>
        <td>{{typeDisplay}} <input type='hidden'name='templateFields[{{i}}].type' data-name="type"
                                   value='{{typeValue}}'/></td>
        <td> {{size}} <input  type='hidden'name='templateFields[{{i}}].size' data-name="size"
                              value='{{size}}'/></td>"

        <td><input  type='checkbox'  value='{{required}}' {{#if required}} checked {{/if}}/>
            <input  type='hidden' name='templateFields[{{i}}].required' data-name="required"
                    value={{#if required}} true
                    {{else}} false {{/if}}/>
        </td>

        <td><input  type='checkbox'  value='{{encrypted}}' {{#if encrypted}} checked {{/if}}/>
            <input  type='hidden' name='templateFields[{{i}}].encrypted' data-name="encrypted"
                    value={{#if encrypted}} true
                    {{else}} false {{/if}}/>
        </td>
</script>

<script id="template-edit-row-template" type="text/x-handlebars-template">
    <tr class="addTemplate" >
        <input type="hidden" id="fieldId" name="id" value="{{id}}">
        <td><input type="text" id="name" name="name" value="{{name}}"></td>
        <td><input type="text" id="description" name="description" value="{{description}}"></td>
        <td><select name="fieldType" id="fieldType">
            <#if fieldTypes??>
                <#list fieldTypes as fieldType>
                    <option value="${fieldType.value!""}" {{#ifCond  ${fieldType.value} type }} selected
                            {{/ifCond }}>${fieldType}</option>
                </#list>
            </#if>
        </select>
        </td>
        <td><input type="text" id="size" name="size" value="{{size}}"></td>
        <td class="text-center">
            <input type="checkbox" id="required" name="required" {{#ifCond required 'true' }} checked {{/ifCond}}>
        </td>
        <td class="text-center">
            <input type="checkbox" id="encrypted" name="encrypted"{{#ifCond encrypted 'true' }} checked {{/ifCond}} >
        </td>
    </tr>
</script>

</@standard.standardPage>
