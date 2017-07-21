/*
 * JS for edit work item screen 
 */
var result = {
    SUCCESS: 1,
    ERROR: 0
};
var ItemTypeEnum = {
    PROCESS: 'PROCESS',
    WORKITEM: 'WORKITEM',
    FOLDER_ITEM: 'FOLDERITEM',
    DOCUMENT: 'DOCUMENT',
    LAYER: 'LAYER'
};

var accessRightData = {
    RO: 'RO',
    RW: 'RW',
    NONE: 'NONE',
    SO: 'SO',
    FULL: 'FULL'
}

var eventTypeEnum = {
    IS_LINK_CLICK: 1,
    IS_BUTTON_CLICK: 0,
    IS_CHANGED_LIST: 2
};
var isChangedData = false;

var url = {
    SHOW_TEMPLATE       :   window.baseUrl + "/workItem/showTemplate",
    LOAD_TEMPLATE_FIELD :   window.baseUrl + "/workItem/setTemplateField",
    UPDATE_PROCESS      :   window.baseUrl + "/workItem/updateProcess",
    UPDATE_WORK_ITEM    :   window.baseUrl + "/workItem/updateWorkItem",
    UPDATE_FOLDER_ITEM  :   window.baseUrl + "/workItem/updateFolderItem",
    UPDATE_DOCUMENT     :   window.baseUrl + "/workItem/updateDocument",
    UPDATE_LAYER        :   window.baseUrl + "/workItem/updateLayer",
    CLOSE               :   window.baseUrl + "/workItem/closeEdit"
}

/**
 * Function
 */
$(function () {

    // Click node on tree event
    $(".treeview li").click(function () {
        $(".treeview li").removeClass("node-selected");
        $(this).addClass("node-selected");
    });

    // Cancel click event
    $("#cancelButton").on("click", function () {
        $('#closeEditWorkItemModal').modal('show');
    })

    // Cancel on modal click event
    $("#btnModalCancelWorkItem").on("click", function () {
         var requestParams = {
         workItemId: $("#workItemId").val()
         };
         ajaxAction(url.CLOSE, requestParams, function (data, res) {
             if (res == result.SUCCESS) {
             window.location.href = window.baseUrl + "/workItem/";
             } else {
                console.log("Back error: " + data);
             }
         })
    })

    // Button save click event
    $("#saveButton").on("click", function () {
        saveTemplate(eventTypeEnum.IS_BUTTON_CLICK);
    })

    // Change value of template event
    $(document).on('change', 'input', function() {
        isChangedData = true;
    });
    $(document).on('change', '#taskList', function() {
        isChangedData = true;
    });

    // Focus to first node when load screen
    $(".list-group>li:first").trigger("click");

    $(".list-group>li").on("click", function () {
        var accessRight = $(this).attr("accessRight");
        if (accessRight == accessRightData.SO) {
            var nextType = $(this).attr("type");
            var currentType = $("#type").val();
            if ((currentType != ItemTypeEnum.DOCUMENT) && (nextType != ItemTypeEnum.DOCUMENT)) {
                $(this).removeClass("node-selected");
                $("." + $("#currentNode").val()).addClass("node-selected");
            } else if ((currentType == ItemTypeEnum.DOCUMENT) && (nextType != ItemTypeEnum.DOCUMENT)) {
                $(this).removeClass("node-selected");
                var parentFolderClass = $(".current-document").attr("parentNode");
                $("." + parentFolderClass).addClass("node-selected");
            }
        }
    })
});

/**
 * Show layer template data
 *
 * @param obj
 */
function showTemplate(obj) {
    if (isChangedData) {
        saveTemplate(eventTypeEnum.IS_LINK_CLICK, function (saveStatus) {
            if(saveStatus) {
                pShowTemplateData(obj)
            } else {
                $("#treeview5>ul>li").removeClass("node-selected");
                $("#treeview5>ul>li."+$("#currentNode").val()).addClass("node-selected")
            }
        });
    } else {
        pShowTemplateData(obj);
    }
}

/**
 * pShowTemplateData
 *
 * @param obj
 */
