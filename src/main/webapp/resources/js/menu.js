$(function () {

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
        var accessRight = 'NONE'
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

})

