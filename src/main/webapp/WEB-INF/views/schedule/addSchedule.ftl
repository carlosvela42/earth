<#assign contentFooter>
    <@component.detailUpdatePanel object="schedule" formId="scheduleForm"></@component.detailUpdatePanel>
</#assign>
<#assign script>
<script src="${rc.getContextPath()}/resources/js/schedule.js"></script>
</#assign>
<@standard.standardPage title=e.get("schedule.add") contentFooter=contentFooter script=script>

<br>
    <#assign isPersisted = (mgrSchedule.lastUpdateTime??)>
    <#assign formAction = isPersisted?then('updateOne', 'insertOne')>

<form id="scheduleForm" action="${rc.getContextPath()}/schedule/${formAction}" object="scheduleForm" method="post"
      class="">
    <#include "../common/messages.ftl">
    <div class="board-wrapper">
        <div class="board board-half">
            <table class="table_form">
                <tr>
                    <td width="50%">${e.get('schedule.id')}</td>
                    <td>${mgrSchedule.scheduleId!""}<input type="hidden" name="scheduleId"
                                                           value="${mgrSchedule.scheduleId!""}"/></td>
                </tr>
                <tr>
                    <td width="50%">${e.get('schedule.hostname')}</td>
                    <td><input type="text" name="hostName" value="${mgrSchedule.hostName!""}"/></td>
                </tr>
                <tr>
                    <td>${e.get('process.service')}</td>
                    <td>
                        <select id="processIServiceName" name="processIServiceName">
                            <option value="0" selected="selected"></option>
                            <#if mgrProcessServices??>
                                <#list mgrProcessServices as mgrProcessService>
                                    <#if mgrSchedule.processIServiceId??>
                                        <#assign selectedProcessService =(mgrSchedule.processIServiceId==(mgrProcessService.processIServiceId?string("")))?then("selected","")>
                                    </#if>
                                    <option value="${mgrProcessService.processIServiceId}"
                                    ${selectedProcessService!""}>${mgrProcessService
                                    .processIServiceName}</option>
                                </#list>
                            </#if>
                        </select>
                        <input type="hidden" id="processIServiceId" name="processIServiceId" height="20px" width="150px"
                               style="text-align: left" value="${mgrSchedule.processIServiceId!""}">
                    </td>
                </tr>
                <tr>
                    <td>${e.get('process.name')}</td>
                    <td>
                        <select id="processName" name="processName">
                            <option value="0" selected="selected"></option>
                            <#if mgrProcesses??>
                                <#list mgrProcesses as mgrProcess>
                                    <#if mgrSchedule.processId??>
                                        <#assign selectedProcess =(mgrSchedule.processId==(mgrProcess.processId))?then("selected","")>
                                    </#if>
                                    <option value="${mgrProcess.processId}" ${selectedProcess!""}>${mgrProcess.processName}</option>
                                </#list>
                            </#if>
                        </select>
                        <input type="hidden" id="processId" name="processId" height="20px" width="150px"
                               style="text-align: left" value="${mgrSchedule.processId!""}">
                    </td>
                </tr>
                <tr>
                    <td>${e.get('schedule.taskName')}</td>
                    <td>
                        <select id="taskName" name="taskName">
                            <option value="0" selected="selected"></option>
                            <#if mgrTasks??>
                                <#list mgrTasks as mgrTask>
                                    <#if mgrSchedule.taskId??>
                                        <#assign selectedTask =(mgrSchedule.taskId==(mgrTask.taskId))?then("selected","")>
                                    </#if>
                                    <option value="${mgrTask.taskId}" ${selectedTask!""}>${mgrTask.taskName}</option>
                                </#list>
                            </#if>
                        </select>
                        <input type="hidden" id="taskId" name="taskId" height="20px" width="150px"
                               style="text-align: left" value="${mgrSchedule.taskId!""}">
                    </td>
                </tr>
            </table>
        </div>
        <div class="board-split"></div>
        <div class="board board-half">
            <table class="table_form">
                <tr>
                    <td width="50%">${e.get('schedule.enable.disable')}</td>
                    <td>
                        <#if mgrSchedule.enableDisable??>
                            <#if mgrSchedule.enableDisable=="enable">
                                <#assign enable="checked">
                            </#if>

                            <#if mgrSchedule.enableDisable=="disable">
                                <#assign disable="checked">
                            </#if>
                        </#if>
                        <div class="row">
                            <div class="col-md-6">
                                <input type="radio" name="enable_Disable"
                                       value="enable" ${enable!""}>${e.get('schedule.enable')}
                            </div>
                            <div class="col-md-6">
                                <input type="radio" name="enable_Disable"
                                       value="disable" ${disable!""}>${e.get('schedule.disable')}
                            </div>
                        </div>
                        <input type="hidden" id="enableDisable" name="enableDisable" height="20px" width="150px"
                               style="text-align: left" value="${mgrSchedule.enableDisable!""}">
                    </td>
                </tr>
                <tr>
                    <td width="50%">${e.get('schedule.startDateTime')}</td>
                    <td><input type="text" name="startTime" value="${mgrSchedule.startTime!""}"/></td>
                </tr>
                <tr>
                    <td>${e.get('schedule.endDateTime')}</td>
                    <td><input type="text" name="endTime" value="${mgrSchedule.endTime!""}"/></td>
                </tr>
                <tr class="nextDateTimeSchedule">
                    <td>${e.get('schedule.executionInterval')}</td>
                    <td>
                        <input type="text" class="halfWidth" name="runIntervalDay"
                               value="${mgrSchedule.runIntervalDay!""}"/><label>${e.get('schedule.day')}</label>
                        <br>
                        <input type="text" class="halfWidth" name="runIntervalHour"
                               value="${mgrSchedule.runIntervalHour!""}"/><label>${e.get('schedule.hour')}</label>
                        <br>
                        <input type="text" class="halfWidth" name="runIntervalMinute"
                               value="${mgrSchedule.runIntervalMinute!""}"/><label>${e.get('schedule.minute')}</label>
                        <br>
                        <input type="text" class="halfWidth lastSecond" name="runIntervalSecond"
                               value="${mgrSchedule.runIntervalSecond!""}"/><label>${e.get('schedule.second')}</label>
                    </td>
                </tr>
            </table>
            <div><input type="hidden" name="lastUpdateTime" value="${mgrSchedule.lastUpdateTime!""}"/></div>
        </div>
    <#--<div class="clearfix"></div>-->
    </div>
    <div><input type="hidden" name="workspaceId" value="${workspaceId!""}"/></div>
</form>
</@standard.standardPage>