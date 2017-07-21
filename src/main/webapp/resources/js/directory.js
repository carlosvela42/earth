
window.onload = function() {
	var baseUrl = "${rc.getContextPath()}";
}
$(function() {
	earth.onDeleteButtonClick(function() {
		var directoryIds = [];
		$('#directoryTbody > tr').each(function() {
			if ($(this).find('.deleteCheckBox').prop('checked')) {
				directoryIds.push($(this).attr('directoryId'));
			}
		});
		var searchColumns0 = $("#searchColumns\\[0\\]").val();
        var searchColumns1 = $("#searchColumns\\[1\\]").val();
        var searchColumns2 = $("#searchColumns\\[2\\]").val();
        var searchColumns3 = $("#searchColumns\\[3\\]").val();
        var searchColumns4 = $("#searchColumns\\[4\\]").val();
		if (directoryIds.length > 0) {
			$.form(window.baseUrl + "/directory/deleteList", {
				"searchColumns[0]": searchColumns0,
            	"searchColumns[1]": searchColumns1,
            	"searchColumns[2]": searchColumns2,
            	"searchColumns[3]": searchColumns3,
            	"searchColumns[4]": searchColumns4,
				"processIds" : directoryIds
			}).submit();
		} else {
			earth.addMessage("E1014");
			return;
		}
	},earth.Messages['directory']);
	$('#enable').click(function() {
		$("#newCreateFile").val($('#enable').val());
		$('#disable').prop('checked',false);
		
	});
	$('#disable').click(function() {
		$("#newCreateFile").val($('#disable').val());
		$('#enable').prop('checked',false);
	});

	function getSize() {
		var folderPath = $("#txtFolderPath").val();

		folderPath = folderPath.replace(/\\/g, "/");
		var urlSwitchWorkspace = baseUrl + "/directory/getSizeFolder"
				+ "?folderPath=" + folderPath;
		$.ajax({
			type : 'GET',
			url : urlSwitchWorkspace,
			dataType : "json",
			success : function(data) {
				$("#txtDiskVolSize").val(data);
			}
		});
	}

	$('#addButton').click(function (e) { 
        var $form = $("#siteSearchForm");
        $form.attr("action", window.baseUrl + "/directory/addNew");
        //${rc.getContextPath()}/directory/addNew
        $form.submit(); 
       return  e.preventDefault ? e.preventDefault() : e.returnValue = false;
    });

    $('.editButton').click(function () { 
    	var id = $(this).data('id');
    	var $form = $("#siteSearchForm");
    	$form.attr("action", window.baseUrl + "/directory/showDetail?dataDirectoryId=" + id);
    	//${rc.getContextPath()}/directory/showDetail?dataDirectoryId=${directory.dataDirectoryId}
        $form.submit();
        return  e.preventDefault ? e.preventDefault() : e.returnValue = false;
    });
    
	$("#directoryForm").on('keyup keypress', function(e) {
		var keyCode = e.keyCode || e.which;
		if (keyCode === 13) {
			getSize();
			e.preventDefault();
			return false;
		}
	});
});