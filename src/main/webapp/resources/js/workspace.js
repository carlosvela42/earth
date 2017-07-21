$(function () {
    earth.onDeleteButtonClick(function () {
        var workspaceIds = [];
        $('#scheduleTbody > tr').each(function () {
            if ($(this).find('.deleteCheckBox').prop('checked')) {
                workspaceIds.push($(this).attr('workspaceId'));
            }
        });
        //console.log(workspaceIds);

        var searchColumns0 = $("#searchColumns\\[0\\]").val();
        var searchColumns1 = $("#searchColumns\\[1\\]").val();
        if (workspaceIds.length > 0) {
            $.form(window.baseUrl + "/workspace/deleteList", {"listIds": workspaceIds,
            	"searchColumns[0]": searchColumns0,
            	"searchColumns[1]": searchColumns1
            }).submit();
        } else {
            earth.addMessage("E1014");
            return;
        }
    },earth.Messages['workspace']);
    // Edit User
    $("input[name=changePassword]").change(function () {
        $('input[type=password]').prop("disabled", !this.checked);
    });
    
    $('#addButton').click(function (e) { 
        var $form = $("#siteSearchForm");
        $form.attr("action", window.baseUrl + "/workspace/addNew");
        //${rc.getContextPath()}/workspace/addNew
        $form.submit(); 
       return  e.preventDefault ? e.preventDefault() : e.returnValue = false;
    });

    $('.editButton').click(function () { 
    	var id = $(this).data('id');
    	var $form = $("#siteSearchForm");
    	$form.attr("action", window.baseUrl + "/workspace/showDetail?workspaceId=" + id);
    	//${rc.getContextPath()}/workspace/showDetail?workspaceId=${mgrWorkspace.workspaceId}
        $form.submit();
        return  e.preventDefault ? e.preventDefault() : e.returnValue = false;
    });

});