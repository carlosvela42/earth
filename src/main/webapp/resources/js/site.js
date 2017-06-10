// Site management
$(function () {
    earth.onDeleteButtonClick(function () {
        var siteIds = [];
        $('#processTbody > tr').each(function () {
            if ($(this).find('.deleteCheckBox').prop('checked')) {
                siteIds.push($(this).attr('siteId'));
            }
        });
        console.log(siteIds);
        if (siteIds.length > 0) {
            $.form(window.baseUrl + "/site/deleteList", {"listIds": siteIds}).submit();
        } else {
            earth.addMessage("E1014");
            return;
        }
    });
$(".deleteCheckBox").click(function(){
    checked();
});

    $(".deleteAllCheckBox").click(function(){
        checked();
    });

});

function checked () {
    var siteIds = "";
    $('#userTbody > tr').each(function () {
        if ($(this).find('.deleteCheckBox').prop('checked')) {
            console.log($(this).attr('siteId').toString());
            siteIds += $(this).attr('siteId').toString() + ",";
        }
    });
    console.log(siteIds);
    if (siteIds.length > 0) {
        siteIds = siteIds.substring(0, siteIds.length - 1);
    }
    $("#siteIds").val(siteIds);
    return;
};


function filter() {
    // Declare variables
    var siteIdInput, i;
    siteIdInput = document.getElementById('siteIdInput').value
            .toUpperCase();
    // Loop through all list items, and hide those who don't match the search query
    for (i = 0; i < $("#sitesTable tr ").length - 2; i++) {

        if (($("#siteId" + i).html().toUpperCase().indexOf(siteIdInput) > -1)) {
            $("#row" + i).show();

        } else {
            $("#row" + i).hide();
        }
    }
}


// Add site
window.onload = function() {
    var countChecked = function() {
        var str = ""
        $('input[name=ChooseRow]:checked').each(function() {
            str += $(this).attr('value') + ",";
        });

        if (str.length > 0) {
            str = str.substring(0, str.length - 1);
        }
        $("#directoryIds").val(str);
    }
    $("input[name=ChooseRow]").on("click", countChecked);
}
function validate() {
    var str = $("#directoryIds").val();

    if (str.length > 0) {
        document.forms[0].submit();
    } else {
        $("#message").html("choose directory");
    }
}