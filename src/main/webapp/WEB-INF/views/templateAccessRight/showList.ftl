<#assign script>
  <script src="${rc.getContextPath()}/resources/js/templateAuth.js"></script>
</#assign>
<@standard.standardPage title=e.get('template.title') contentFooter=contentFooter displayWorkspace=true imageLink="authority" script=script>
<div class="board-wrapper board-full">
    <#include "../common/messages.ftl">
    <form method="get" id="filter" action="">
        <input type="hidden" name="workspaceId" value="${workspaceId}">
    </form>
    <form action="${rc.getContextPath()}/templateAccessRight/" id="siteSearchForm" object="searchForm" method="post">
    <table class="clientSearch table_list">
        <thead>
        <tr class="table_header">
            <td colspan="2">${e.get('template.id')}</td>
            <td style="width: 25%">${e.get('template.type')}</td>
            <td style="width: 30%">${e.get('template.name')}</td>
            <td style="width: 30%">${e.get('template.tableName')}</td>
        </tr>
        <tr class="condition">
            <td class="text_center search_icon" style="width: 1%">
                <img src="${rc.getContextPath()}/resources/images/search.png"/>
            </td>
            <td><input id="searchColumns[0]" name="searchColumns[0]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[0]!"",""),"")}"
                    type="text" col="2" placeholder="search"/></td>
            <td><input id="searchColumns[1]" name="searchColumns[1]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[1]!"",""),"")}"
                    type="text" col="3" placeholder="search"/></td>
            <td><input id="searchColumns[2]" name="searchColumns[2]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[2]!"",""),"")}"
                    type="text" col="4" placeholder="search"/></td>
            <td  style="border-left: transparent; border-right-style: solid; border-right-color: #B0AFB0;" ><input id="searchColumns[3]" name="searchColumns[3]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[3]!"",""),"")}"
                    type="text" col="5" placeholder="search"/></td>
        </tr>
        </thead>
        <tbody class="table_body">
            <#if mgrTemplates?? && workspaceId??> <#list mgrTemplates
            as mgrTemplate>
            <tr processId="${mgrTemplate.templateId}">
                <td class="text_center" style="width: 1%">
                    <a class="icon icon_edit editButton" href="#" data-id="${mgrTemplate.templateId}" data-wid="${workspaceId}"></a>
                </td>
                <td class="text">${mgrTemplate.templateId}</td>
                <td class="text">${mgrTemplate.templateType!""}</td>
                <td class="text">${mgrTemplate.templateName!""}</td>
                <td class="text">${mgrTemplate.templateTableName}</td>
            </tr>
            </#list> </#if>
        </tbody>
    </table>
    </form>
</div>
</@standard.standardPage>
