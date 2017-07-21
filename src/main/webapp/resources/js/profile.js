$(function () {
    earth.onDeleteButtonClick(function () {
        var profileIds = [];
        $('#profileTbody > tr').each(function () {
            if ($(this).find('.deleteCheckBox').prop('checked')) {
                profileIds.push($(this).attr('profileId'));
            }
        });
        //console.log(profileIds);
        var searchColumns0 = $("#searchColumns\\[0\\]").val();
        var searchColumns1 = $("#searchColumns\\[1\\]").val();
        if (profileIds.length > 0) {
            $.form(window.baseUrl + "/profile/deleteList", {"searchColumns[0]": searchColumns0,
            	"searchColumns[1]": searchColumns1,"listIds": profileIds}).submit();
        } else {
            earth.addMessage("E1014");
            return;
        }
    },earth.Messages['profile']);
    
	$(".deleteCheckBox").click(function(){
	    checked();
	});

    $(".deleteAllCheckBox").click(function(){
        checked();
    });

    $('#addButton').click(function (e) { 
        var $form = $("#siteSearchForm");
        $form.attr("action", window.baseUrl + "/profile/addNew");
        //${rc.getContextPath()}/profile/addNew
        $form.submit(); 
       return  e.preventDefault ? e.preventDefault() : e.returnValue = false;
    });

    $('.editButton').click(function () { 
    	var id = $(this).data('id');
    	var $form = $("#siteSearchForm");
    	$form.attr("action", window.baseUrl + "/profile/showDetail?profileId=" + id);
    	//${rc.getContextPath()}/profile/showDetail?profileId=${mgrProfile.profileId}
        $form.submit();
        return  e.preventDefault ? e.preventDefault() : e.returnValue = false;
    });
});
function checked () {
    var userIds = "";
    $('#userTbody > tr').each(function () {
        if ($(this).find('.deleteCheckBox').prop('checked')) {
            console.log($(this).attr('userId').toString());
            userIds += $(this).attr('userId').toString() + ",";
        }
    });
    console.log(userIds);
    if (userIds.length > 0) {
        userIds = userIds.substring(0, userIds.length - 1);
    }
    $("#userIds").val(userIds);
    return;
};
