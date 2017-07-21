$(function() {
	earth.onDeleteButtonClick(function() {
		var processIds = [];
		$('#processTbody > tr').each(function() {
			if ($(this).find('.deleteCheckBox').prop('checked')) {
				processIds.push($(this).attr('processId'));
			}
		});
		var searchColumns0 = $("#searchColumns\\[0\\]").val();
        var searchColumns1 = $("#searchColumns\\[1\\]").val();
        var searchColumns2 = $("#searchColumns\\[2\\]").val();
        var searchColumns3 = $("#searchColumns\\[3\\]").val();
        var searchColumns4 = $("#searchColumns\\[4\\]").val();
		if (processIds.length > 0) {
			$.form(window.baseUrl + "/process/deleteList", {
				"processIds" : processIds,
				"searchColumns[0]": searchColumns0,
            	"searchColumns[1]": searchColumns1,
            	"searchColumns[2]": searchColumns2,
            	"searchColumns[3]": searchColumns3,
            	"searchColumns[4]": searchColumns4,
				workspaceId : $('#workspaceId').val()
			}).submit();
		} else {
			earth.addMessage("E1014");
			return;
		}
	},earth.Messages['process']);
	
	$('#addButton').click(function (e) { 
        var $form = $("#siteSearchForm");
        $form.attr("action", window.baseUrl + "/process/addNew");
        //${rc.getContextPath()}/process/addNew
        $form.submit(); 
       return  e.preventDefault ? e.preventDefault() : e.returnValue = false;
    });

    $('.editButton').click(function () { 
    	var id = $(this).data('id');
    	var $form = $("#siteSearchForm");
    	$form.attr("action", window.baseUrl + "/process/showDetail?processId=" + id);
    	//${rc.getContextPath()}/process/showDetail?processId=${process.processId}
        $form.submit();
        return  e.preventDefault ? e.preventDefault() : e.returnValue = false;
    });
});

// Add new process start
var processDefinition;
window.onload = function() {
	$('.documentDataSavePath').click(function() {
		if ($(this).val() == "database") {
			$('#fileArea').hide();
			$('#databaseArea').show();
		} else if ($(this).val() == "file") {
			$('#fileArea').show();
			$('#databaseArea').hide();
		}
	});

	$('#fileUpload').change(function() {
		var oFReader = new FileReader();
		oFReader.readAsText($(this).get(0).files[0]);
		oFReader.onload = function(oFREvent) {
			console.log(this.result);
			processDefinition = this.result;
		}
	});
	$('#fileDownload').click(function() {
		document.location.href = baseUrl + "downloadFile";
	});
};
function getFileExtension(filename) {
	return filename.substr(filename.lastIndexOf('.') + 1)
};
// Add new process end
