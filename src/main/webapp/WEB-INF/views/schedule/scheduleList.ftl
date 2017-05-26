<@standard.standardPage title=" LIST SCHEDULE">

<script>

    function filter() {
        // Declare variables
        var i;
        scheduleIdInput = document.getElementById('scheduleIdInput').value.toUpperCase();
        processIdInput = document.getElementById('processIdInput').value.toUpperCase();
        processNameInput = document.getElementById('processNameInput').value.toUpperCase();
        taskNameInput = document.getElementById('taskNameInput').value.toUpperCase();
        startTimeInput = document.getElementById('startTimeInput').value.toUpperCase();
        endTimeInput = document.getElementById('endTimeInput').value.toUpperCase();

        // Loop through all list items, and hide those who don't match the search query
        for (i = 0; i < $("#scheduleList tr ").length - 2; i++) {

            if (($("#scheduleId" + i).html().toUpperCase().indexOf(scheduleIdInput) > -1)
                    && ($("#processId" + i).html().toUpperCase().indexOf(processIdInput) > -1)
                    && ($("#processName" + i).html().toUpperCase().indexOf(processNameInput) > -1)
                    && ($("#taskName" + i).html().toUpperCase().indexOf(taskNameInput) > -1)
                    && ($("#startTime" + i).html().toUpperCase().indexOf(startTimeInput) > -1)
                    && ($("#endTime" + i).html().toUpperCase().indexOf(endTimeInput) > -1)) {
                $("#row" + i).show();
            } else {
                $("#row" + i).hide();
            }
        }
    }

    function validate() {
        var deleted = parseInt($("#deleted").val());
        var str = $("#scheduleIds").val();
        console.log("scheduleIds: " + str);

        if (deleted > 0 && str.length > 0) {
            document.forms[0].submit();
        } else {
            if (deleted == 0 && str.length == 0) {
                $("#message").html("choose mgrSchedule and Confirm");
            } else if (deleted == 0 && str.length > 0) {
                $("#message").html("choose Confirm");
            } else if (deleted > 0 && str.length == 0) {
                $("#message").html("choose mgrSchedule");
            }
        }
    }

    $(function () {
        $("#workspaces").on('change', function () {
            $("#workspaceId").val($('#workspaces option:selected').val());
            var workspaceId = $('#workspaces option:selected').text();
            if (workspaceId != '-- Please choose --') {
                $("#scheduleListForm").attr("action", "switchWorkspace");
                $("#scheduleListForm").submit();
            }
        });

        var countChecked = function () {
            var str = ""
            $('input[name=DeleteRow]:checked').each(function () {
                str += $(this).attr('value') + ",";
            });
            if (str.length > 0) {
                str = str.substring(0, str.length - 1);
            }
            $("#scheduleIds").val(str);
        };

        var Checked = function () {
            if ($("input[name=delAll]").prop('checked')) {
                $('input[name=DeleteRow]').prop('checked', true);
            }
            else {
                $('input[name=DeleteRow]').prop('checked', false);
            }
            ;
            countChecked();
        };

        var countDeleted = function () {
            var str = "0";
            $('input[name=delete]:checked').each(function () {
                str = "1";
            });
            $("#deleted").val(str);
        };
        $("input[name=DeleteRow]").on("click", countChecked);
        $("input[name=delete]").on("click", countDeleted);
        $("input[name=delAll]").on("click", Checked);
    })
