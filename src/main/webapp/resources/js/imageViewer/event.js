$(document).on('keydown', function ( e ) {
    // You may replace `c` with whatever key you want
    if ((e.metaKey || e.ctrlKey) && ( String.fromCharCode(e.which).toLowerCase() === 'x') ) {
        onCut();
    }
    else if ((e.metaKey || e.ctrlKey) && ( String.fromCharCode(e.which).toLowerCase() === 'c') ) {
        onCopy();
    }
    else if ((e.metaKey || e.ctrlKey) && ( String.fromCharCode(e.which).toLowerCase() === 'v') ) {
        onPaste();
    }
});

function onClick(e) {
    var id = "#" + $(this).attr('id');
    console.log("onClick:id=" + id);
    processClick(id);
}

function onCopy() {
    console.log("onCopy!");
    copyTimeRotation = currentRotation;
    resetClipBoard();

    // Clone clipBoardAnnos from selectedAnnos.
    cloneArray(clipBoardAnnos, selectedAnnos);
}

function onCut() {
    console.log("onCut!");
    copyTimeRotation = currentRotation;
    resetClipBoard();
    copyArray(clipBoardAnnos, selectedAnnos);
    unSelectMultipleSvg();
    for (var i in clipBoardAnnos) {
        SVG.get(clipBoardAnnos[i]).hide();
    }
}

function onPaste() {
    console.log("onPaste");
    if (clipBoardAnnos.length > 0) {
        unSelectMultipleSvg();
        cloneArray(selectedAnnos, clipBoardAnnos);
        copyArray(allAnnos, selectedAnnos);
        iv.selectedGroup = iv.svg.group();
        displaySelectedAnnos();
        // Update transform as copy time.
        if (selectedAnnos.length == 1) {
            // smartTransform("#" + e.node.id, copyTimeRotation -
            // currentRotation);
            SVG.get(selectedAnnos[0]).rotate(copyTimeRotation - currentRotation);
        } else {
            // smartTransform("#" + iv.selectedGroup.node.id, copyTimeRotation -
            // currentRotation);
            for (var i in selectedAnnos) {
                SVG.get(iv.selectedGroup.node.id).rotate(copyTimeRotation - currentRotation);
            }
        }
    }
}

function onMouseDown(e) {
    console.log("onMouseDown!");
    iv.drawStartX = e.x;
    iv.drawStartY = e.y;

    // unSelectMultipleSvg();
    if (iv.toolbarFlg == toolbars.BTN_SELECT) {
        if (waitMouseUpFlg) {
            selectMultipleSvg();
            iv.sMultipleSvg.draw('cancel');
            return;
        }
        createMultipleSelectRect(e);
        iv.drawFlg = setDrawFlgWhenClick();
        console.log("drawFlg=" + iv.drawFlg);
        if (iv.drawFlg) {
            unSelectMultipleSvg();
            iv.sMultipleSvg.front();
            console.log("waitMouseUpFlg=" + waitMouseUpFlg);

                iv.sMultipleSvg.draw(e);

        }
        iv.drawFlg = true;
    } else {
        unSelectMultipleSvg();
        // draw text then open text popup
        if (iv.settingAnno == "text") {
            iv.drawAnno = iv.svg.text("");
            iv.drawAnno.front();
            iv.drawAnno.draw(e);
            textPopup();
            // draw comment are draw image and draw text, then add text to class
            // to get, then get user login to set, then reset comment text area,
            // then open comment popup
        } else if (iv.settingAnno == "comment") {
            iv.drawAnno = iv.svg.image(getResource(commentImage)).attr('width','48').attr('height','48');
            iv.drawAnno.front();
            iv.drawAnno.draw(e);
            iv.drawAnno.width(48).height(48);
            iv.drawAnno = iv.svg.text(userInfo).attr("class", "newComment");
            iv.drawAnno.front();
            iv.drawAnno.draw(e);

            $(commentExample.COMMENTTEXTBOX).val(userInfo);

            $(commentExample.COMMENTTEXTAREA).val("");

            $(commentExample.COMMENTPOPUP).modal();
            iv.drawAnno.front();
            iv.drawAnno.draw(e);
            iv.drawAnno.y(iv.drawAnno.y()+48);

        } else {
            iv.drawAnno = iv.settingAnno.clone();
            iv.drawAnno.front();
            iv.drawAnno.draw(e);
        }
        iv.drawAnno.attr("class", iv.drawAnno.attr("class")+" "+iv.layerActive);
    }
    waitMouseUpFlg = true;
}

// text popup when draw
function textPopup() {
    $(textExample.COMMENTTEXT).val('');
    $(textExample.COMMENTTEXT).css("font-family", propertiesType.fontFamily);
    $(textExample.COMMENTTEXT).css("fontSize", 18);
    $(textExample.COMMENTTEXT).css("font-style", propertiesType.fontStyle);
    $("#color").val(selectOptionByText("#color", propertiesType.penColor));
    $("#fill").val(selectOptionByText("#fill", 'Transparent'));
    $("#border").val(selectOptionByText("#border", 'Transparent'));
    $("#font").val(selectOptionByText("#font", propertiesType.fontFamily));
    $("#fontSize").val(selectOptionByText("#fontSize", propertiesType.fontSize));
    $("#fontStyle").val(selectOptionByText("#fontStyle", propertiesType.fontStyle));
    $("#myModalText").modal();
}

// get value of select option by text
function selectOptionByText(id, value) {
    return $(id + ' option').filter(function () {
        return $(this).html().toLowerCase() === value.toLowerCase();
    }).val();
}
function onMouseUp(e) {
    console.log("onMouseUp!");
    waitMouseUpFlg = false;

        if (iv.toolbarFlg == toolbars.BTN_SELECT) {
            if (iv.drawStartX == e.x && iv.drawStartY == e.y) {
                unSelectMultipleSvg();
                selectOneSvg();
            } else {
                selectMultipleSvg();
            }
            iv.sMultipleSvg.draw('cancel');
        } else if (iv.drawStartX == e.x && iv.drawStartY == e.y){
            iv.drawAnno.draw('cancel');
        }
     else {
        iv.drawAnno.draw('stop', e);
        if (allAnnos.indexOf(iv.drawAnno.node.id) == -1) {
            allAnnos.push(iv.drawAnno.node.id);
        }
    }
}