function pShowTemplateData(obj) {
    var typeTemplate = $(obj).attr("type");
    // Hide all document image
    if (typeTemplate != ItemTypeEnum.DOCUMENT) {
        hideAllDocumentImage();
    }

    // Load template
    var requestParams = {
        workspaceId: $("#workspaceId").val(),
        workItemId: $("#workItemId").val(),
        type: typeTemplate
    };
    if (typeTemplate ==  ItemTypeEnum.PROCESS) {
        requestParams["processId"] = $(obj).attr("processId");
    } else if (typeTemplate == ItemTypeEnum.WORKITEM) {
        // For maintain
    } else if (typeTemplate == ItemTypeEnum.FOLDER_ITEM) {
        // Show document image
        var folderItemNo = $(obj).attr("folderItemNo");
        showDocumentImageOfFolder(folderItemNo);
        requestParams["folderItemNo"] = folderItemNo;
    } else if (typeTemplate == ItemTypeEnum.DOCUMENT) {
        requestParams["folderItemNo"] = $(obj).attr("folderItemNo");
        requestParams["documentNo"] = $(obj).attr("documentNo");
    } else if (typeTemplate == ItemTypeEnum.LAYER) {
        requestParams["folderItemNo"] = $(obj).attr("folderItemNo");
        requestParams["documentNo"] = $(obj).attr("documentNo");
        requestParams["layerNo"] = $(obj).attr("layerNo");
        requestParams["ownerId"] = $(obj).attr("ownerId");
    }

    var accessRight = $.trim($(obj).attr("accessRight"));
    ajaxAction(url.SHOW_TEMPLATE, requestParams, function (data, res) {
        if (res == result.SUCCESS) {
            $("#template_area").html(data).promise().done(function(){
                $("#templateList").val($("#currentTemplateId").val());
                $("#accessRight").val(accessRight);
                if(accessRight == accessRightData.RO) {
                    $("#template_area_fieldSet").attr("disabled", true);
                } else if((accessRight == accessRightData.FULL) || (accessRight == accessRightData.RW)) {
                    $("#template_area_fieldSet").attr("disabled", false);
                } else {
                    alert("No permission");
                }
            });
        } else {
            console.log("Error show template. requestParams = " + requestParams);
        }
        if (typeTemplate == ItemTypeEnum.DOCUMENT) {
            $(".document-image .document-link").removeClass("current-document");
            $(obj).addClass("current-document");
        }
        isChangedData = false;
    })
}

/**
 * Reload template with newest selected template
 */
function setTemplateField() {
    var templateId = $("#templateList").val();
    var preTemplateId = $("#currentTemplateId").val();
    if (templateId == preTemplateId) {
        return;
    }
    if (isChangedData) {
        saveTemplate(eventTypeEnum.IS_CHANGED_LIST, function (saveStatus) {
            if(saveStatus) {
                pSetTemplateField(templateId);
            } else {
                $("#templateList").val(preTemplateId);
            }
        });
    } else {
        pSetTemplateField(templateId);
    }

    isChangedData = true;
}

/**
 * Private SetTemplateField
 *
 * @param templateId
 */
function pSetTemplateField(templateId) {
    if (templateId == "") {
        $("#div_template").html("");
    } else {

        // Load template field
        var requestParams = {
            templateField: $("#templateList option:selected").attr("templateField"),
            templateId: templateId,
            folderItemNo: $("#folderItemNo").val(),
            workItemId: $("#workItemId").val(),
            processId: $("#processId").val(),
            documentNo: $("#documentNo").val(),
            type: $("#type").val()
        };
        ajaxAction(url.LOAD_TEMPLATE_FIELD, requestParams, function (data, res) {
            if (res == result.SUCCESS) {
                $("#div_template").html(data).promise().done(function(){
                    $("#currentTemplateId").val($("#templateList").val());
                });
            } else {
                // TODO Need to do something here
                console.log(data);
            }
        })
    }
}

/*
 * Show document image by folder
 */
function showDocumentImageOfFolder(folderItemNo) {
    $("#folder" + (folderItemNo)).removeClass("hidden");
    /*var accessRight = $.trim($(obj).attr("accessRight"));
    if(accessRight == accessRightData.RO) {
        $(".document-image").attr("disabled", true);
        $(".document-image>a").addClass("disabled");
    } else if((accessRight == accessRightData.FULL) || (accessRight == accessRightData.RW)) {
        $(".document-image").attr("disabled", false);
        $(".document-image>a").removeClass("disabled");
    } else {
        alert("No permission");
    }*/
}

/*
 * Hide all document image
 */
function hideAllDocumentImage() {
    $(".document-image").addClass("hidden");
}

function saveTemplateFromModal() {
    // Remove modal
    $('#closeEditWorkItemModal').modal('hide');

    // Validate data and do save
    saveTemplate(eventTypeEnum.IS_BUTTON_CLICK);
}

/**
 * Save template data
 */
