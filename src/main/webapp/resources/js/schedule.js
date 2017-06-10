$(function () {
    earth.onDeleteButtonClick(function () {
        var scheduleIds = [];
        $('#scheduleTbody > tr').each(function () {
            if ($(this).find('.deleteCheckBox').prop('checked')) {
                scheduleIds.push($(this).attr('scheduleId'));
            }
        });
        console.log(scheduleIds);
        console.log($('#workspaceSelection').val());

        if (scheduleIds.length > 0) {
            $.form(window.baseUrl + "/schedule/deleteList", {"listIds": scheduleIds,
                workspaceId: $('#workspaceId').val()}).submit();
        } else {
            earth.addMessage("E1014");
            return;
        }
    });

    $("#processIServiceName").change(function () {
        $("#processIServiceId").val($('#processIServiceName option:selected').val());
    });
    $("#processName").change(function () {
        $("#processId").val($('#processName option:selected').val());
    });
    $("#taskName").change(function () {
        $("#taskId").val($('#taskName option:selected').val());
    });
    $('input[name=enable_Disable]:radio').change(function () {
        $("#enableDisable").val($("input[name=enable_Disable]:checked").val());
    });

});