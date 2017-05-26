<@standard.standardPage title="EDIT SCHEDULE">
<script>
    $(function () {
        $("#processIServiceName").change(function () {
            $("#processIServiceId").val($('#processIServiceName option:selected').val());
        });
        $("#processName").change(function () {
            $("#processId").val($('#processName option:selected').val());
        });
        $("#taskName").change(function () {
            $("#taskId").val($('#taskName option:selected').val());
        });
        $('input[name=enable_disable]:radio').change(function () {
            $("#enableDisable").val($("input[name=enable_disable]:checked").val());
        });
    })
</script>

<form action="${rc.getContextPath()}/schedule/updateOne" object="addScheduleForm" method="post" class="form-narrow
form-horizontal">

    <input type="hidden" id="workspaceId" name="workspaceId" value="${workspaceId!""}">
    <table style="text-align: left;">
        <tr style="height: 40px; text-align: center">
            <td><input type="submit" value="決定" class="button"></td>
            <td><a href="${rc.getContextPath()}/schedule/showList" class="button">キャンセル</a></td>
        </tr>
        <tr>
            <td colspan="2">
                <#if messages??>
                    <#list messages as message>
                        <div>
                            <b style="color: red;">${message.getContent()}</b>
                        </div>
                    </#list>
                </#if>
            </td>
        </tr>

        <tr>
            <td>
                <label>スケジュールID</label>
            </td>
            <td>
                <input type="text" id="txtScheduleID" name="scheduleId" height="20px" width="150px"
                       style="text-align: left" value="${mgrSchedule.scheduleId!""}" readonly>
            </td>
        </tr>
    <#--HOSTNAME-->
        <tr>
            <td>
                <label>ホスト名</label>
            </td>
            <td>
                <input type="text" id="txtHostName" name="hostName" height="20px" width="150px"
                       style="text-align: left" value="${mgrSchedule.hostName!""}">
            </td>
        </tr>
    <#--PROCESSISERVICEID-->
        <tr>
            <td>
                <label>プロセスサービスID</label>
            </td>
            <td>
                <select id="processIServiceName" name="processIServiceName" style="width: 195px;">
                    <option value="0" selected="selected"></option>
                    <#if mgrProcessServices??>
                        <#list mgrProcessServices as mgrProcessService>
                            <#if mgrSchedule.processIServiceId??>
                                <#assign selectedProcessService =(mgrSchedule.processIServiceId==(mgrProcessService
                                .processIServiceId?string("")))?then("selected","")>
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
    <#--PROCESS-->
        <tr>
            <td>
                <label>プロセス名</label>
            </td>
            <td>
                <select id="processName" name="processName" style="width: 195px;">
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
    <#--TASK-->
        <tr>
            <td>
                <label>タスク名</label>
            </td>
            <td>
                <select id="taskName" name="taskName" style="width: 195px;">
                    <option value="0" selected="selected"></option>
                    <#if mgrTasks??>
                        <#list mgrTasks as mgrTask>
                            <#if mgrSchedule.taskId??>
                                <#assign selectedTask =(mgrSchedule.taskId==(mgrTask.taskId))?then
                                ("selected","")>
                            </#if>
                            <option value="${mgrTask.taskId}" ${selectedTask!""}>${mgrTask.taskName}</option>
                        </#list>
                    </#if>
                </select>
                <input type="hidden" id="taskId" name="taskId" height="20px" width="150px"
                       style="text-align: left" value="${mgrSchedule.taskId!""}">
            </td>
        </tr>
        <tr>
            <td>
                <label>有効/無効</label>
            </td>
            <td>
                <#if mgrSchedule.enableDisable??>
                    <#if mgrSchedule.enableDisable=="enable">
                        <#assign enable="checked">
                    </#if>

                    <#if mgrSchedule.enableDisable=="disable">
                        <#assign disable="checked">
                    </#if>
                </#if>
                <input type="radio" name="enable_disable" value="enable" ${enable!""}>有効
                <input type="radio" name="enable_disable" value="disable" ${disable!""}>無効
                <input type="hidden" id="enableDisable" name="enableDisable" height="20px" width="150px"
                       style="text-align: left" value="${mgrSchedule.enableDisable!""}">
            </td>
        </tr>
        <tr>
            <td>
                <label>開始日時</label>
            </td>
            <td><input type="text" id="txtStartTime" name="startTime" height="20px" width="150px"
                       style="text-align: left" value="${mgrSchedule.startTime!""}"></td>
        </tr>
        <tr>
            <td>
                <label>終了日時</label>
            </td>
            <td><input type="text" id="txtEndTime" name="endTime" height="20px" width="150px"
                       style="text-align: left" value="${mgrSchedule.endTime!""}"></td>
        </tr>
        <tr>
            <td rowspan="4">
                <label>次回実行日時</label>
            </td>
            <td><input type="text" id="txtRunIntervalDay" name="runIntervalDay" height="20px" width="150px"
                       style="text-align: left" value="${mgrSchedule.runIntervalDay!""}"></td>
        </tr>
        <tr>
            <td><input type="text" id="txtRunIntervalHour" name="runIntervalHour" height="20px" width="150px"
                       style="text-align: left" value="${mgrSchedule.runIntervalHour!""}"></td>
        </tr>
        <tr>
            <td><input type="text" id="txtRunIntervalMinute" name="runIntervalMinute" height="20px" width="150px"
                       style="text-align: left" value="${mgrSchedule.runIntervalMinute!""}"></td>
        </tr>
        <tr>
            <td><input type="text" id="txtRunIntervalSecond" name="runIntervalSecond" height="20px" width="150px"
                       style="text-align: left" value="${mgrSchedule.runIntervalSecond!""}"></td>
        </tr>
    </table>
</form>

</@standard.standardPage>