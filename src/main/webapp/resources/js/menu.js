$(function () {
    var countChecked = function () {
//           var selected = [];
        var str = ""
        $('input[name=DeleteRow]:checked').each(function () {
            str += $(this).attr('value') +
                ",";
        });
        var res = str.substring(0, str.length-1);
        console.log(res);
        var h = "";
        if(str.length>0){
            h = "?userIds="+ res;
        }

        $("a[name=deleteUser]").attr('href',h);
    };
    countChecked();

    $('#addFormuser').on('shown.bs.modal', function () {
        $('#userSelect').focus();
    }) 

    // User
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
        openModalUser();
    });

    // Profile
    $("#addProfile").click(function () {
        $("#profileTbody > tr").each(function(){
            var name=$(this).attr("profileId");
            $('#profileSelect option[value="'+name+'"]').remove();
        });
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

    window.editRowNew = function(name) {
        var source = $("#addRow").html();
        var template = Handlebars.compile(source);
        var value = $('#'+name+'Select option:selected').val();
        var indx = $('.'+name+'AccessRightTable tbody').attr('index');
        var accessRight = 'NONE'
        var html = template({id: value, name: name, index: indx, accessRight: accessRight});
        var valueOld = $('#'+name+'IdOld').val();
        if ($('#'+name+'EditItem').val() == "0") {
            if ($('table.' + name + 'AccessRightTable tbody > tr[' + name + 'Id=' + value + ']').length <= 0) {
                $('table.' + name + 'AccessRightTable tbody').append(html);
                var index = parseInt(indx) + 1;
                console.log(index);
                console.log("html : " + html);
                console.log($('.' + name + 'AccessRightTable'));

                $('.' + name + 'AccessRightTable tbody').attr('index', index);
            }
        } else {
            indx=$('table.' + name + 'AccessRightTable tbody >  tr[' + name + 'Id=' + valueOld + ']').attr("index");
            html = template({id: value, name: name, index: indx, accessRight: accessRight});
            if (valueOld == value) {
                $('table.' + name + 'AccessRightTable tbody >  tr[' + name + 'Id=' + value + ']').replaceWith(html);
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

