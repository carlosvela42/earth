// function getUrlParameter(sParam) {
//   var sPageURL = decodeURIComponent(window.location.search.substring(1)),
//       sURLVariables = sPageURL.split('&'), sParameterName, i;
//   for (i = 0; i < sURLVariables.length; i++) {
//     sParameterName = sURLVariables[i].split('=');
//     if (sParameterName[0] === sParam) {
//       return sParameterName[1] === undefined ? true : sParameterName[1];
//     }
//   }
// };

window.earth = {}

$(function () {
    var ww = $(window).width();
    var wh = $(window).height();


    $("body").css({
        "min-height": wh
    });

    $(".global ul").css({
        "height": wh - 60
    });

    $(".global .navigation_btn").click(function () {
        $(".global > ul").fadeToggle();
    });

    $(".global > ul > li").click(function () {
        $(this).siblings().find("ul").hide("fast");
        $(this).find("ul").stop().fadeToggle("fast");
    });

    $(".global > ul > li").hover(function () {
            $(this).addClass("active");
        },
        function () {
            $(this).removeClass("active");
        });


});

$(window).on('resize', function () {

    var ww = $(window).width();
    var wh = $(window).height();

    $(".global ul").css({
        "height": wh - 60
    });

    $("body").css({
        "min-height": wh
    });

});

jQuery(function ($) {
    $.extend({
        form: function (url, data, method) {
            if (method == null) method = 'POST';
            if (data == null) data = {};

            var form = $('<form>').attr({
                method: method,
                action: url
            }).css({
                display: 'none'
            });

            var addData = function (name, data) {
                if ($.isArray(data)) {
                    for (var i = 0; i < data.length; i++) {
                        var value = data[i];
                        addData(name + '[' + i + ']', value);
                    }
                } else if (typeof data === 'object') {
                    for (var key in data) {
                        if (data.hasOwnProperty(key)) {
                            addData(name + '[' + key + ']', data[key]);
                        }
                    }
                } else if (data != null) {
                    form.append($('<input>').attr({
                        type: 'hidden',
                        name: String(name),
                        value: String(data)
                    }));
                }
            };

            for (var key in data) {
                if (data.hasOwnProperty(key)) {
                    addData(key, data[key]);
                }
            }

            return form.appendTo('body');
        }
    });
});

function initWorkspaceSelectBox() {
    $("#workspaceSelection li a").click(function (e) {
        e.preventDefault();
        $("#workspaceSelection .workspaceName").html($(this).data("name"));
        var form = $("#filter");
        form.find("[name='workspaceId']").val($(this).data("value"));
        form.submit();
    });
}

// All Common Init Function should be here
$(function () {
    initWorkspaceSelectBox();
});


// Common button
$(function () {
    $("#saveButton").click(function () {
        var formId = $(this).data("form_id");
        $("#" + formId).submit();
    })
});

//search box
$(function () {
    $('table.clientSearch').each(function () {
        var $this = $(this);
        // $(CHECKBOX)
        $this.find('.deleteAllCheckBox').click(function () {
            var table = $(this).parents("table");
            table.find('tbody > tr').each(function () {
                $(this).find('.deleteCheckBox').prop('checked', $this.find('.deleteAllCheckBox').prop('checked'))
            });
        });
        //Search
        $this.find('.condition td > input').keyup(function () {
            var array = [];
            var table = $(this).parents("table");
            $this.find('.condition td > input').each(function () {
                if ($(this).val()) {
                    var searchTxt = $(this).val().toUpperCase();
                    console.log(searchTxt);
                    var col = $(this).attr('col');
                    var count = 0;
                    table.find('tbody > tr').each(function () {
                        if (!searchTxt || $(this).find('td:nth-child(' + col + ')').text().toUpperCase().indexOf(searchTxt) >= 0) {
                            if (!array[count]) {
                                array[count] = 1;
                                $(this).show();
                            } else {
                                if (array[count] === 1) {
                                    $(this).show();
                                } else {
                                    $(this).hide();
                                }
                            }
                        } else {
                            array[count] = 2;
                            $(this).hide();
                        }
                        count++;
                    });
                }
            });
            if (array.length === 0) {
                table.find('tbody > tr').each(function () {
                    $(this).show();
                });
            }
        });
    });
});

earth.Messages = {
    E0001: "{0} may be not empty.",
    E0013: 'No checked box is selected.',
    E0014: 'You did not tick into CheckBox "Confirm Delete" before deleting.',
};

// Delete button
earth.onDeleteButtonClick = function (cb) {
    $('#deleteButton').click(function () {
        earth.removeMessage(["E0014", "E0013"]);

        if (!$('#deleteConfirm').prop('checked')) {
            earth.addMessage("E0014");
            return;
        }

        if ($(".table_list .table_body td:nth-child(1) input:checked").length == 0) {
            earth.addMessage("E0013");
            return;
        }
        cb();
    });
};

Handlebars.registerHelper('ifCond', function (v1, v2, options) {
    if (v1 == v2) {
        return options.fn(this);
    }
    return options.inverse(this);
});

