<#assign addFrom>
    <@component.workItemSearch></@component.workItemSearch>
</#assign>
<#assign addPopupFrom>
    <@component.searchPopup></@component.searchPopup>
</#assign>

<#assign addSearchPopupFrom>
    <@component.searchFormPopup></@component.searchFormPopup>
</#assign>

<#assign contentFooter>
    <@component.unLockPanel></@component.unLockPanel>
</#assign>

<#assign addSearchFrom>
    <@component.searchColumnsForm></@component.searchColumnsForm>
</#assign>

<#assign searchFormField>
    <@component.searchColumnFormPanel object="" formId=""></@component.searchColumnFormPanel>
</#assign>

<#assign script>
<script src="${rc.getContextPath()}/resources/js/workItemList.js"></script>
</#assign>

<@standard.standardPage title=e.get('workItem.titleList') contentFooter=contentFooter displayWorkspace=true
script=script imageLink="tool">

<div class="board-wrapper board-full">
    <#include "../common/messages.ftl">
    <form method="get" id="filter" action="">
        <input type="hidden" id="workspaceId" name="workspaceId" value="${workspaceId}">
    </form>
<#--div searchForm-->
    <div class="modal fade" id="addFormSearch" role="dialog">
        <div class="modal-dialog content_main">
            <div class="modal-content board-wrapper board-full">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">X</button>
                    <span class="modal-title title_popup">
                    ${e.get('workItem.detailSearch')}
                    </span>
                </div>

                <div class="modal-body">

                    <table style="width: 100%;" id="searchTable">
                        <tr>
                            <td class="text" style="width: 20%;vertical-align: middle;">${e.get('template.type')}</td>
                            <td class="text" style="width: 25%;vertical-align: middle;"><select id="templateType">
                                <option selected>-- Please choose --</option>
                                <#list templateTypes as templateType>
                                    <#assign selected=searchByColumnsForm???then( searchByColumnsForm.templateType???then((templateType==searchByColumnsForm.templateType)?then("selected",""),""),"")>
                                    <option value="${templateType}" ${selected!""}>${templateType}</option>
                                </#list>
                            </select></td>
                            <td style="width: 5%;"></td>
                            <td class="text" style="width: 25%;vertical-align: middle;">${e.get('template.name')}</td>
                            <#if searchForms??>
                                <td class="text" style="width: 25%;vertical-align: middle;" id="selectTemplateName">
                                    <select id="templateId">

                                    <option selected="selected">-- Please choose --</option>
                                    <#list searchForms as searchForm>
                                        <#assign templateIdSelected=searchByColumnsForm???then( searchByColumnsForm.templateId???then((searchForm.templateId==searchByColumnsForm.templateId)?then("selected",""),""),"")>

                                        <option
                                            value="${searchForm.templateId}"${templateIdSelected!""}>${searchForm.templateName}
                                        </option>
                                    </#list>
                                </select></td>
                            </#if>

                        </tr>
                        <tr>
                            <td colspan="5">
                            ${searchFormField}
                            </td>
                        </tr>
                    </table>


                    <br>
                    <form method="post" id="searchForm">
                        <div id="searchListDiv">
                            <table class="search">
                                <#if searchForm??> <#if searchForm.columns?exists>
                                    <tr>
                                        <#list searchForm.columns as column>
                                            <th><b>${column.name?upper_case}</b></th>
                                        </#list>
                                    </tr>
                                    <#if searchForm.rows?exists> <#list searchForm.rows as row>
                                        <tr>
                                            <#list row.columns as column>
                                                <td>${column.value}</td>
                                            </#list>
                                        </tr>
                                    </#list> </#if> </#if> </#if>
                            </table>
                        </div>
                    </form>
                    <form method="post" id="deleteListForm" object="deleteListForm">
                        <input type="hidden" id="workspaceId" name="workspaceId" value="${workspaceId}">
                        <input type="hidden" id="workItemId" name="listIds[0]" value="">
                    </form>
                </div>
                <div class="modal-footer modal-footer-popup">
                    <button type="button" class="btn btn-default btn_cancel_popup btn_popup" id="btnCancel">
                    ${e.get('button.cancel')}
                    </button>
                    <button type="button" class="btn btn-default btn_save_popup btn_popup" id="btnReflect">
                    ${e.get('button.reflect')}
                    </button>
                </div>
            </div>

        </div>
    </div>

    <br>

<#--Div search by column-->
    ${addSearchPopupFrom}
    <br>
    <button class="btn searchBtn icon_search" id="btnSearchForm" data-target="#addFormSearch">
        <img src="${rc.getContextPath()}/resources/images/search.png">
    ${e.get('button.search')}
    </button>
    <br>
    <div class="clearfix" style="height: 20px;"></div>
    <form method="post" id="workItemForm" object="workItems" action="${rc.getContextPath()}/workItem/">
        <table class="clientSearch table_list" id="directorysTable">
            <thead>
            <tr class="table_header">
                <td rowspan="2"><input type="checkbox" class="deleteAllCheckBox"/></td>
                <td colspan="2" rowspan="2">${e.get('workItem.id')}</td>
                <td rowspan="2">${e.get('workItem.lockStatus')}</td>
                <td colspan="3">${e.get('workItem')}</td>
            </tr>
            <tr class="table_header">
                <td>${e.get('task.name')}</td>
                <td>${e.get('template.id')}</td>
                <td>${e.get('template.name')}</td>
            </tr>
            </thead>
            <tbody id="workItemTBody" class="table_body">
                <#if workItems??>
                    <#list workItems as workItem>
                    <tr eventId="${workItem.eventId!""}">
                        <td><input type="checkbox" class="deleteCheckBox"/></td>
                        <td class="text_center">
                            <a class="icon icon_edit" workItem="${workItem.workitemId!""}"
                               href="${rc.getContextPath()}/workItem/showDetail?workItemId=${workItem.workitemId!""}">
                            </a>
                        </td>
                        <td class="number">${workItem.workitemId!""}</td>
                        <td class="number">${workItem.statusLock???then("1","0")}</td>
                        <td class="text">${workItem.taskName!""}</td>
                        <td class="text">${workItem.templateId!""}</td>
                        <td class="text">${workItem.templateName!""}</td>
                    </tr>
                    </#list>
                </#if>
            </tbody>
        </table>
        <br>
    </form>

${addPopupFrom}
${addSearchFrom}
${addFrom}
</div>

</@standard.standardPage>