function selectMultipleSvg() {
    if (selectedFlg) {
        return;
    }
    console.log("selectMultipleSvg!");
    // unSelectMultipleSvg();
    for (var i in allAnnos) {
        var e = SVG.get(allAnnos[i]);
        if (hasIntersection(iv.sMultipleSvg, e)) {
            if (e != null && (selectedAnnos.indexOf(allAnnos[i]) == -1)) {
                // process with select text or image then select all comment
                if (allAnnos[i].startsWith('SvgjsText')&&typeof allAnnos[parseInt(i) + 1] != "undefined") {

                        if (allAnnos[parseInt(i) + 1].startsWith('SvgjsImage')) {
                            selectedAnnos.push(allAnnos[parseInt(i) + 1]);
                            i++;
                        }

                }

                selectedAnnos.push(allAnnos[i]);
                if (allAnnos[i].startsWith('SvgjsImage')) {
                    if (selectedAnnos[selectedAnnos.length - 1] != allAnnos[i - 1])
                        selectedAnnos.push(allAnnos[i - 1]);
                }

                // iv.selectedGroup.add(e);
                // iv.selectedGroup.selectize().resize().draggable();
            }
        }
    }
    displaySelectedAnnos();
    console.log("allAnnos.length=" + allAnnos.length);
    console.log("selectedAnnos.length=" + selectedAnnos.length);
}

function selectOneSvg() {
    if (selectedFlg) {
        return;
    }
    console.log("selectOneSvg!");
    // unSelectMultipleSvg();
    for (var i in allAnnos) {
        var e = SVG.get(allAnnos[i]);
        if (hasIntersection(iv.sMultipleSvg, e)) {
            var index = selectedAnnos.indexOf(allAnnos[i]);
            selectedAnnos = [];
            if (index == -1) {
                // process with select text object
                if ($('#'+allAnnos[i]).attr('class')=='textDraw') {
                    selectedAnnos.push(allAnnos[i]);
                    selectedAnnos.push(allAnnos[parseInt(i) + 1]);
                    displaySelectedAnnos();
                    allAnnos.splice(i, 2);
                    allAnnos.push(selectedAnnos[0]);
                    allAnnos.push(selectedAnnos[1]);
                    return;
                }
                // process with select image of comment
                if (allAnnos[i].startsWith('SvgjsImage')) {
                    selectedAnnos.push(allAnnos[i]);
                    selectedAnnos.push(allAnnos[parseInt(i) + 1]);
                    displaySelectedAnnos();
                    allAnnos.splice(i - 1, 2);
                    allAnnos.push(selectedAnnos[1]);
                    allAnnos.push(selectedAnnos[0]);
                    return;
                }
            }
            if (allAnnos[i].startsWith('SvgjsText')) {
                selectedAnnos.push(allAnnos[parseInt(i) + 1]);
                selectedAnnos.push(allAnnos[i]);
                displaySelectedAnnos();
                allAnnos.splice(i, 2);
                allAnnos.push(selectedAnnos[1]);
                allAnnos.push(selectedAnnos[0]);
                return;
            }
            selectedAnnos.push(allAnnos[i]);
            displaySelectedAnnos();
            allAnnos.splice(i, 1);
            allAnnos.push(selectedAnnos[0]);

        }
    }

    console.log("allAnnos.length=" + allAnnos.length);
    console.log("selectedAnnos.length=" + selectedAnnos.length);
}

function displaySelectedAnnos() {
    if (selectedAnnos.length == 1) {
        var e = SVG.get(selectedAnnos[0]);
        e.show();
        e.selectize({
            deepSelect: true
        }).resize(true).draggable(true);
        // Change selection priority of element.
        var index = allAnnos.indexOf(selectedAnnos[0]);
        allAnnos.splice(index, 1);
        allAnnos.push(selectedAnnos[0]);
    } else if (selectedAnnos.length > 1) {
        iv.selectedGroup = iv.svg.group();
        for (var i in selectedAnnos) {
            var e = SVG.get(selectedAnnos[i]);
            e.show();
            e.selectize(false, {
                deepSelect: true
            }).resize(false).draggable(false);

            iv.selectedGroup.add(e);
        }
        // var selectedBounds = $(".svg_select_boundingRect").parent();
        iv.selectedGroup.selectize({
            deepSelect: true
        });
        iv.selectedGroup.draggable(true);
        iv.selectedGroup.front();
    }

    console.log("allAnnos.lenth=" + allAnnos.length);
    console.log("selectedAnnos.lenth=" + selectedAnnos.length);

    var boundingRects = [
        ".svg_select_boundingRect"
        , ".svg_select_points_lt"
        , ".svg_select_points_rt"
        , ".svg_select_points_rb"
        , ".svg_select_points_lb"
        , ".svg_select_points_t"
        , ".svg_select_points_r"
        , ".svg_select_points_b"
        , ".svg_select_points_l"
        , ".svg_select_points_rot"
        , ".svg_select_points_point"
    ];

    for (var c in boundingRects) {
        $(boundingRects[c]).mousedown(function () {
            resetDrawFlg();
        });
    }

    selectedFlg = true;
}

function unSelectMultipleSvg() {
    if (!selectedFlg) {
        return;
    }
    console.log("unSelectMultipleSvg!");

    if (selectedAnnos.length > 1) {
        for (var i in selectedAnnos) {
            var e = SVG.get(selectedAnnos[i]);
            e.selectize(false, {
                deepSelect: true
            }).draggable(false).resize(false);
        }
        iv.selectedGroup.selectize(false, {
            deepSelect: true
        });
        iv.selectedGroup.draggable(false, {});
        // iv.selectedGroup.selectize(false);
        //no know
        iv.selectedGroup.ungroup();
        iv.selectedGroup = iv.svg.group();
    } else if (selectedAnnos.length == 1) {
        var e = SVG.get(selectedAnnos[0]);
        e.selectize(false, {
            deepSelect: true
        }).resize(false).draggable(false);
    }
    // iv.hiddenRect.remove();
    // for (var i in selectedAnnos) {
    // var e = SVG.get(selectedAnnos[i]);
    // e.selectize(false).draggable(false).resize(false);
    // }
    // iv.selectedGroup.selectize(false,{});
    // iv.selectedGroup.draggable(false,{});
    // iv.selectedGroup.ungroup(iv.svg);
    // iv.selectedGroup = iv.svg.group();
    selectedAnnos = [];
    console.log("allAnnos.lenth=" + allAnnos.length);
    console.log("selectedAnnos.lenth=" + selectedAnnos.length);
    selectedFlg = false;
    clickOnSelectedBound = false;
}