</script>
<form action="${rc.getContextPath()}/schedule/deleteList" object="mgrSchedules" method="post" class="form-narrow
form-horizontal" id="scheduleListForm">
    <label>スケジュール一覧</label>
    <#if mgrWorkspaces??>
        <select onchange="this.form.submit()" id="workspaces" name="workspaceId">
            <option selected="selected">-- Please choose --</option>
            <#list mgrWorkspaces as mgrWorkspace>
                <#if workspaceId?? && mgrWorkspace.workspaceId==workspaceId>
                    <option value="${mgrWorkspace.workspaceId}" selected>${mgrWorkspace.workspaceName}</option>
                <#else >
                    <option value="${mgrWorkspace.workspaceId}">${mgrWorkspace.workspaceName}</option>
                </#if>
            </#list>
        </select>
    </#if>
    <div id="count"></div>
    <input type="hidden" id="workspaceId" name="workspaceId" value="${workspaceId!"0"}">
    <table>
        <tr style="height: 40px;">
            <td style="padding-left: 20px;padding-right: 20px">
                <input type="button" class="button" value="削除" onclick="validate()">
            </td>
            <td style="padding-left: 20px;padding-right: 20px">
                <input type="checkbox" name="delete" value="delete">Confirm Delete
            </td>
        </tr>
    </table>
    <div>
        <b id="message" style="color: red;"></b>
    </div>
    <input type="hidden" id="scheduleIds" name="scheduleIds" value="">
    <input type="hidden" id="deleted" name="deleted" value="0">
    <table border="1" id="scheduleList">
        <tr style="white-space: nowrap;text-align: center;">
            <th>
                <input type="checkbox" id="delAll" name="delAll" class="DeleteRow">
            </th>
            <th>
                <a href="${rc.getContextPath()}/schedule/addNew?workspaceId=${workspaceId!""}">
                    <img src="${rc.getContextPath()}/resources/images/add.png" width="20">
                </a>
            </th>
            <th>スケジュールID</th>
            <th>プロセスID</th>
            <th>プロセス</th>
            <th>タスク名</th>
            <th>開始日時</th>
            <th>終了日時</th>
        </tr>
        <tr style="text-align: left;">
            <td colspan="3">
                <input type="text" id="scheduleIdInput" onkeyup="filter()" placeholder="Search ...">
            </td>
            <td>
                <input type="text" id="processIdInput" onkeyup="filter()" placeholder="Search ...">
            </td>
            <td>
                <input type="text" id="processNameInput" onkeyup="filter()" placeholder="Search ...">
            </td>
            <td>
                <input type="text" id="taskNameInput" onkeyup="filter()" placeholder="Search ..." style="width: auto;">
            </td>
            <td>
                <input type="text" id="startTimeInput" onkeyup="filter()" placeholder="Search ...">
            </td>
            <td>
                <input type="text" id="endTimeInput" onkeyup="filter()" placeholder="Search ...">
            </td>
        </tr>

        <#if mgrSchedules??>
            <#list mgrSchedules as mgrSchedule>
                <tr id="row${mgrSchedule?index}">
                    <th>
                        <input type="checkbox" id="DeleteRow" name="DeleteRow" value="${mgrSchedule.scheduleId!""}"
                               class="DeleteRow">
                    </th>
                    <th>
                        <a href="${rc.getContextPath()}/schedule/showDetail?scheduleId=${mgrSchedule.scheduleId}&workspaceId=${workspaceId!""}">
                            <img src="${rc.getContextPath()}/resources/images/edit.png" width="20">
                        </a>
                    </th>
                    <td style="text-align: right;" id="scheduleId${mgrSchedule?index}">${mgrSchedule.scheduleId!""}</td>
                    <td style="text-align: left;" id="processId${mgrSchedule?index}">${mgrSchedule.processId!""}</td>
                    <td style="text-align: left;"
                        id="processName${mgrSchedule?index}">${mgrSchedule.processName!""}</td>
                    <td style="text-align: right;" id="taskName${mgrSchedule?index}">${mgrSchedule.taskName!""}</td>
                    <td style="text-align: right;" id="startTime${mgrSchedule?index}">${mgrSchedule.startTime!""}</td>
                    <td style="text-align: right;" id="endTime${mgrSchedule?index}">${mgrSchedule.endTime!""}</td>
                </tr>
            </#list>
        <#else>
            <tr>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
        </#if>
    </table>
</form>
</@standard.standardPage>