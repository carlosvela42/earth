<#assign searchForm>
    <@component.searchColumnFormPanel object="" formId=""></@component.searchColumnFormPanel>
</#assign>

<#assign addSearchPopupFrom>
    <@component.searchFormPopup></@component.searchFormPopup>
</#assign>

<#assign addFrom>
    <@component.workItemSearch></@component.workItemSearch>
</#assign>
<#assign addPopupFrom>
    <@component.searchPopup></@component.searchPopup>
</#assign>
<#assign addSearchFrom>
    <@component.searchColumnsForm></@component.searchColumnsForm>
</#assign>

<#assign script>
<script src="${rc.getContextPath()}/resources/js/evidentLog.js"></script>
</#assign>
<@standard.standardPage title=e.get('evidenceLog.log') displayWorkspace=true script=script imageLink="report">
<script id="evidenceLogRow" type="text/x-handlebars-template">
    {{#each strLogAccesses}}
    <tr>
        <td class="number">{{this.eventId}}</td>
        <td class="text">{{this.processTime}}</td>
        <td class="text">{{this.userId}}</td>
        <td class="number">{{this.workitemId}}</td>
        <td class="number">{{this.historyNo}}</td>
        <td class="number">{{this.processId}}</td>
        <td class="number">{{this.processVersion}}</td>
        <td class="number">{{this.taskId}}</td>
    </tr>
    {{/each}}
</script>
<div class="board-wrapper board-full">
    <#include "../common/messages.ftl">
    <form method="get" id="filter" action="">
        <input type="hidden" name="workspaceId" value="${workspaceId}">
    </form>

    ${addSearchPopupFrom}
    ${searchForm}
    <div class="clearfix" style="height: 15px;"></div>
    <table class="clientSearch table_list">
        <thead>
        <tr class="table_header rowSearch" style="white-space: nowrap;">
            <td>
                <button type="button"
                        onclick="openSearchCondition(this, 0,'NCHAR','eventId','${e.get('evidenceLog.eventId')}','false');"
                        class="icon btn_filter"
                        data-target="#addFormSearchColumn"></button>
                ${e.get('evidenceLog.eventId')}
            </td>
            <td>
                 <button type="button"
                         onclick="openSearchCondition(this, 1,'NCHAR','processTime','${e.get('evidenceLog.eventId')}','false');"
                        class="icon btn_filter"
                        data-target="#addFormSearchColumn"></button>
                ${e.get('evidenceLog.eventId')}
            </td>
            <td>
                 <button type="button" onclick="openSearchCondition(this, 2,'NCHAR','userId','${e.get('user.id')}','false');"
                        class="icon btn_filter"
                        data-target="#addFormSearchColumn"></button>
                ${e.get('user.id')}
            </td>
            <td>
                 <button type="button"
                         onclick="openSearchCondition(this, 3,'NCHAR','workitemId','${e.get('evidenceLog.workitemId')}','false');"
                        class="icon btn_filter"
                        data-target="#addFormSearchColumn"></button>
                ${e.get('evidenceLog.workitemId')}
            </td>
            <td>
                 <button type="button"
                         onclick="openSearchCondition(this, 4,'NCHAR','historyNo','${e.get('evidenceLog.historyNo')}','false');"
                        class="icon btn_filter"
                        data-target="#addFormSearchColumn"></button>
                ${e.get('evidenceLog.historyNo')}
            </td>
            <td>
                 <button type="button"
                         onclick="openSearchCondition(this, 5,'NCHAR','processId','${e.get('evidenceLog.processId')}','false');"
                        class="icon btn_filter"
                        data-target="#addFormSearchColumn"></button>
                ${e.get('evidenceLog.processId')}
            </td>
            <td>
                 <button type="button"
                         onclick="openSearchCondition(this, 6,'NCHAR','processVersion','${e.get('evidenceLog.processVersion')}','false');"
                        class="icon btn_filter"
                        data-target="#addFormSearchColumn"></button>
                ${e.get('evidenceLog.processVersion')}
            </td>
            <td>
                 <button type="button"
                         onclick="openSearchCondition(this, 7,'NCHAR','taskId','${e.get('evidenceLog.taskId')}','false');"
                        class="icon btn_filter"
                        data-target="#addFormSearchColumn"></button>
                ${e.get('evidenceLog.taskId')}
            </td>
        </tr>
        </thead>
        <tbody id="evidentTBody" class="table_body">
            <#if strLogAccesses??>
                <#list strLogAccesses as strLogAccess>
                <tr>
                    <td class="number">${strLogAccess.eventId!""}</td>
                    <td class="text">${strLogAccess.processTime!""}</td>
                    <td class="text">${strLogAccess.userId!""}</td>
                    <td class="number">${strLogAccess.workitemId!""}</td>
                    <td class="number">${strLogAccess.historyNo!""}</td>
                    <td class="number">${strLogAccess.processId!""}</td>
                    <td class="number">${strLogAccess.processVersion!""}</td>
                    <td class="number">${strLogAccess.taskId!""}</td>
                </tr>
                </#list>
            <#else>
                <#include "../common/noResult.ftl">
            </#if>
        </tbody>
    </table>
</div>
${addPopupFrom}
${addSearchFrom}
${addFrom}
</@standard.standardPage>