function onRotate(type) {
    console.log("onRotate!");
    currentRotation = currentRotation + (90 * type);
    smartTransform(iv.svg.svgId, currentRotation);

    if (Math.abs(currentRotation) == 360) {
        currentRotation = 0;
    }
}

function processClick(id) {
    console.log("processClick:id=" + id);
    if (id == toolbars.BTN_SELECT || id == annoTypes.LINE || id == annoTypes.RECTANGLE || id == annoTypes.TEXT || id == annoTypes.HIGHLIGHT || id == annoTypes.COMMENT || id == annoTypes.TESTLINE) {
        iv.toolbarFlg = id;
        iv.drawFlg = true;
        if (id != toolbars.BTN_SELECT) {
            createSettingAnnotation(id);
        }
        else{
            $(iv.svg.svgId).css('cursor', 'auto');
        }
        return;
    } else {
        iv.drawFlg = false;
    }

    switch (id) {
        case toolbars.BTN_ZOOMFP:
            if (($('#imageDrawing').width() - getScrollBarWidth()) / iv.imgWidth > ($('#imageDrawing').height() - getScrollBarWidth()) / iv.imgHeight) {
                currentScale = ($('#imageDrawing').height() - getScrollBarWidth()) / iv.imgHeight;
            }
            else {
                currentScale = ($('#imageDrawing').width() - getScrollBarWidth()) / iv.imgWidth;
            }
            smartTransform(iv.svg.svgId, currentRotation);
            break;

        case toolbars.BTN_ZOOMFW:
            currentScale = ($('#imageDrawing').width() - getScrollBarWidth()) / iv.imgWidth;
            smartTransform(iv.svg.svgId, currentRotation);
            break;
        case toolbars.BTN_ZOOM200:
            currentScale = 2;
            smartTransform(iv.svg.svgId, currentRotation);
            break;
        case toolbars.BTN_ZOOM100:
            currentScale = 1;
            smartTransform(iv.svg.svgId, currentRotation);
            break;
        case toolbars.BTN_ZOOM75:
            currentScale = 0.75;
            smartTransform(iv.svg.svgId, currentRotation);
            break;
        case toolbars.BTN_ZOOM50:
            currentScale = 0.5;
            smartTransform(iv.svg.svgId, currentRotation);
            break;
        case toolbars.BTN_ZOOMIN:
            if (currentScale < 4) {
                currentScale = Math.floor(currentScale * 10) / 10 + iv.zoomDefault;
                smartTransform(iv.svg.svgId, currentRotation);
            }
            break;
        case toolbars.BTN_ZOOMOUT:
            if (currentScale > 0.1) {
                currentScale = Math.ceil(currentScale * 10) / 10 + iv.panDefault;
                smartTransform(iv.svg.svgId, currentRotation);
            }
            break;
        case toolbars.BTN_ROTATE_LEFT:
            onRotate(rotateType.LEFT);
            break;
        case toolbars.BTN_ROTATE_RIGHT:
            onRotate(rotateType.RIGHT);
            break;
        case toolbars.BTN_PRINTALL:
            printAll();
            break;
        case toolbars.BTN_PRINTIMAGE:
            printImage('<img src="' + iv.documentPath + '">');
            break;
        case toolbars.BTN_LAYER:
            layerPopup();
            break;
        case toolbars.BTN_COPY:
            onCopy();
            break;
        case toolbars.BTN_CUT:
            onCut();
            break;
        case toolbars.BTN_PASTE:
            onPaste();
            break;
        case toolbars.BTN_PROPERTIES:
            properties();
            break;
        case toolbars.BTN_OKPROPERTIES:
            okProperties();
            break;
        case toolbars.BTN_CANCELPROPERTIES:
            cancelProperties();
            break;

        case toolbars.BTN_TEXTPROPERTIES:
            textProperties();
            break;
        case toolbars.BTN_COMMENTPROPERTIES:
            commentProperties();
            break;
        case toolbars.BTN_CANCELCOMMENTPROPERTIES:
            cancelCommentProperties();
            break;
        case toolbars.BTN_LAST:
            last();
            break;
        case toolbars.BTN_NEXT:
            next();
            break;
        case toolbars.BTN_PREVIOUS:
            previous();
            break;
        case toolbars.BTN_FIRST:
            first();
            break;
        case toolbars.BTN_ADDLAYER:
            addLayer();
            break;
        case toolbars.BTN_REMOVELAYER:
            removeLayer();
            break;
        case toolbars.BTN_RENAMELAYER:
            renameLayer();
            break;
        case toolbars.BTN_ACTIVELAYER:
            activeLayer();
            break;
        case toolbars.BTN_DISPLAYLAYER:
            displayLayer();
            break;
        case toolbars.BTN_OKLAYER:
            okLayer();
            break;
        case toolbars.BTN_OKLAYERDELETE:
            okLayerDelete();
            break;
        case toolbars.BTN_GRAYSCALE:
            grayScale();
            break;
        case toolbars.BTN_OKGRAYSCALE:
            okGrayScale();
            break;
        case toolbars.BTN_CANCELGRAYSCALE:
            cancelGrayScale();
            break;
        case toolbars.BTN_CONTROLS:
            changeGrayScale();
            break;
        case toolbars.BTN_CONTROLS1:
            changeGrayScale();
            break;
        case toolbars.BTN_CBOX1:
            changeGrayScale();
            break;
        case toolbars.BTN_SAVEIMAGE:
            saveImage();
            break;
        case toolbars.BTN_SUBIMAGE:
            subImage();
            break;
    }
}

function subImage() {
    window.open(window.location.href + "&mode=1");
}

function saveImage() {
    saveAnnoToLayer();
    saveDocument(getResource("WS/saveImage"), window.sessionId, window.workspaceId, iv.currentDocument, function (data, res) {
        if (res == result.SUCCESS) {
             postImage(getResource("WS/saveAndCloseImageViewer?sessionId=" + window.sessionId+"&workspaceId="+ window.workspaceId+"&workItemId="+ window.workItemId+"&folderItemNo="+ window.folderItemNo), function (data, res) {
                 if (res == result.SUCCESS) {

            window.close();
                 }
             });
        }
        else{
            alert('Save unsuccessful!');
        }
    });
}

