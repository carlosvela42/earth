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
            url: window.baseUrl + "/licenseHistory",
            data: $("#searchByColumnForm").serialize(), // serializes the form's elements.
            success: function (data) {
                var context = {
                    strCals : data
                }
                console.log(data);
                console.log(context);
                $('#licenseHistoryTBody').html(earth.buildHtml("#licenseHistoryRow", context));
            }
        });
    });
}
