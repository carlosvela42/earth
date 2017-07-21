<#assign contentFooter>
<@component.removePanel></@component.removePanel>
</#assign>


<#assign script>
<script src="${rc.getContextPath()}/resources/js/schedule.js"></script>
</#assign>

<@standard.standardPage title=e.get('schedule.title') contentFooter=contentFooter displayWorkspace=true script=script imageLink="process">

<div class="board-wrapper board-full">
  <#include "../common/messages.ftl">
  <form method="get" id="filter" action="">
        <input type="hidden"  id="workspaceId" name="workspaceId" value="${workspaceId}">
    </form>
    <form action="${rc.getContextPath()}/schedule/" id="siteSearchForm" object="searchForm" method="post">
    <table class="clientSearch table_list">
        <thead>
        <tr class="table_header">
            <td class=""><input type="checkbox" class="deleteAllCheckBox" /></td>
            <td class="text_center">
                <a id="addButton" class="icon icon_add" href="#">
                </a>
            </td>
            <td>${e.get('schedule.id')}</td>
            <td>${e.get('process.id')}</td>
            <td>${e.get('process.name')}</td>
            <td>${e.get('task.name')}</td>
            <td>${e.get('schedule.startDateTime')}</td>
            <td>${e.get('schedule.endDateTime')}</td>
        </tr>
        <tr class="condition">
            <td class="search_icon"><img src="${rc.getContextPath()}/resources/images/search.png"/></td>
            <td colspan="2"><input id="searchColumns[0]" name="searchColumns[0]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[0]!"",""),"")}"
                  type="text" col="3" placeholder="search"/></td>
            <td><input id="searchColumns[1]" name="searchColumns[1]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[1]!"",""),"")}"
                  type="text" col="4" placeholder="search"/></td>
            <td><input id="searchColumns[2]" name="searchColumns[2]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[2]!"",""),"")}"
                  type="text" col="5" placeholder="search"/></td>
            <td><input id="searchColumns[3]" name="searchColumns[3]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[3]!"",""),"")}"
                  type="text" col="6" placeholder="search"/></td>
            <td><input id="searchColumns[4]" name="searchColumns[4]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[4]!"",""),"")}"
                  type="text" col="7" placeholder="search"/></td>
            <td  style="border-left: transparent; border-right-style: solid; border-right-color: #B0AFB0;" ><input id="searchColumns[5]" name="searchColumns[5]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[5]!"",""),"")}"
                  type="text" col="8" placeholder="search"/></td>
        </tr>
        </thead>
        <tbody id="scheduleTbody" class="table_body">
          <#if mgrSchedules??>
            <#list mgrSchedules as mgrSchedule>
            <tr scheduleId="${mgrSchedule.scheduleId}">
                <td><input type="checkbox" class="deleteCheckBox" /></td>
                <td class="text_center">
                    <a class="icon icon_edit editButton" href="#" data-id="${mgrSchedule.scheduleId}">

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
    </form>
</div>
</@standard.standardPage>