function cancelGrayScale() {
    var val;
    if (checkGrayscale) {
        val = (contrast + brightness) / 4;
        if (val > 50 || val < -50)
            return false;
    } else {
        val = 50;
    }
    $('#imageDrawing').css("backgroundColor", val > 0 ? 'white' : 'black');
    $('#imageDrawing').css("opacity", Math.abs(val / 100) * 2);
}

function okGrayScale() {
    brightness = tmpBrightness;
    contrast = tmpContrast;
    checkGrayscale = tmpCheckGrayscale;
}

function changeGrayScale() {
    tmpBrightness = brightness;
    tmpContrast = contrast;
    tmpCheckGrayscale = checkGrayscale;
    tmpContrast = parseInt($('#controls1').val());
    tmpBrightness = parseInt($('#controls').val());
    tmpCheckGrayscale = cbox1.checked;
    $('#brightnessValue').val(tmpBrightness);
    $('#contrastValue').val(tmpContrast);
    var val;
    if (tmpCheckGrayscale) {
        val = (tmpContrast + tmpBrightness) / 4;
        if (val > 50 || val < -50)
            return false;
    } else {
        val = 50;
    }
    $('#imageDrawing').css("backgroundColor", val > 0 ? 'white' : 'black');
    $('#imageDrawing').css("opacity", Math.abs(val / 100) * 2);
}

function grayScale() {
    $('#controls1').val(contrast);
    $('#controls').val(brightness);
    $('#contrastValue').val(contrast);
    $('#brightnessValue').val(brightness);
    if (checkGrayscale) {
        $("#cbox1").prop('checked', true);
    } else {
        $("#cbox1").prop('checked', false);
    }
    $("#myModal2").modal();
}

function okLayerDelete(){
    deleteLayerFlag = false;
    removeLayer();
}

function okLayer() {
    iv.cDocId = $("#newLayerBody tr").length;
    for (var j = 0; j < iv.cDocId; j++) {

        if (typeof iv.currentDocument.layers[j] == "undefined") {
            iv.currentDocument.layers[j] = $.extend(true, {}, iv.currentDocument.layers[j - 1]);
            iv.currentDocument.layers[j].annotations = "";
            iv.currentDocument.layers[j].layerNo = null;
        }

        if ($("#newLayerBody tr:eq(" + j + ") td:eq(0)")
                .text() == "") {
            $('.'+iv.currentDocument.layers[j].layerName).remove();
            iv.currentDocument.layers.splice(j, 1);
            $("#newLayerBody tr:eq(" + j + ") td:eq(0)").parent().remove();
            j--;
            iv.cDocId--;
        }
        else {
            $('.'+iv.currentDocument.layers[j].layerName).each(function () {
                $(this).attr("class",$("#newLayerBody tr:eq(" + j + ") td:eq(0)")
                    .text());
            });
            iv.currentDocument.layers[j].layerName = $("#newLayerBody tr:eq(" + j + ") td:eq(0)")
                .text();

            iv.currentDocument.layers[j].ownerId = $("#newLayerBody tr:eq(" + j + ") td:eq(1)")
                .text();
            iv.currentDocument.layers[j].layerDisplay = $("#newLayerBody tr:eq(" + j + ") td:eq(2) input").is(
                ':checked');
            iv.currentDocument.layers[j].lastUpdateTime = dateReformat($("#newLayerBody tr:eq(" + j + ") td:eq(4)").text());
            iv.currentDocument.layers[j].insertDateTime = dateReformat($("#newLayerBody tr:eq(" + j + ") td:eq(5)").text());
            if ($("#newLayerBody tr:eq(" + j + ") td:eq(6) input").is(
                    ':checked')) {
                iv.layerActive = iv.currentDocument.layers[j].layerName;
            }
            if ($("#newLayerBody tr:eq(" + j + ") td:eq(2) input").is(
                    ':checked')) {
                $('.' + iv.currentDocument.layers[j].layerName).each(function () {
                    if ($(this).parent().css("visibility") == "hidden") {

                    } else {
                        $(this).css("visibility", "visible");
                    }
                });
            } else {
                $('.' + iv.currentDocument.layers[j].layerName).css("visibility", "hidden");
            }
        }
    }
    unSelectMultipleSvg();
}

function displayLayer() {

    $(
        "input[type=checkbox][name=display][value="
        + $('#layerName').val() + "]").trigger(
        "click");


}

function activeLayer() {



                $('#layerActive').val($('#layerName').val());
        $(
                    "input[type=radio][name=active][value="
                    + $('#layerName').val() + "]").prop(
                    "checked", true).trigger("click");



}

function renameLayer() {

        var countRow = $("#newLayerBody tr").length;
        $(
            "input[type=radio][name=active][value="
            + $('#layerActive').val() + "]").attr(
            'value', $('#layerName').val());

        for (var j = 1; j <= countRow; j++) {
            if ($('#layerActive').val() == $('#layer' + j).text()) {

                $('#layer' + j).replaceWith('<input type="checkbox" name="layerName" value="' + $('#layerName').val() + '"' +
                    ' style="margin-top: 0px">' + $('#layerName').val());

                return $('#layerActive').val($('#layerName').val());


            }
        }

}

function removeLayer() {


    if($('.'+$('#layerName').val()).length>0&&deleteLayerFlag)
    {
        deleteLayerFlag = true;
        $('#myModalConfirm').modal();
    }
    else{
        var countRow = $("#newLayerBody tr").length;
        for (var j = 1; j <= countRow; j++) {
            if ($('#layerName').val() == $('#layer' + j).text()) {

                $('#layer' + j).text("");
                 $('#layer' + j).parent().parent().hide();
                if ($('input[name=active]:checked').val() == $(
                        '#layerName').val()) {
                    for (var k = 0; k <= countRow; k++) {
                        if ($("#newLayerBody tr:eq(" + k + ")").css('display') != 'none') {
                            $('#layerName').val($('#layer' + (k + 1)).text());
                            return $('#activeLayer').trigger('click');
                        }
                    }
                }
                }

            }
        }


}

function addLayer() {

        var countRow = $("#newLayerBody tr").length;
            var d = new Date();
            var currentDate = dateLayer(d);
            addNewLayer(countRow + 1, $('#layerName').val(),
                userInfo, true, currentDate, currentDate, false, templateLayerName);
            $('input[name=active]').change(function () {
                $('#layerActive').val($('input[name=active]:checked').val());
            });
        activeLayer();

}

