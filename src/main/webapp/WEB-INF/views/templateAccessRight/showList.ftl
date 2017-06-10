<@standard.standardPage title=e.get('template.title') contentFooter=contentFooter displayWorkspace=true script=script>
<div class="board-wrapper board-full">
    <#include "../common/messages.ftl">
    <form method="get" id="filter" action="">
        <input type="hidden" name="workspaceId" value="${workspaceId}">
    </form>
    <table class="clientSearch table_list">
        <thead>
        <tr class="table_header">
            <td colspan="2">${e.get('template.id')}</td>
            <td style="width: 25%">${e.get('template.type')}</td>
            <td style="width: 30%">${e.get('template.name')}</td>
            <td style="width: 30%">${e.get('template.tableName')}</td>
        </tr>
        <tr class="condition">
            <td class="text_center" style="width: 1%">
                <img src="${rc.getContextPath()}/resources/images/search.png"/>
            </td>
            <td><input type="text" col="2" placeholder="search"/></td>
            <td><input type="text" col="3" placeholder="search"/></td>
            <td><input type="text" col="4" placeholder="search"/></td>
            <td><input type="text" col="5" placeholder="search"/></td>
        </tr>
        </thead>
        <tbody class="table_body">
            <#if mgrTemplates?? && workspaceId??> <#list mgrTemplates
            as mgrTemplate>
            <tr processId="${mgrTemplate.templateId}">
                <td class="text_center" style="width: 1%">
                    <a class="icon icon_edit"
                                           href="${rc.getContextPath()}/templateAccessRight/showDetail?templateId=${mgrTemplate.templateId}&workspaceId=${workspaceId}"></a>
                </td>
                <td class="text">${mgrTemplate.templateId}</td>
                <td class="text">${mgrTemplate.templateType!""}</td>
                <td class="text">${mgrTemplate.templateName!""}</td>
                <td class="text">${mgrTemplate.templateTableName}</td>
            </tr>
            </#list> </#if>
        </tbody>
    </table>
</div>
</@standard.standardPage>
