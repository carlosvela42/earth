$(function () {
	$('.editButton').click(function () { 
    	var id = $(this).data('id');
    	var $form = $("#siteSearchForm");
    	$form.attr("action", window.baseUrl + "/menuAccessRight/showDetail?functionId=" + id);
    	//${rc.getContextPath()}/menuAccessRight/showDetail?functionId=${mgrMenu.functionId}
        $form.submit();
        return  e.preventDefault ? e.preventDefault() : e.returnValue = false;
    });

})