function resetLayer() {
    // reset popup layer
    $("#newLayerBody").find("tr").remove();
}

//open layer popup
function layerPopup() {
    resetLayer();
    // re-add popup layer
    for (var k = 0; k < iv.cDocId; k++) {
        if(iv.currentDocument.layers[k].mgrTemplate!=null){
            templateLayerName = iv.currentDocument.layers[k].mgrTemplate.templateName;}
        addNewLayer(k + 1, iv.currentDocument.layers[k].layerName, iv.currentDocument.layers[k].ownerId, iv.currentDocument.layers[k].layerDisplay, iv.currentDocument.layers[k].lastUpdateTime, iv.currentDocument.layers[k].insertDateTime, iv.layerActive, templateLayerName);
    }
    // reset layer name
    $('#layerName').val($('input[name=active]:checked').val());
    $('#layerActive').val($('input[name=active]:checked').val());
    $(toolbars.BTN_ADDLAYER).css('pointer-events', 'none');
    $(toolbars.BTN_REMOVELAYER).css('pointer-events', 'none');
    $(toolbars.BTN_RENAMELAYER).css('pointer-events', 'none');
    $(toolbars.BTN_ACTIVELAYER).css('pointer-events', 'none');
    $(toolbars.BTN_DISPLAYLAYER).css('pointer-events', 'none');
    $('input[name=active]').change(function () {
        $('#layerActive').val($('input[name=active]:checked').val());
    });
    $("#myModal3").modal();
}

function first() {
    callSaveApi(1);
}

function next() {
    callSaveApi(iv.indexCurrent + 2);
}

function previous() {
    callSaveApi(iv.indexCurrent);
}

function last() {
    callSaveApi(iv.documentsLength);
}

function changeDocument() {
    iv.currentDocument = iv.documents[iv.cDocId - 1];
    iv.documentImage = getResource(jQuery.parseJSON(iv.currentDocument.viewInformation).Image);
    switch (iv.currentDocument.documentType) {
        case "1":
            displayImage();
            break;
        case "2":
            var url = iv.documentImage;
            $('body').append('<canvas id="the-canvas" ></canvas>');
            // The workerSrc property shall be specified.
            PDFJS.workerSrc = IV.url('resources/js/lib/pdf.worker.js');
            var pdfDoc = null,
                pageNum = iv.currentDocument.pageCount,
                pageRendering = false,
                pageNumPending = null,
                scale = 0.8,
                canvas = document.getElementById('the-canvas'),
                ctx = canvas.getContext('2d');


        /**
         * Get page info from document, resize canvas accordingly, and render page.
         * @param num Page number.
         */
        function renderPage(num) {
            pageRendering = true;
            // Using promise to fetch the page
            pdfDoc.getPage(num).then(function (page) {
                var viewport = page.getViewport(scale);
                canvas.height = viewport.height;
                canvas.width = viewport.width;

                // Render PDF page into ca0nvas context
                var renderContext = {
                    canvasContext: ctx,
                    viewport: viewport
                };
                var renderTask = page.render(renderContext);
                // Wait for rendering to finish
                renderTask.promise.then(function () {
                    iv.documentImage = canvas.toDataURL("image/png");
                    canvas.style.display = "none";
                    var img = new Image();
                    img.onload = function () {
                        iv.x2[0] = -parseInt(this.width);
                        iv.y2[0] = -parseInt(this.height);
                        iv.vis.append("image").attr("x", 0).attr("y", 0).attr("xlink:href", iv.documentImage).attr("id", "0").attr("transform", "rotate(0) scale(1)").attr("width", -iv.x2[0]).attr("height", -iv.y2[0]);
                        iv.load();
                        iv.switchImage();
                    }
                    img.src = iv.documentImage;
                    //vis.append("foreignObject").attr("id", "0").attr("transform","rotate(0) scale(1)");
                    //$('#0').append(canvas);

                    pageRendering = false;
                    if (pageNumPending !== null) {
                        // New page rendering is pending
                        renderPage(pageNumPending);
                        pageNumPending = null;
                    }
                });
            });
            // Update page counters
            //document.getElementById('page_num').textContent = pageNum;
        }

        /**
         * If another page rendering in progress, waits until the rendering is
         * finised. Otherwise, executes rendering immediately.
         */
        function queueRenderPage(num) {
            if (pageRendering) {
                pageNumPending = num;
            } else {
                renderPage(num);
            }
        }

        /**
         * Displays previous page.
         */
        function onPrevPage() {
            if (pageNum <= 1) {
                return;
            }
            pageNum--;
            queueRenderPage(pageNum);
        }

            $('body').append('<button type="button" class="btn btn-default" title="prev" id="prev"></button>');
            document.getElementById('prev').addEventListener('click', onPrevPage);

        /**
         * Displays next page.
         */
        function onNextPage() {
            if (pageNum >= pdfDoc.numPages) {
                return;
            }
            pageNum++;
            queueRenderPage(pageNum);
        }

            $('body').append('<button type="button" class="btn btn-default" title="next2" id="next2"></button>');
            document.getElementById('next2').addEventListener('click', onNextPage);

            /**
             * Asynchronously downloads PDF.
             */
            PDFJS.getDocument(url).then(function (pdfDoc_) {
                pdfDoc = pdfDoc_;
                //document.getElementById('page_count').textContent = pdfDoc.numPages;

                // Initial/first page rendering
                renderPage(pageNum);
            });
            break;
        case "3":
            $(function () {
                Tiff.initialize({
                    TOTAL_MEMORY: 16777216 * 10
                });
                var xhr = new XMLHttpRequest();
                xhr.open('GET', iv.documentImage);
                xhr.responseType = 'arraybuffer';
                xhr.onload = function (e) {
                    var buffer = xhr.response;
                    var tiff = new Tiff({
                        buffer: buffer
                    });
                    tiff.setDirectory(iv.currentDocument.pageCount - 1);
                    var canvas = tiff.toCanvas();
                    iv.documentImage = canvas.toDataURL("image/png");
                    var img = new Image();
                    img.onload = function () {
                        iv.x2[0] = -parseInt(this.width);
                        iv.y2[0] = -parseInt(this.height);
                        iv.vis.append("image").attr("x", 0).attr("y", 0).attr("xlink:href", iv.documentImage).attr("id", "0").attr("transform", "rotate(0) scale(1)").attr("width", -iv.x2[0]).attr("height", -iv.y2[0]);
                        iv.load();
                        iv.switchImage();
                    }
                    img.src = iv.documentImage;

//				vis.append("foreignObject").attr("id", "0").attr(
//						"transform", "rotate(0) scale(1)");
//				$('#0').append(canvas);			

                    return;

                };
                xhr.send();
            });
            break;
    }
    iv.layerCount = iv.currentDocument.layers.length;
    loadBefore();
}

