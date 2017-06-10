<#assign searchForm>
    <@component.searchFormPanel object="evidentLog" formId="evidentForm"></@component.searchFormPanel>
</#assign>

<@standard.standardPage title=e.get('evidenceLog.log') displayWorkspace=true script=script>

<div class="board-wrapper board-full">
    <#include "../common/messages.ftl">
    <form method="get" id="filter" action="">
        <input type="hidden" name="workspaceId" value="${workspaceId}">
    </form>

    ${searchForm}

    <table class="clientSearch table_list">
        <thead>
        <tr class="table_header" style="white-space: nowrap;">
            <td>${e.get('evidenceLog.eventId')}</td>
            <td>${e.get('evidenceLog.processTime')}</td>
            <td>${e.get('user.id')}</td>
            <td>${e.get('evidenceLog.workitemId')}</td>
            <td>${e.get('evidenceLog.historyNo')}</td>
            <td>${e.get('evidenceLog.processId')}</td>
            <td>${e.get('evidenceLog.processVersion')}</td>
            <td>${e.get('evidenceLog.taskId')}</td>
        </tr>
        </thead>
        <tbody id="evidentTbody" class="table_body">
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
            </#if>
        </tbody>
    </table>
</div>
</@standard.standardPage>