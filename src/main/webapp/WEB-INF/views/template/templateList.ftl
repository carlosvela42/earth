<#assign contentFooter>
    <@component.removePanel></@component.removePanel>
</#assign>

<#assign script>
<script src="${rc.getContextPath()}/resources/js/template.js"></script>
</#assign>

<@standard.standardPage title=e.get('template.title') contentFooter=contentFooter displayWorkspace=true script=script imageLink="data">
<div class="board-wrapper board-full">
    <#include "../common/messages.ftl">
    <form method="get" id="filter" action="">
        <input type="hidden"  id="templateType" name="templateType" value="${templateType!""}">
        <input type="hidden"  id="workspaceId" name="workspaceId" value="${workspaceId}">
    </form>
    <form action="${rc.getContextPath()}/template/" id="siteSearchForm" object="searchForm" method="post">
    <table class="clientSearch table_list">
        <thead>
            <tr class="table_header">
                <td class="" width="40px"><input type="checkbox" class="deleteAllCheckBox" /></td>
                <td class="text_center" width="40px">
                    <a id="addButton" class="icon icon_add" href="#">
                    </a>
                </td>
                <td width="30%">${e.get('template.id')}</td>
                <td width="30%">${e.get('template.name')}</td>
                <td width="35%">${e.get('template.tableName')}</td>
            </tr>
            <tr class="condition">
                <td colspan="3"><input id="searchColumns[0]" name="searchColumns[0]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[0]!"",""),"")}"
                        type="text" col="3" placeholder="search" class="searchInput"/></td>
                <td><input id="searchColumns[1]" name="searchColumns[1]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[1]!"",""),"")}"
                        type="text" col="4" placeholder="search" class="searchInput"/></td>
                <td><input id="searchColumns[2]" name="searchColumns[2]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[2]!"",""),"")}"
                        type="text" col="5" placeholder="search" class="searchInput"/></td>
            </tr>
        </thead>
        <tbody id="templateTbody" class="table_body">
            <#if mgrTemplates??>
                <#list mgrTemplates as mgrTemplate>
                <tr templateId="${mgrTemplate.templateId}">
                    <td><input type="checkbox" class="deleteCheckBox" /></td>
                    <td class="text_center">
                        <a class="icon icon_edit editButton" href="#" data-id="${mgrTemplate.templateId}">
                        </a>
                    </td>
                    <td class="text">${mgrTemplate.templateId!""}</td>
                    <td class="text">${mgrTemplate.templateName!""}</td>
                    <td class="text">${mgrTemplate.templateTableName!""}</td>
                </tr>
                </#list>
            </#if>
        </tbody>
    </table>
    </form>
</div>
</form>
</@standard.standardPage>