function loadBefore() {
    // luu annotation voi anh truoc->xoa trang object, reset page
    $('#chooseImage').empty();
    $("#svg").empty();
}

// print include annotation
function printAll() {
    // reset rotate, scale, select before print
    var tmpScale = currentScale;
    var tmpRotate = currentRotation;
    currentRotation = 0;
    currentScale = 1;
    smartTransform(iv.svg.svgId, 0);
    unSelectMultipleSvg();

    // print then return rotate, scale
    printImage(document.getElementById("imageDrawing").innerHTML);
    currentRotation = tmpRotate;
    currentScale = tmpScale;
    smartTransform(iv.svg.svgId, currentRotation);
}

function printImage(image) {
    var windowContent = '<!DOCTYPE html>';
    windowContent += '<html>'
    windowContent += '<head><title>Print</title></head>';
    windowContent += '<body>'
    windowContent += image;
    windowContent += '</body>';
    windowContent += '</html>';
    var printWin = window.open('', '');
    printWin.document.open();
    printWin.document.write(windowContent);
    printWin.document.close();
    printWin.focus();
    printWin.print();
    printWin.close();
}

// button cancel of comment popup
// remove comment if cancel create new
function cancelCommentProperties() {
    if (selectedAnnos.length == 0) {
        $("#" + iv.drawAnno.node.id).remove();
        $('#SvgjsImage' + +(parseInt(iv.drawAnno.node.id.substr(-4))-1)).remove();
    }
}

// button ok of comment popup
function commentProperties() {
    // create new
    if (selectedAnnos.length == 0) {
        $('.' + commentExample.COMMENTCLASS).children().text($("#tbComment").val());
        $('.' + commentExample.COMMENTCLASS).attr('value', $('#commentTxtArea').val());

        // remove tmpClass and push to allAnnos object
        $('.' + commentExample.COMMENTCLASS).removeAttr("class");
        if (allAnnos.indexOf(iv.drawAnno.node.id) == -1) {
            allAnnos.push(iv.drawAnno.node.id.replace("Text", "Image").slice(0, 10) + (parseInt(iv.drawAnno.node.id.slice(9) - 1)));
            allAnnos.push(iv.drawAnno.node.id);
        }
    }// exist comment
    else {
        $('#' + selectedAnnos[1]).attr('value', $("#commentTxtArea").val());
        $('#' + selectedAnnos[1]).children().text($("#tbComment").val());
    }
}

// button ok of text popup
function textProperties() {
    // create new
    if (selectedAnnos.length == 0) {
        // properties of text
        $('#' + iv.drawAnno.node.id).children().text($(textExample.COMMENTTEXT).val());
        $("#" + iv.drawAnno.node.id).attr("fill", $('#color').find(":selected").text());
        $("#" + iv.drawAnno.node.id).attr("font-family", $('#font').find(":selected").text());

        $("#" + iv.drawAnno.node.id).attr("font-size", $('#fontSize').find(":selected").text());
        if ($(textExample.FONTSTYLEID).find(":selected").text() == "Regular") {
            $("#" + iv.drawAnno.node.id).attr("font-style", "");
            $("#" + iv.drawAnno.node.id).attr("font-weight", "");
        }
        if ($(textExample.FONTSTYLEID).find(":selected").text() == "Bold") {
            $("#" + iv.drawAnno.node.id).attr("font-style", "");
            $("#" + iv.drawAnno.node.id).attr("font-weight", "bold");
        }
        if ($(textExample.FONTSTYLEID).find(":selected").text() == "Italic") {
            $("#" + iv.drawAnno.node.id).attr("font-style", "italic");
            $("#" + iv.drawAnno.node.id).attr("font-weight", "");
        }
        if ($(textExample.FONTSTYLEID).find(":selected").text() == "Bold Italic") {
            $("#" + iv.drawAnno.node.id).attr("font-style", "italic");
            $("#" + iv.drawAnno.node.id).attr("font-weight", "bold");
        }

        // create rect around
        var ctx = document.getElementById(iv.svg.node.id)
            , textElm = ctx.getElementById(iv.drawAnno.node.id)
            , SVGRect = textElm.getBBox();
        if (allAnnos.indexOf(iv.drawAnno.node.id) == -1) {
            allAnnos.push("SvgjsRect"+SVG.did);
            allAnnos.push(iv.drawAnno.node.id);
        }
        var rect = document.createElementNS("http://www.w3.org/2000/svg", "rect");
        rect.setAttribute("id", "SvgjsRect"+SVG.did);
        SVG.did++;
        rect.setAttribute("class", "textDraw");
        rect.setAttribute("x", SVGRect.x);
        rect.setAttribute("y", SVGRect.y);
        rect.setAttribute("width", SVGRect.width);
        rect.setAttribute("height", SVGRect.height);
        rect.setAttribute("fill", $('#fill').find(":selected").text());
        rect.setAttribute("stroke", $('#border').find(":selected").text());
        ctx.insertBefore(rect, textElm);
    }// exist text
    else {
        // properties of text
        $('#' + selectedAnnos[1]).children().text($(textExample.COMMENTTEXT).val());
        $('#' + selectedAnnos[1]).attr("fill", $('#color').find(":selected").text());
        $('#' + selectedAnnos[1]).attr("font-family", $('#font').find(":selected").text());

        $('#' + selectedAnnos[1]).attr("font-size", $('#fontSize').find(":selected").text());
        if ($(textExample.FONTSTYLEID).find(":selected").text() == "Regular") {
            $('#' + selectedAnnos[1]).attr("font-style", "");
            $('#' + selectedAnnos[1]).attr("font-weight", "");
        }
        if ($(textExample.FONTSTYLEID).find(":selected").text() == "Bold") {
            $('#' + selectedAnnos[1]).attr("font-style", "");
            $('#' + selectedAnnos[1]).attr("font-weight", "bold");
        }
        if ($(textExample.FONTSTYLEID).find(":selected").text() == "Italic") {
            $('#' + selectedAnnos[1]).attr("font-style", "italic");
            $('#' + selectedAnnos[1]).attr("font-weight", "");
        }
        if ($(textExample.FONTSTYLEID).find(":selected").text() == "Bold Italic") {
            $('#' + selectedAnnos[1]).attr("font-style", "italic");
            $('#' + selectedAnnos[1]).attr("font-weight", "bold");
        }

        // set properties of exist rect around
        $('#' + selectedAnnos[0]).attr("fill", $('#fill').find(":selected").text());
        $('#' + selectedAnnos[0]).attr("stroke", $('#border').find(":selected").text());
    }
}

