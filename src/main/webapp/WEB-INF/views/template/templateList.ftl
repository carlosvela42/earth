<#assign contentFooter>
    <@component.removePanel></@component.removePanel>
</#assign>

<#assign script>
<script src="${rc.getContextPath()}/resources/js/template.js"></script>
</#assign>

<@standard.standardPage title=e.get('schedule.title') contentFooter=contentFooter displayWorkspace=true script=script>
<div class="board-wrapper board-full">
    <#include "../common/messages.ftl">
    <form method="get" id="filter" action="">
        <input type="hidden"  id="templateType" name="templateType" value="${templateType!""}">
        <input type="hidden"  id="workspaceId" name="workspaceId" value="${workspaceId}">
    </form>
    <table class="clientSearch table_list">
        <thead>
            <tr class="table_header">
                <td class=""><input type="checkbox" class="deleteAllCheckBox" /></td>
                <td class="text_center">
                    <a id="addButton" class="icon icon_add" href="${rc.getContextPath()}/template/addNew">
                    </a>
                </td>
                <td>${e.get('template.id')}</td>
                <td>${e.get('template.name')}</td>
                <td>${e.get('template.tableName')}</td>
            </tr>
            <tr class="condition">
                <td><img src="${rc.getContextPath()}/resources/images/search.png"/></td>
                <td colspan="2"><input id="tests" type="text" col="3" placeholder="search"/></td>
                <td><input id="tests2" type="text" col="4" placeholder="search"/></td>
                <td><input type="text" col="5" placeholder="search"/></td>
            </tr>
        </thead>
        <tbody id="templateTbody" class="table_body">
            <#if mgrTemplates??>
                <#list mgrTemplates as mgrTemplate>
                <tr templateId="${mgrTemplate.templateId}">
                    <td><input type="checkbox" class="deleteCheckBox" /></td>
                    <td class="text_center">
                        <a class="icon icon_edit" href="${rc.getContextPath()}/template/showDetail?templateIds=${mgrTemplate.templateId}">
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
</div>
</form>
</@standard.standardPage>