function saveTemplate(eventType, callback) {
    removeClassError();
    var accessRight = $("#accessRight").val();
    if ((accessRight != accessRightData.RW) && (accessRight != accessRightData.FULL)) {
        var htmlError = '<div id="messages">';
        htmlError += '<ul class="list-group">';
        htmlError += '<li class="list-group-item list-group-item-danger" data-code="All">';
        htmlError += 'Access denied';
        htmlError += '</li>';
        htmlError += '</ul>';
        htmlError += '</div>';
        $("#errorMessage").html(htmlError);
        return;
    }
    var templateId = $.trim($("#templateList").val());
    if (eventType == eventTypeEnum.IS_CHANGED_LIST) {
        templateId = $.trim($("#currentTemplateId").val());
    }
    var updateParams = [];
    if (templateId == '') {
        updateParams = {
            workspaceId         : $.trim($("#workspaceId").val()),
            templateId          : null,
            eventType           : $.trim(eventType),
            workItemId          : $.trim($("#workItemId").val())
        }
    } else {
        // Get template data
        var templateData = [];
        $(".table_list>tbody>tr").each(function () {
            var item = {};
            var name = ($(this).find("input[name='fieldName']").val());
            var value = ($(this).find("input[name='fieldValue']").val());
            item [name] = value;
            templateData.push(item);
        })
        var currentOption = $("#templateList option[value="+templateId+"]");
        updateParams = {
            workspaceId         : $.trim($("#workspaceId").val()),
            templateId          : templateId,
            templateField       : $.trim(currentOption.attr("templateField")),
            templateTableName   : $.trim(currentOption.attr("templateTableName")),
            templateType        : $.trim(currentOption.attr("templateType")),
            templateName        : $.trim(currentOption.text()),
            templateData        : JSON.stringify(templateData),
            eventType           : $.trim(eventType),
            workItemId          : $.trim($("#workItemId").val())
        }
    }

    var itemType = $.trim($("#type").val());
    var urlUpdate = [];
    if (itemType == ItemTypeEnum.PROCESS) {

        // Update process
        updateParams["processId"] = $.trim($("#processId").val());
        urlUpdate = url.UPDATE_PROCESS;
    } else if (itemType == ItemTypeEnum.WORKITEM) {

        // Update work item
        updateParams["task"] = $("#taskList").val();
        urlUpdate = url.UPDATE_WORK_ITEM;
    } else if (itemType == ItemTypeEnum.FOLDER_ITEM) {
        updateParams["folderItemNo"] =  $.trim($("#folderItemNo").val());

        // Update folder item
        urlUpdate = url.UPDATE_FOLDER_ITEM;
    } else if (itemType == ItemTypeEnum.DOCUMENT) {
        updateParams["folderItemNo"] =  $.trim($("#folderItemNo").val());
        updateParams["documentNo"] =  $.trim($("#documentNo").val());

        // Update document
        urlUpdate = url.UPDATE_DOCUMENT;
    } else if (itemType == ItemTypeEnum.LAYER) {
        updateParams["folderItemNo"] =  $.trim($("#folderItemNo").val());
        updateParams["documentNo"] =  $.trim($("#documentNo").val());
        updateParams["layerNo"] =  $.trim($("#layerNo").val());
        updateParams["ownerId"] =  $.trim($("#ownerId").val());

        // Update layer
        urlUpdate = url.UPDATE_LAYER;
    }
    $.ajax({
        url         : urlUpdate,
        async       : true,
        dataType    : 'html',
        type        : "POST",
        cache       : true,
        data        : updateParams,
        success: function (data, textStatus, xhr) {
            if (eventType == eventTypeEnum.IS_BUTTON_CLICK) {
                window.location.href = window.baseUrl + "/workItem/";
            }
            $("#errorMessage").html("");
            if ($.isFunction(callback)){
                callback(true);
            }
        },
        error: function (data, xhr, textStatus, errorThrown) {
            $("#errorMessage").html("");
            $("#errorMessage").html(data.responseText).promise().done(function() {
                $("#messages li").each(function() {
                    $("." + $(this).attr("data-code")).addClass("has-error");
                })
                $(".has-error:first").focus();
                if ($.isFunction(callback)){
                    callback(false);
                }
            });
        }
    });
}

/**
 * Load template by ajax
 *
 * @param url
 * @param data
 * @param callback
 * @returns
 */
function ajaxAction(url, data, callback) {
    $.ajax({
        url         : url,
        async       : true,
        dataType    : 'html',
        type        : "POST",
        cache       : true,
        data        : data,
        success: function (data, textStatus, xhr) {
            return callback(data, result.SUCCESS);
        },
        error: function (data, xhr, textStatus, errorThrown) {
            return callback(xhr.status, result.ERROR);
        }
    });
}

/**
 * Remove error class
 */
function removeClassError() {
    $(".has-error").removeClass("has-error");
}