function cancelProperties(){

}

// button ok of default popup
function okProperties() {
    // not select, set properties variable
    if (selectedAnnos.length == 0) {
        propertiesType.penColor = $(textExample.INPUTCOLOR).val();
        propertiesType.fillColor = $(textExample.INPUTFILL).val();
        propertiesType.highlightColor = $(textExample.INPUTHIGHLIGHT).val();
        propertiesType.lineSize = $(textExample.WIDTH).val();
    } else {
        for(var j = 0; j < selectedAnnos.length; j++){
        // properties of line
        if ($('#' + selectedAnnos[j]).prop("tagName") == "line") {
            $('#' + selectedAnnos[j]).attr("stroke-width", $(textExample.WIDTH).val());
            $('#' + selectedAnnos[j]).attr("stroke", $(textExample.INPUTCOLOR).val());
        } else if ($('#' + selectedAnnos[j]).prop("tagName") == "rect") {
            // properties of highlight
            if ($('#' + selectedAnnos[j]).attr("fill-opacity") == "0.4") {
                $('#' + selectedAnnos[j]).attr("fill", $(textExample.INPUTHIGHLIGHT).val());
            }// properties of rect
            else {
                $('#' + selectedAnnos[j]).attr("stroke", $(textExample.INPUTCOLOR).val());
                $('#' + selectedAnnos[j]).attr("fill", $(textExample.INPUTFILL).val());
                $('#' + selectedAnnos[j]).attr("stroke-width", $(textExample.WIDTH).val());
            }
        }}
    }
}

// default properties popup
function modalLineProperties() {
    $("#" + propertiesType.penColor).prop("checked", true);
    $("#1" + propertiesType.fillColor).prop("checked", true);
    $("#2" + propertiesType.highlightColor).prop("checked", true);
    $(textExample.WIDTH).val(propertiesType.lineSize);
    $("#myModalLine").modal();
}

// button properties of toolbar, controller to correct popup
function properties() {
    switch (selectedAnnos.length) {
        case 1:
            // disable
            if(selectedAnnos[0].startsWith('SvgjsLine')){
                $('#li2').parent().addClass('disabled');
                $('#li3').parent().addClass('disabled');
                $('#li1').parent().removeClass('disabled');
                $('#width').removeClass('disabled');
                $('#li1').trigger('click');
            }
            else if(selectedAnnos[0].startsWith('SvgjsRect')){
                if ($('#' + selectedAnnos).attr("fill-opacity") == "0.4") {
                    $('#li1').parent().addClass('disabled');
                    $('#li2').parent().addClass('disabled');
                    $('#li3').parent().removeClass('disabled');
                    $('#width').addClass('disabled');
                    $('#li3').trigger('click');
                }// properties of rect
                else{
                $('#li1').parent().removeClass('disabled');
                $('#li2').parent().removeClass('disabled');
                    $('#li1').trigger('click');
                $('#li3').parent().addClass('disabled');
                    $('#width').removeClass('disabled');
                }
            }
            $("#" + $('#' + selectedAnnos).attr("stroke")).prop("checked", true);
            $(textExample.WIDTH).val($('#' + selectedAnnos).attr("stroke-width"));
            $("#1" + $('#' + selectedAnnos).attr("fill")).prop("checked", true);
            $("#2" + $('#' + selectedAnnos).attr("fill")).prop("checked", true);

            $("#myModalLine").modal();
            break;
        case 2:
            //cut xong paste thi rect lai trc text
            if ($('#' + selectedAnnos).attr('class')=='textDraw') {
                var textId = "#" + selectedAnnos[1];
                $(textExample.COMMENTTEXT).val($(textId).text());
                $(textExample.COMMENTTEXT).css("font-family", $(textId).attr("font-family"));
                $("#color").val(selectOptionByText("#color", $(textId).attr("fill")));
                $("#fill").val(selectOptionByText("#fill", $("#" + selectedAnnos).attr("fill")));
                $("#border").val(selectOptionByText("#border", $("#" + selectedAnnos).attr("stroke")));
                $("#font").val(selectOptionByText("#font", $(textId).attr("font-family")));
                $("#fontSize").val(selectOptionByText("#fontSize", $(textId).attr("font-size")));
                if ($(textId).attr("font-style") == "italic") {
                    if ($(textId).attr("font-weight") == "bold") {
                        $("#fontStyle").val("boldItalic");
                        $(textExample.COMMENTTEXT).css("font-style", "italic");
                        $(textExample.COMMENTTEXT).css("font-weight", "bold");
                    } else {
                        $("#fontStyle").val("italic");
                        $(textExample.COMMENTTEXT).css("font-style", "italic");
                        $(textExample.COMMENTTEXT).css("font-weight", "");
                    }
                } else {
                    if ($(textId).attr("font-weight") == "bold") {
                        $("#fontStyle").val("bold");
                        $(textExample.COMMENTTEXT).css("font-weight", "bold");
                        $(textExample.COMMENTTEXT).css("font-style", "");
                    } else {
                        $("#fontStyle").val("normal");
                        $(textExample.COMMENTTEXT).css("font-style", "");
                        $(textExample.COMMENTTEXT).css("font-weight", "");
                    }
                }
                $("#myModalText").modal();
                break;
            }
            else if ($('#' + selectedAnnos).attr('id').startsWith('SvgjsImage')) {
                $("#commentTxtArea").val($('#' + selectedAnnos[1]).attr('value'));
                $("#tbComment").val($('#' + selectedAnnos[1])[0].textContent);
                $("#myModalComment").modal();
                break;
            }
            modalLineProperties();
            break;
        default:
            $('#li1').parent().removeClass('disabled');
            $('#li2').parent().removeClass('disabled');
            $('#li3').parent().removeClass('disabled');
            $('#width').removeClass('disabled');
            $('#li1').trigger('click');
            modalLineProperties();
            break;
    }
}

