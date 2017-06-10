$(function () {
    earth.onDeleteButtonClick(function () {
        var workspaceIds = [];
        $('#scheduleTbody > tr').each(function () {
            if ($(this).find('.deleteCheckBox').prop('checked')) {
                workspaceIds.push($(this).attr('workspaceId'));
            }
        });
        console.log(workspaceIds);
        if (workspaceIds.length > 0) {
            $.form(window.baseUrl + "/workspace/deleteList", {"listIds": workspaceIds}).submit();
        } else {
            earth.addMessage("E1014");
            return;
        }
    });
    // Edit User
    $("input[name=changePassword]").change(function () {
        $('input[type=password]').prop("disabled", !this.checked);
    });

});