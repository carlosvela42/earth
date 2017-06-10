<#assign contentFooter>
<@component.removePanel></@component.removePanel>
</#assign>


<#assign script>
<script src="${rc.getContextPath()}/resources/js/schedule.js"></script>
</#assign>

<@standard.standardPage title=e.get('schedule.title') contentFooter=contentFooter displayWorkspace=true script=script>

<div class="board-wrapper board-full">
  <#include "../common/messages.ftl">
  <form method="get" id="filter" action="">
        <input type="hidden"  id="workspaceId" name="workspaceId" value="${workspaceId}">
    </form>
    <table class="clientSearch table_list">
        <thead>
        <tr class="table_header">
            <td class=""><input type="checkbox" class="deleteAllCheckBox" /></td>
            <td class="text_center">
                <a id="addButton" class="icon icon_add" href="${rc.getContextPath()}/schedule/addNew">
                </a>
            </td>
            <td>${e.get('schedule.id')}</td>
            <td>${e.get('process.id')}</td>
            <td>${e.get('process')}</td>
            <td>${e.get('task.name')}</td>
            <td>${e.get('startDateTime')}</td>
            <td>${e.get('endDateTime')}</td>
        </tr>
        <tr class="condition">
            <td><img src="${rc.getContextPath()}/resources/images/search.png"/></td>
            <td colspan="2"><input id="tests" type="text" col="3" placeholder="search"/></td>
            <td><input id="tests2" type="text" col="4" placeholder="search"/></td>
            <td><input type="text" col="5" placeholder="search"/></td>
            <td><input type="text" col="6" placeholder="search"/></td>
            <td><input type="text" col="7" placeholder="search"/></td>
            <td><input type="text" col="8" placeholder="search"/></td>
        </tr>
        </thead>
        <tbody id="scheduleTbody" class="table_body">
          <#if mgrSchedules??>
            <#list mgrSchedules as mgrSchedule>
            <tr scheduleId="${mgrSchedule.scheduleId}">
                <td><input type="checkbox" class="deleteCheckBox" /></td>
                <td class="text_center">
                    <a class="icon icon_edit" href="${rc.getContextPath()}/schedule/showDetail?scheduleId=${mgrSchedule.scheduleId}">

                    </a>
                </td>
                <td class="text">${mgrSchedule.scheduleId!""}</td>
                <td class="text">${mgrSchedule.processId!""}</td>
                <td class="text">${mgrSchedule.processName!""}</td>
                <td class="text">${mgrSchedule.taskName!""}</td>
                <td class="text">${mgrSchedule.startTime!""}</td>
                <td class="text">${mgrSchedule.endTime!""}</td>
            </tr>
            </#list>
          </#if>
        </tbody>
    </table>
</div>
</@standard.standardPage>