// Create new annotation by selecting type of annotation on the screen.
function createSettingAnnotation(annoType) {

    switch (annoType) {
        case annoTypes.LINE:
            $(iv.svg.svgId).css('cursor', 'auto');
            iv.settingAnno = iv.svg.line().stroke(propertiesType.penColor).stroke({
                width: propertiesType.lineSize
            });
            break;
        case annoTypes.RECTANGLE:
            $(iv.svg.svgId).css('cursor', 'auto');
            iv.settingAnno = iv.svg.rect().stroke(propertiesType.penColor).fill(propertiesType.fillColor).stroke({
                width: propertiesType.lineSize
            });
            break;
        case annoTypes.TEXT:
            $(iv.svg.svgId).css('cursor', 'auto');
            iv.settingAnno = "text";
            break;
        case annoTypes.HIGHLIGHT:
            $(iv.svg.svgId).css('cursor', 'url(' + getResource(cursorImage) + '), auto');
            iv.settingAnno = iv.svg.rect().fill(propertiesType.highlightColor).fill({
                opacity: 0.4
            });
            break;
        case annoTypes.COMMENT:
            $(iv.svg.svgId).css('cursor', 'auto');
            iv.settingAnno = "comment";
            break;
    }

    // not set if text and comment
    if (iv.settingAnno != "text" && iv.settingAnno != "comment") {
        setAttribute(iv.settingAnno.node.id, "pointer-events", "all");
    }
}

// Process dynamically transform, automatically translate to start position
// after rotating.
function smartTransform(svgId, rotate) {
    var transform = getTransform(0, 0, rotate, currentScale);
    setTransform(svgId, transform);

    var cTop = $(svgId).position().top;
    var cLeft = $(svgId).position().left;

    var cTranslateLeft = iv.svgStartLeft - cLeft;
    var cTranslateTop = iv.svgStartTop - cTop;

    var transform = getTransform(cTranslateTop, cTranslateLeft, rotate, currentScale);
    setTransform(svgId, transform);
    $('#zoomInput').val(Math.floor(currentScale * 100));
}

// Check type of browser is IE or not.
function isMsiBrowser() {
    var ua = window.navigator.userAgent;
    var msie = ua.indexOf("MSIE ");

    // In case Browser is Internet explorer.
    if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./)) {
        return true;
    }
    return false;
}

// Create new transform, including: translate, rotate, scale.
function getTransform(translateTop, translateLeft, rotate, scale) {
    if (isMsiBrowser()) {
        return ( "transform",
        "translate(" + translateLeft + "px," + translateTop + "px)" + " scale(" + scale + "," + scale + ")" + " rotate(" + rotate + "deg)");
    } else {
        return ( "translate(" + translateLeft + "," + translateTop + ")" + " scale(" + scale + "," + scale + ")" + " rotate(" + rotate + ")");
    }
}

// Dynamically set transform for a svg element by id.
function setTransform(svgId, transform) {
    if (isMsiBrowser()) {
        $(svgId).css("transform-origin", "top left");
        $(svgId).css("transform", transform);
    } else {
        $(svgId).attr("transform-origin", "top left");
        $(svgId).attr("transform", transform);
    }
}

// This method to check intersection between a rectangle and a svg element.
function hasIntersection(tmpRect, svg) {
    if (svg == null || svg == "undefined") {
        return false;
    }
    var el = document.getElementById(svg.node.id);
    var childNodes = el.childNodes;
    var check = false;
    if (childNodes.length == 0) {
        return checkInterscetion(tmpRect, el);
    } else {
        for (var c in childNodes) {
            if (childNodes[c] instanceof SVGElement) {
                check = checkInterscetion(tmpRect, childNodes[c]);
                if (check) {
                    return check;
                }
            }

        }
    }
    return check;
}

function checkInterscetion(tmpRect, el) {
    var svgRoot = el.farthestViewportElement;
    var rect = svgRoot.createSVGRect();
    rect.x = tmpRect.x();
    rect.y = tmpRect.y();
    rect.height = tmpRect.height();
    rect.width = tmpRect.width();
    return svgRoot.checkIntersection(el, rect);
}

// This method to update drawFlg when click on the screen.
// In case point click in selected Annotations area(in selectedAnnos) => drawFlg
// = false, this means preparing for selecting, dragging or resizing.
// In case point click out of selected Annotations area(not in selectedAnnos) =>
// drawFlg = true, this means preparing for drawing.
function setDrawFlgWhenClick() {
    console.log("setDrawFlgWhenClick");
    for (var i in selectedAnnos) {
        var e = SVG.get(selectedAnnos[i]);
        if (hasIntersection(iv.sMultipleSvg, e)) {
            return false;
        }
    }

    if (clickOnSelectedBound) {
        return false;
    }
    return true;
}

// Dynamically set attribute for an element by id.
function setAttribute(id, attribute, value) {
    if (isMsiBrowser()) {
        $("#" + id).css(attribute, "" + value);
    } else {
        $("#" + id).attr(attribute, "" + value);
    }
}

// Clone tArr from sArr.
function cloneArray(tArr, sArr) {
    for (var i in sArr) {
        var e = SVG.get(sArr[i]).clone();
        e.hide();
        if (tArr.indexOf(e.node.id) == -1) {
            tArr.push(e.node.id);
        }
    }
}

// Copy tArr from sArr.
function copyArray(tArr, sArr) {
    for (var i in sArr) {
        if (tArr.indexOf(sArr[i]) == -1) {
            tArr.push(sArr[i]);
        }
    }
}

// Draw an rectangle to select multiple svg elements on imageviewer screen.
function createMultipleSelectRect(e) {
    iv.sMultipleSvg = iv.svg.rect().fill('none').stroke({
        width: 2
    });
    iv.sMultipleSvg.x(e.x);
    iv.sMultipleSvg.y(e.y);
}

// Clear all the contents in clipboard.
function resetClipBoard() {
    if (clipBoardAnnos.length > 0) {
        for (var i in clipBoardAnnos) {
            var e = SVG.get(clipBoardAnnos[i]);
            e.remove();
        }
        clipBoardAnnos = [];
    }
}

function resetDrawFlg() {
    console.log("resetDrawFlg");
    clickOnSelectedBound = true;
}
