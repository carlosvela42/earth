$(function () {
	$('.editButton').click(function () { 
    	var id = $(this).data('id');
    	var wid = $(this).data('wid');
    	var $form = $("#siteSearchForm");
    	$form.attr("action", window.baseUrl + "/templateAccessRight/showDetail?templateId=" + id + "&workspaceId=" + wid);
    	//${rc.getContextPath()}/templateAccessRight/showDetail?templateId=${mgrTemplate.templateId}&workspaceId=${workspaceId}
        $form.submit();
        return  e.preventDefault ? e.preventDefault() : e.returnValue = false;
    });

})

