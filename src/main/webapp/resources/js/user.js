$(function () {
    earth.onDeleteButtonClick(function () {
        var userIds = [];
        $('#userTbody > tr').each(function () {
            if ($(this).find('.deleteCheckBox').prop('checked')) {
                userIds.push($(this).attr('userId'));
            }
        });
        console.log(userIds);
        if (userIds.length > 0) {
            $.form(window.baseUrl + "/user/deleteList", {"listIds": userIds}).submit();
        } else {
            earth.addMessage("E1014");
            return;
        }
    });
    $('.deleteAllCheckBox').click(function () {
        $('#userTbody > tr').each(function () {
            $(this).find('.deleteCheckBox').prop('checked',$('.deleteAllCheckBox').prop('checked'));
        });
    });
    // Edit User
    $("input[name=changePassword]").change(function () {
        $('input[type=password]').prop("disabled", !this.checked);
    });

});