earth.buildHtml = function (selector, context) {
    var context = context || {};
    var source = $(selector).html();
    var template = Handlebars.compile(source);
    return template(context);
};

function addMessageToElement(select, code, params) {
    var source = $("#message-template").html();
    var template = Handlebars.compile(source);
    var message = earth.Messages[code];

    for (var i = 0; i < params.length; i++) {
        message = message.replace("{" + i + "}", params[i]);
    }

    if (!message) {
        alert("Error: message does not exists. Add it to javascript.")
        return;
    }

    var messageDiv = $(select);
    if (messageDiv.find("b[data-code='" + code + "']").length == 0) {
        messageDiv.append(template({code: code, message: message}));
    }
}

earth.addMessage = function (code) {
    var params = Array.prototype.splice.call(arguments, 1);
    addMessageToElement("#messages", code, params);
};

earth.addModalMessage = function (code, args) {
    var params = Array.prototype.splice.call(arguments, 1);
    addMessageToElement("#modal-messages", code, params);
};

earth.removeMessage = function (codes) {
    var messageDiv = $("#messages");
    codes.forEach(function (code) {
        messageDiv.find("b[data-code='" + code + "']").remove();
    });
}

function addRow(name, value, indx, accessRight) {
    var source = $("#addRow").html();
    var template = Handlebars.compile(source);
    var html = template({id: value, name: name, index: indx, accessRight: accessRight});
    var valueOld = $('#' + name + 'IdOld').val();
    if ($('#' + name + 'EditItem').val() == "0") {
        if ($('table.' + name + 'AccessRightTable tbody > tr[' + name + 'Id=' + value + ']').length <= 0) {
            $('table.' + name + 'AccessRightTable tbody').append(html);
            var index = parseInt(indx) + 1;
            console.log(index);
            console.log("html : " + html);
            console.log($('.' + name + 'AccessRightTable'));

            $('.' + name + 'AccessRightTable tbody').attr('index', index);
        }
    } else {
        if (valueOld == value) {
            $('table.' + name + 'AccessRightTable tbody >  tr[' + name + 'Id=' + valueOld + ']').each(function () {
                $(this).attr(name + 'Id', value);
                console.log($(this));
                $(this).find('td').each(function () {
                    console.log($(this));
                    $(this).find('a').attr('onclick', 'editRow("' + name + '","' + value + '");');
                    $(this).find('span').html(value);
                    $(this).find('input').val(value);
                });
                $(this).find("td:last-child").find('span').html(accessRight);
                $(this).find("td:last-child").find('input').val(accessRight);
            });
        } else {
            if ($('table.' + name + 'AccessRightTable tbody > tr[' + name + 'Id=' + value + ']').length <= 0) {
                $('table.' + name + 'AccessRightTable tbody >  tr[' + name + 'Id=' + valueOld + ']').each(function () {
                    $(this).attr(name + 'Id', value);
                    console.log($(this));
                    $(this).find('td').each(function () {
                        console.log($(this));
                        $(this).find('a').attr('onclick', 'editRow("' + name + '","' + value + '");');
                        $(this).find('span').html(value);
                        $(this).find('input').val(value);
                    });
                    $(this).find("td:last-child").find('span').html(accessRight);
                    $(this).find("td:last-child").find('input').val(accessRight);
                });
            }
        }
    }

    cancel(name);
    return;
}


function delRow(name) {
    $('table.' + name + 'AccessRightTable tbody > tr').each(function () {
        if ($(this).find('.deleteCheckBox').prop('checked') == true) {
            $(this).remove();
        }
    });
}
// function editRow(name, value, accessRight) {
//     $('#' + name + 'EditItem').val("1");
//     console.log($('#' + name + 'editItem'));
//     $('#' + name + 'IdOld').val(value.toString());
//     console.log(value);
//
//     $('#' + name + 'Select option').each(function () {
//         if ($(this).val() == value) {
//             $(this).prop('selected', true);
//         }
//     });
//
//
//     $('input[type=radio]').each(function () {
//         if($(this).attr('data-name') == accessRight )
//         {
//             $(this).prop('checked', true);
//         }
//     });
// }


function editRow(name, value, accessRight) {
    $('#' + name + 'EditItem').val("1");
    console.log($('#' + name + 'editItem'));
    $('#' + name + 'IdOld').val(value.toString());
    console.log(value);

    $('#' + name + 'Select option').each(function () {
        if ($(this).val() == value) {
            $(this).prop('selected', true);
        }
    });


    $('input[type=radio]').each(function () {
        if ($(this).attr('data-name') == accessRight) {
            $(this).prop('checked', true);
        }
    });
    $('#addForm' + name).modal('show');
}


function cancel(name) {
    $('#' + name + 'Select option').prop('selected', false);
    $('#' + name + 'IdOld').val("");
    $('#' + name + 'EditItem').val(0)
    $('input[type=radio]').prop('checked', false);
}
