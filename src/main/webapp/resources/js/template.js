function getIdIncrement() {
    var id = -1;
    $(".template-row").each(function () {
        var currentId = parseInt($(this).data('row-id'));
        if (currentId > id) {
            id = currentId;
        }
    });
    return id + 1;
}

function proxyDelRow(name) {
    delRow(name);
    var index = $('table.' + name + 'AccessRightTable tbody > tr').size();
    $('.' + name + 'AccessRightTable tbody').attr('index', index);
}


$(function () {
    $("#addField").click(function () {
        $('#addFieldModal').find("table .table_body").html(earth.buildHtml("#template-edit-row-template"));
        openModal();
    });

    $('#addRow').click(function () {
        var name = $('#name').val();
        var description = $('#description').val();
        var typeDisplay = $('#fieldType option:selected').text();
        var typeValue = $('#fieldType option:selected').val();
        var size = $('#size').val();
        var required = $('#required').is(':checked');
        var encrypted = $('#encrypted').is(':checked');
        var fieldName = earth.Messages['field.name'];
        var fieldDescription = earth.Messages['field.description'];
        var fieldSize = earth.Messages['field.size'];

        earth.removeModalMessage(["E0001"]);
        if (name == "") {
            earth.addModalMessage("E0001", fieldName)
            return;
        }

        if(isSameName()) {
            earth.addModalMessage("E0005", fieldName)
            return;
        }
        
        if (description == "") {
            earth.addModalMessage("E0001", fieldDescription)
            return;
        }
        if (typeDisplay == 'NVARCHAR2' || typeDisplay == 'NCHAR') {
            if (size == "") {
                earth.addModalMessage("E0001", fieldSize);
                return;
            }
        }


        var fieldId = $("#fieldId").val();
        var isAddNew = false;
        if (fieldId === '') {
            fieldId = getIdIncrement();
            isAddNew = true;
        }
        var row = earth.buildHtml("#template-row-template", {
            i: fieldId, name: name, description: description,
            typeDisplay: typeDisplay, typeValue: typeValue, size: size,
            size: size, required: required, encrypted: encrypted
        });
        if (isAddNew) {
            $('#fieldList tbody').append(row);
        } else {
            $("#fieldList tr[data-row-id='" + fieldId + "']").replaceWith(row);
        }

        $('#name').val("");
        $('#description').val("");
        $('#type').val("");
        $('#size').val("");
        $('#required').prop("checked",
            false);
        $('#encrypted').prop("checked",
            false);
        $("#message").text("");

        // close popup
        closeModal();
    });

    window.editTemplateRow = function (i) {
        var context = {}
        $(".template-row[data-row-id='" + i + "']").find("[data-name]").each(function () {
            context[$(this).data("name")] = $(this).attr("value");
        });
        context["id"] = i;

        $('#addFieldModal').find("table .table_body").html(earth.buildHtml("#template-edit-row-template", context));
        openModal();
    }

    function openModal() {
        earth.removeModalMessage(["E0001","E0005"]);
        disableFieldsOnEdit();
        sizeChange();
        $('#addFieldModal').modal('show');
    }

    function closeModal() {
        $('#addFieldModal').modal('hide');
    }

    $('#clearRow').click(function () {
        $('#name').val("");
        $('#description').val("");
        $('#type').val("");
        $('#size').val("");
        $('#required').prop("checked", false);
        $('#encrypted').prop("checked", false);
    })

    $("#removeField").click(function (e) {
        e.preventDefault();
        $("#fieldList").find(".template-row [name=DeleteRow]").each(function () {
            if ($(this).is(":checked")) {
                $(this).parents("tr").remove();
            }
        })
    });

    function isSameName() {
    	var currentId = $(".addTemplate #fieldId")[0].value;
    	var currentValue = $(".addTemplate #name")[0].value
    	var listOfField = $("#userTbody input:hidden[name*='.name']");
    	
    	// there is only one field, no need
    	if(listOfField.length == 0 ) {
    		return false;
    	} 
    		
    	// have two or more
    	for(var index = 0; index < listOfField.length; index++) {
    		fieldName = listOfField[index];
    	

    		var name = fieldName.name; 
    		var value = fieldName.value; 
    		var matches = name.match(/\[(.*?)\]/);
    		var id = "-1";
    		if (matches) {
                id = matches[1];
    		}
    		
			if(currentId != id){
				if(value == currentValue) {
					return true;
				}
			}
    	}
    	return false;
    }
    
    function sizeChange() {
        var fieldType = $('#fieldType option:selected').text();
        var isDisabled = (fieldType == 'NUMBER' || fieldType == 'INTEGER');
        $('#size').prop("disabled", isDisabled || isPersisted());
    }

    function isPersisted() {
        return ($("#isPersisted")[0].value == 'Y');
    }
    function disableFieldsOnEdit() {
        var isDisable = isPersisted();
        $('#name').prop("disabled", isDisable);
        $('#fieldType').prop("disabled", isDisable);
        $('#size').prop("disabled", isDisable);
        $('#required').prop("disabled", isDisable);
        $('#encrypted').prop("disabled", isDisable);

    }

    $("#addFieldModal").on('change','#fieldType',sizeChange);

    earth.onDeleteButtonClick(function () {
        var templateIds = [];
        $('#templateTbody > tr').each(function () {
            if ($(this).find('.deleteCheckBox').prop('checked')) {
                templateIds.push($(this).attr('templateId'));
            }
        });
        //console.log(templateIds);
        //console.log($('#workspaceSelection').val());
        var searchColumns0 = $("#searchColumns\\[0\\]").val();
        var searchColumns1 = $("#searchColumns\\[1\\]").val();
        var searchColumns2 = $("#searchColumns\\[2\\]").val();
        if (templateIds.length > 0) {
            $.form(window.baseUrl + "/template/deleteList", {
                "listIds": templateIds,
                "searchColumns[0]": searchColumns0,
            	"searchColumns[1]": searchColumns1,
            	"searchColumns[2]": searchColumns2,
                workspaceId: $('#workspaceId').val()
            }).submit();
        } else {
            earth.addMessage("E1014");
            return;
        }
    },earth.Messages['template']);

//  Template Authority

    // User
    $('#addFormuser').on('shown.bs.modal', function () {
        $('#userSelect').focus();
    })
    
    function openModalUser() {
        $('#addFormuser').modal('show');
    };

    function closeModalUser() {
        $('#addFormuser').modal('hide');
    };

    $("#addUser").click(function () {
        $("#userTbody > tr").each(function(){
            var name=$(this).attr("userId");
            $('#userSelect option[value="'+name+'"]').remove();
        });
        $('.accessRights').each(function () {
            $('input[type=radio]:first', this).prop('checked', true);
        });
        $("#textuser").hide();
        $('#userSelect').show();
        $('#userEditItem').val(0);
        openModalUser();
    });

    // Profile
    $("#addProfile").click(function () {
        $("#profileTbody > tr").each(function(){
            var name=$(this).attr("profileId");
            $('#profileSelect option[value="'+name+'"]').remove();
        });
        $('.accessRights').each(function () {
            $('input[type=radio]:first', this).prop('checked', true);
        });
        $("#textprofile").hide();
        $('#userSelect').show();
        $('#profileEditItem').val(0);
        openModalProfile();
    });
    
    $('#addFormprofile').on('shown.bs.modal', function () {
        $('#profileSelect').focus();
    })
    function openModalProfile() {
        $('#addFormprofile').modal('show');
    };

    function closeModalProfile() {
        $('#addFormprofile').modal('hide');
    };

    window.editRowNew = function (name) {
        var source = $("#addRow").html();
        var template = Handlebars.compile(source);
        var value = $('#' + name + 'Select option:selected').val();
        if ($('#' + name + 'EditItem').val() == "1") {
            value = $("#text" + name).html();
        }

        var indx = $('.' + name + 'AccessRightTable tbody').attr('index');
        var screenType = "1";
        var accessRight = $('input[name=accessRight' + name + ']:checked').attr('data-name');
        var html = template({id: value, name: name, index: indx, accessRight: accessRight, screenType: screenType});

        if ($('#' + name + 'EditItem').val() == "0") {
            if ($('table.' + name + 'AccessRightTable tbody > tr[' + name + 'Id=' + value + ']').length <= 0) {

                $('table.' + name + 'AccessRightTable tbody').append(html);
                var index = parseInt(indx) + 1;
                //console.log(index);
                //console.log("html : " + html);
                //console.log($('.' + name + 'AccessRightTable'));
                $('.' + name + 'AccessRightTable tbody').attr('index', index);
            }
        } else {
            indx = $('table.' + name + 'AccessRightTable tbody >  tr[' + name + 'Id=' + value + ']').attr("index");
            html = template({id: value, name: name, index: indx, accessRight: accessRight, screenType: screenType});
            $('table.' + name + 'AccessRightTable tbody >  tr[' + name + 'Id=' + value + ']').replaceWith(html);
        }

        $('#' + name + 'Select option').prop('selected', false);
        $('input[type=radio]').prop('checked', false);
        $('#addForm' + name).modal('hide');
    }
    
    $('#addButton').click(function (e) { 
        var $form = $("#siteSearchForm");
        $form.attr("action", window.baseUrl + "/template/addNew");
        //${rc.getContextPath()}/template/addNew
        $form.submit(); 
       return  e.preventDefault ? e.preventDefault() : e.returnValue = false;
    });

    $('.editButton').click(function () { 
    	var id = $(this).data('id');
    	var $form = $("#siteSearchForm");
    	$form.attr("action", window.baseUrl + "/template/showDetail?templateIds=" + id);
    	//${rc.getContextPath()}/template/showDetail?templateIds=${mgrTemplate.templateId}
        $form.submit();
        return  e.preventDefault ? e.preventDefault() : e.returnValue = false;
    });
    
    
});