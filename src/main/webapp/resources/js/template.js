function getIdIncrement() {
  var id = -1;
  $(".template-row").each(function () {
    var currentId = parseInt($(this).data('row-id'));
    if(currentId > id){
      id = currentId;
    }
  });
  return id + 1;
}

$(function() {
  $("#addField").click(function () {
    $('#addFieldModal').find("table .table_body").html(earth.buildHtml("#template-edit-row-template"));
    openModal();
  });

  $('#addRow').click(function() {
            var name = $('#name').val();
            var description = $('#description').val();
            var typeDisplay = $('#fieldType option:selected').text();
            var typeValue = $('#fieldType option:selected').val();
            var size = $('#size').val();
            var required = $('#required').is(':checked');
            var encrypted = $('#encrypted').is(':checked');

            if(name == "" || description == ""){
              earth.addModalMessage("E0001", "Name or Description")
              return;
            }
            if(typeDisplay == 'NVARCHAR2' || typeDisplay == 'NCHAR'){
              if(size == ""){
                earth.addModalMessage("E0001", "Size");
                return;
              }
            }

            var fieldId = $("#fieldId").val();
            var isAddNew = false;
            if(fieldId === '') {
              fieldId = getIdIncrement();
              isAddNew = true;
            }
            var row = earth.buildHtml("#template-row-template", {i: fieldId, name: name, description: description,
              typeDisplay: typeDisplay, typeValue: typeValue, size: size,
              size: size, required: required, encrypted: encrypted});
            if(isAddNew) {
              $('#fieldList tbody').append(row);
            } else {
              $("#fieldList tr[data-row-id='"+ fieldId + "']").replaceWith(row);
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

  window.editTemplateRow = function(i) {
    var context = {}
    $(".template-row[data-row-id='"+ i +"']").find("[data-name]").each(function () {
      context[$(this).data("name")] = $(this).attr("value");
    });
    context["id"] = i;

    $('#addFieldModal').find("table .table_body").html(earth.buildHtml("#template-edit-row-template", context));
    openModal();
  }

  function openModal() {
    $('#addFieldModal').modal('show');
  }

  function closeModal() {
    $('#addFieldModal').modal('hide');
  }

  $('#clearRow').click(function() {
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
      if($(this).is(":checked")) {
        $(this).parents("tr").remove();
      }
    })
  });



  $(function() {
    $("#fieldType").on(
        'change',
        function() {
          var fieldType = $(
              '#fieldType option:selected')
              .text();
          if (fieldType == 'NUMBER'
              || fieldType == 'LONG') {
            $('#size').prop("disabled", true);
          } else {
            $('#size').prop("disabled", false);
          }
        });
  })
    earth.onDeleteButtonClick(function () {
        var templateIds = [];
        $('#templateTbody > tr').each(function () {
            if ($(this).find('.deleteCheckBox').prop('checked')) {
                templateIds.push($(this).attr('templateId'));
            }
        });
        console.log(templateIds);
        console.log($('#workspaceSelection').val());

        if (templateIds.length > 0) {
            $.form(window.baseUrl + "/template/deleteList", {"listIds": templateIds,
                workspaceId: $('#workspaceId').val()}).submit();
        } else {
            earth.addMessage("E1014");
            return;
        }
    });

//  Template Authority

    // User
    function openModalUser() {
        $('#addFormuser').modal('show');
    };

    function closeModalUser() {
        $('#addFormuser').modal('hide');
    };

    $("#addUser").click(function () {
        openModalUser();
    });

    // Profile
    $("#addProfile").click(function () {
        openModalProfile();
    });
    function openModalProfile() {
        $('#addFormprofile').modal('show');
    };

    function closeModalProfile() {
        $('#addFormprofile').modal('hide');
    };

    window.editRowNew = function(name) {
        var source = $("#addRow").html();
        var template = Handlebars.compile(source);
        var value = $('#'+name+'Select option:selected').val();
        var indx = $('.'+name+'AccessRightTable tbody').attr('index');
        var accessRight = $('input[name=accessRight'+name+']:checked').attr('data-name');
        var html = template({id: value, name: name, index: indx, accessRight: accessRight});
        var valueOld = $('#'+name+'IdOld').val();
        if ($('#'+name+'EditItem').val() == "0") {
            if ($('table.' + name + 'AccessRightTable tbody > tr[userId=' + value + ']').length <= 0) {
                $('table.' + name + 'AccessRightTable tbody').append(html);
                var index = parseInt(indx) + 1;
                console.log(index);
                console.log("html : " + html);
                console.log($('.' + name + 'AccessRightTable'));

                $('.' + name + 'AccessRightTable tbody').attr('index', index);
            }
        } else {
            if (valueOld == value) {
                $('table.' + name + 'AccessRightTable tbody >  tr[userId=' + valueOld + ']').replaceWith(html);
            } else {
                if ($('table.' + name + 'AccessRightTable tbody > tr[' + name + 'Id=' + value + ']').length <= 0) {
                    $('table.' + name + 'AccessRightTable tbody >  tr[' + name + 'Id=' + valueOld + ']').replaceWith(html);
                }
            }
        }

        $('#' + name + 'Select option').prop('selected', false);
        $('input[type=radio]').prop('checked', false);
        $('#addForm'+name).modal('hide');
    }
});