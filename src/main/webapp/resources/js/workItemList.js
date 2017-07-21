// MinhTV


window.onload = function () {

    $('.icon_edit').on("click", function (e) {
        // e.preventDefault();
        console.log("aaa");
        if(1==1){
            return true;
        }
        return false;
    });
    $('#unlockButton').on("click", function (){
        var i=0;
        $('#workItemTBody > tr').each(function() {
            if($(this).find('.deleteCheckBox').prop('checked')){
                var value=$(this).attr('eventId');
                var context={
                    id:i,
                    value:value
                };
                $('#unlockForm').append(earth.buildHtml("#unlockRows", context))
                i++;
            }
        });
        if(i > 0){
            $('.deleteAllCheckBox').prop("checked",false);
            $.ajax({
                type: "POST",
                url: window.baseUrl + "/workItem/unlock",
                data: $("#unlockForm").serialize(), // serializes the form's elements.
                success: function (data) {
                    var context = {
                        workItems: data
                    }
                    $('#workItemTBody').html(earth.buildHtml("#searchWorkItemRows", context));
                    $('#unlockForm').html('');

                }
            });
        }
    });

    $('#save').on("click", function () {
        var indx = $('#searchByColumnForms').val();
        $("#searchByColumnForm" + indx).html($('#valid' + indx));
        $("#searchByColumnForm" + indx).append($('#type' + indx));
        $("#searchByColumnForm" + indx).append('<div id="tBody' + indx + '"></div>');

        $("#tBody" + indx).append($('#conditionTBody').html());
        $('#addFormSearchColumn').modal('hide');

    });

    $('#searchButton').on("click", function () {
        $("#templateIdCondition").val($('#templateId').val());
        $("#templateTypeCondition").val($('#templateType').val());
        $.ajax({
            type: "POST",
            url: window.baseUrl + "/workItem/searchColumn",
            data: $("#searchByColumnForm").serialize(), // serializes the form's elements.
            success: function (data) {
                var context = {
                    searchForms: data
                }
                $('#searchListDiv').html(earth.buildHtml("#searchList", context));
                $("table.search").find("tr.rows").click(function () {
                    $("#workItemId").val(($(this).find('td:first').html()));
                })
            }
        });


    });

    $('#templateType').change(function () {
        $("#templateTypeCondition").val($('#templateType').val());
        $.ajax({
            type: "POST",
            url: window.baseUrl + "/workItem/getTemplateName",
            data: $("#searchByColumnForm").serialize(), // serializes the form's elements.
            success: function (data) {
                var context = {
                    mgrTemplates: data
                }
                $('#selectTemplateName').html(earth.buildHtml("#selectTemplateTypeOption", context));
                var templateID=$("#templateIdCondition").val();
                $("#selectTemplateName").find("#templateId").find("option").each(function () {
                    if($(this).val()===templateID){
                        $(this).attr("selected","selected");
                    }

                })
            }
        });
    });


    $('#btnReflect').on("click", function () {
        $.ajax({
            type: "POST",
            url: window.baseUrl + "/workItem/getList",
            data: $("#deleteListForm").serialize(), // serializes the form's elements.
            success: function (data) {
                var context = {
                    workItems: data
                }
                $('#workItemTBody').html(earth.buildHtml("#searchWorkItemRows", context));
                $('#addFormSearch').modal('hide');
            }
        });
    });
    $('#btnCancel').on("click", function () {
        $("#workItemId").val('');
        $.ajax({
            type: "POST",
            url: window.baseUrl + "/workItem/getList",
            data: $("#deleteListForm").serialize(), // serializes the form's elements.
            success: function (data) {
                var context = {
                    workItems: data
                }
                $('#workItemTBody').html(earth.buildHtml("#searchWorkItemRows", context));
                $('#addFormSearch').modal('hide');

            }
        });

    });
}

$(function () {


    // popup search
    function openFormSearch() {
        $('#addFormSearch').modal('show');
    };

    function closeFormSearch() {
        $('#addFormSearch').modal('hide');
    };

    $("#btnSearchForm").click(function () {
        $.ajax({
            type: "POST",
            url: window.baseUrl + "/workItem/searchColumn",
            data: $("#searchByColumnForm").serialize(), // serializes the form's elements.
            success: function (data) {
                var context = {
                    searchForms: data
                }
                $('#searchListDiv').html(earth.buildHtml("#searchList", context));
                $("table.search").find("tr.rows").click(function () {
                    $("#workItemId").val(($(this).find('td:first').html()));
                })

            }
        });
        $('#templateType').change();

        openFormSearch();

    });

    // popup search by column
    function openFormSearchColumn() {
        $('#addFormSearchColumn').modal('show');
    };

    function closeFormSearchColumn() {
        $('#addFormSearchColumn').modal('hide');
    };

})