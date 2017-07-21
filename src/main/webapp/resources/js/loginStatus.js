// MinhTV
window.onload = function () {

    $('#save').on("click", function () {
        var indx = $('#searchByColumnForms').val();
        $("#searchByColumnForm" + indx).html($('#valid' + indx));
        $("#searchByColumnForm" + indx).append($('#type' + indx));
        $("#searchByColumnForm" + indx).append('<div id="tBody' + indx + '"></div>');
        $("#tBody" + indx).append($('#conditionTBody').html());
        $('#addFormSearchColumn').modal('hide');

    });

    $('#searchButton').on("click", function () {
        $.ajax({
            type: "POST",
            url: window.baseUrl + "/loginView",
            data: $("#searchByColumnForm").serialize(), // serializes the form's elements.
            success: function (data) {
                var context = {
                    ctlLogins : data
                }
                console.log(data);
                console.log(context);
                $('#evidentTBody').html(earth.buildHtml("#loginStatusRow", context));
            }
        });
    });
}
