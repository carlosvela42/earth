$(function () {
    earth.onDeleteButtonClick(function () {
        var scheduleIds = [];
        $('#scheduleTbody > tr').each(function () {
            if ($(this).find('.deleteCheckBox').prop('checked')) {
                scheduleIds.push($(this).attr('scheduleId'));
            }
        });
        //console.log(scheduleIds);
        //console.log($('#workspaceSelection').val());

        var searchColumns0 = $("#searchColumns\\[0\\]").val();
        var searchColumns1 = $("#searchColumns\\[1\\]").val();
        var searchColumns2 = $("#searchColumns\\[2\\]").val();
        var searchColumns3 = $("#searchColumns\\[3\\]").val();
        var searchColumns4 = $("#searchColumns\\[4\\]").val();
        var searchColumns5 = $("#searchColumns\\[5\\]").val();
        
        if (scheduleIds.length > 0) {
            $.form(window.baseUrl + "/schedule/deleteList", {"listIds": scheduleIds,
            	"searchColumns[0]": searchColumns0,
            	"searchColumns[1]": searchColumns1,
            	"searchColumns[2]": searchColumns2,
            	"searchColumns[3]": searchColumns3,
            	"searchColumns[4]": searchColumns4,
            	"searchColumns[5]": searchColumns5,
                workspaceId: $('#workspaceId').val()}).submit();
        } else {
            earth.addMessage("E1014");
            return;
        }
    },earth.Messages['schedule']);

    $("#processIServiceName").change(function () {
        $("#processIServiceId").val($('#processIServiceName option:selected').val());
    });
    $("#processName").change(function () {
        $("#processId").val($('#processName option:selected').val());
    });
    $("#taskName").change(function () {
        $("#taskId").val($('#taskName option:selected').val());
    });
    
    $('#enable').click(function() {
		$("#enableDisable").val($('#enable').val());
		$('#disable').prop('checked',false);
		
	});
	$('#disable').click(function() {
		$("#enableDisable").val($('#disable').val());
		$('#enable').prop('checked',false);
	});
	
	$('#addButton').click(function (e) { 
        var $form = $("#siteSearchForm");
        $form.attr("action", window.baseUrl + "/schedule/addNew");
        //${rc.getContextPath()}/schedule/addNew
        $form.submit(); 
       return  e.preventDefault ? e.preventDefault() : e.returnValue = false;
    });

    $('.editButton').click(function () { 
    	var id = $(this).data('id');
    	var $form = $("#siteSearchForm");
    	$form.attr("action", window.baseUrl + "/schedule/showDetail?scheduleId=" + id);
    	//${rc.getContextPath()}/schedule/showDetail?scheduleId=${mgrSchedule.scheduleId}
        $form.submit();
        return  e.preventDefault ? e.preventDefault() : e.returnValue = false;
    });

});