var zoomDefault = 0.1;
var panDefault = -0.1;
var waitMouseUpFlg = false;

var ctrlDown = false;
var CTRLKEY = 17;
var CMDKEY = 91;
var VKEY = 86;
var CKEY = 67;
var clickOnSelectedBound = false;

function onKeydown(e) {
    e = e || window.event;
    var key = e.which || e.keyCode;
    // keyCode detection
    var ctrlDown = e.CTRLKEY ? e.CTRLKEY : ((key === 17) ? true : false);
    // ctrl detection

    if (key == VKEY && ctrlDown) {
        console.log("Ctrl + V Pressed !");
        paste();
        return;
    } else if (key == CKEY && ctrlDown) {
        console.log("Ctrl + C Pressed !");
        copy();
        return;
    }
}

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
            //			smartTransform("#" + e.node.id, copyTimeRotation - currentRotation);
            SVG.get(selectedAnnos[0]).rotate(copyTimeRotation - currentRotation);
        } else {
            //			smartTransform("#" + iv.selectedGroup.node.id, copyTimeRotation - currentRotation);
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

    //	unSelectMultipleSvg();
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
            if (waitMouseUpFlg) {
                iv.sMultipleSvg.draw('cancel');
            } else {
                iv.sMultipleSvg.draw(e);
            }
        }
        iv.drawFlg = true;
    } else {
        unSelectMultipleSvg();
        //draw text then open text popup
        if (iv.settingAnno == "text") {
            iv.drawAnno = iv.svg.text("");
            iv.drawAnno.front();
            iv.drawAnno.draw(e);
            textPopup();
            //draw comment are draw image and draw text, then add text to class to get, then get user login to set, then reset comment text area, then open comment popup
        } else if (iv.settingAnno == "comment") {
            iv.drawAnno = iv.svg.text(userInfo).attr("class", "newComment");
            iv.drawAnno.front();
            iv.drawAnno.draw(e);
            iv.drawAnno = iv.svg.image(getResource(commentImage));
            iv.drawAnno.front();
            iv.drawAnno.draw(e);
            $(commentExample.COMMENTTEXTBOX).val(userInfo);

            $(commentExample.COMMENTTEXTAREA).val("");

            $(commentExample.COMMENTPOPUP).modal();

        } else {
            iv.drawAnno = iv.settingAnno.clone();
        }
        iv.drawAnno.attr("class", iv.layerActive);
        iv.drawAnno.front();
        iv.drawAnno.draw(e);
    }
    waitMouseUpFlg = true;
}

//text popup when draw
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

//get value of select option by text
function selectOptionByText(id, value) {
    return $(id + ' option').filter(function() {
        return $(this).html() == value;
    }).val();
}
function onMouseUp(e) {
    console.log("onMouseUp!");
    waitMouseUpFlg = false;
    if ((iv.toolbarFlg == toolbars.BTN_SELECT) || (iv.drawStartX == e.x && iv.drawStartY == e.y)) {
        if (iv.toolbarFlg == toolbars.BTN_SELECT) {
            if (iv.drawStartX == e.x && iv.drawStartY == e.y) {
                unSelectMultipleSvg();
                selectOneSvg();
            } else {
                selectMultipleSvg();
            }
            iv.sMultipleSvg.draw('cancel');
        } else {
            iv.drawAnno.draw('cancel');
        }
    } else {
        iv.drawAnno.draw('stop', e);
        if (allAnnos.indexOf(iv.drawAnno.node.id) == -1) {
            allAnnos.push(iv.drawAnno.node.id);
        }
        //		if (iv.drawAnno.node.id.startsWith('SvgjsLine')) {
        //			var lrect = iv.svg.rect(iv.drawAnno.attr("x2") - iv.drawAnno.attr("x1"), iv.drawAnno.attr("y2") - iv.drawAnno.attr("y1")).stroke(1)
        //							.fill("transparent").x(iv.drawAnno.attr("x1")).y(iv.drawAnno.attr("y1"));
        //			var lgroup = iv.svg.group().add(iv.drawAnno).add(lrect);
        //			
        //			if (allAnnos.indexOf(lgroup.node.id) == -1) {
        //				allAnnos.push(lgroup.node.id);
        //			}
        //		} else {
        //			if (allAnnos.indexOf(iv.drawAnno.node.id) == -1) {
        //				allAnnos.push(iv.drawAnno.node.id);
        //			}
        //		}
        //		iv.drawAnno.on("mousedown", function(e) {
        //			onMouseDownSvg(e);
        //		});
    }
}

//function onMouseDownSvg(e) {
//	console.log("svg element click down!");
////	unSelectMultipleSvg();
////	createMultipleSelectRect(e);
////	if (!waitMouseUpFlg) {
//		iv.drawStartX = e.x;
//		iv.drawStartY = e.y;
//		createMultipleSelectRect(e);
//		iv.drawFlg = setDrawFlgWhenClick();
//		if (iv.drawFlg) {
//			unSelectMultipleSvg();
//		}
////		waitMouseUpFlg = true;
////	}
//}

function selectMultipleSvg() {
    if (selectedFlg) {
        return;
    }
    console.log("selectMultipleSvg!");
    //	unSelectMultipleSvg();
    for (var i in allAnnos) {
        var e = SVG.get(allAnnos[i]);
        if (hasIntersection(iv.sMultipleSvg, e)) {
            if (e != null && (selectedAnnos.indexOf(allAnnos[i]) == -1)) {
                //process with select text or image then select all comment
                if (allAnnos[i].startsWith('SvgjsText')) {
                    if (typeof allAnnos[parseInt(i) + 1] != "undefined") {
                        if (allAnnos[parseInt(i) + 1].startsWith('SvgjsImage')) {
                            selectedAnnos.push(allAnnos[parseInt(i) + 1]);
                            i++;
                        }
                    }
                }

                selectedAnnos.push(allAnnos[i]);
                if (allAnnos[i].startsWith('SvgjsImage')) {
                    if (selectedAnnos[selectedAnnos.length - 1] != allAnnos[i - 1])
                        selectedAnnos.push(allAnnos[i - 1]);
                }

                //				iv.selectedGroup.add(e);
                //				iv.selectedGroup.selectize().resize().draggable();
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
    //	unSelectMultipleSvg();
    for (var i in allAnnos) {
        var e = SVG.get(allAnnos[i]);
        if (hasIntersection(iv.sMultipleSvg, e)) {
            var index = selectedAnnos.indexOf(allAnnos[i]);
            selectedAnnos = [];
            if (index == -1) {
                //process with select text object
                if (allAnnos[i].endsWith('R')) {
                    selectedAnnos.push(allAnnos[i]);
                    selectedAnnos.push(allAnnos[parseInt(i) + 1]);
                    displaySelectedAnnos();
                    allAnnos.splice(i, 2);
                    allAnnos.push(selectedAnnos[0]);
                    allAnnos.push(selectedAnnos[1]);
                    return;
                }
                //process with select image of comment
                if (allAnnos[i].startsWith('SvgjsImage')) {
                    selectedAnnos.push(allAnnos[i]);
                    selectedAnnos.push(allAnnos[i - 1]);
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
        //		iv.selectedGroup.add(e);
        //		iv.selectedGroup.selectize({deepSelect:true}).draggable(true);
        //		iv.selectedGroup.front();
        // Change selection priority of element.
        var index = allAnnos.indexOf(selectedAnnos[0]);
        allAnnos.splice(index, 1);
        allAnnos.push(selectedAnnos[0]);
    } else if (selectedAnnos.length > 1) {
        for (var i in selectedAnnos) {
            var e = SVG.get(selectedAnnos[i]);
            e.show();
            e.selectize(false, {
                deepSelect: true
            }).resize(false).draggable(false);
            iv.selectedGroup.add(e);
        }
        //		var selectedBounds = $(".svg_select_boundingRect").parent();
        iv.selectedGroup.selectize({
            deepSelect: true
        });
        iv.selectedGroup.draggable(true);
        iv.selectedGroup.front();
    }

    console.log("allAnnos.lenth=" + allAnnos.length);
    console.log("selectedAnnos.lenth=" + selectedAnnos.length);

    var boundingRects = [".svg_select_boundingRect", ".svg_select_points_lt", ".svg_select_points_rt", ".svg_select_points_rb", ".svg_select_points_lb", ".svg_select_points_t", ".svg_select_points_r", ".svg_select_points_b", ".svg_select_points_l", ".svg_select_points_rot", ".svg_select_points_point"];

    for (var c in boundingRects) {
        $(boundingRects[c]).mousedown(function() {
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
        //		iv.hiddenRect.remove();
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
        //		iv.selectedGroup.selectize(false);
        iv.selectedGroup.ungroup();
        iv.selectedGroup = iv.svg.group();
    } else if (selectedAnnos.length == 1) {
        var e = SVG.get(selectedAnnos[0]);
        e.selectize(false, {
            deepSelect: true
        }).resize(false).draggable(false);
    }
    //	iv.hiddenRect.remove();
    //	for (var i in selectedAnnos) {
    //		var e = SVG.get(selectedAnnos[i]);
    //		e.selectize(false).draggable(false).resize(false);
    //	}
    //	iv.selectedGroup.selectize(false,{});
    //	iv.selectedGroup.draggable(false,{});
    //	iv.selectedGroup.ungroup(iv.svg);
    //	iv.selectedGroup = iv.svg.group();	
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
        return;
    } else {
        iv.drawFlg = false;
    }

    switch (id) {
    case toolbars.BTN_ZOOMFP:
        currentScale = ($('#imageDrawing').width() - getScrollBarWidth()) / iv.imgWidth;
        zoomPan(0);
        break;

    case toolbars.BTN_ZOOMFW:
        currentScale = ($('#imageDrawing').width() - getScrollBarWidth()) / iv.imgWidth;
        zoomPan(0);
        break;
    case toolbars.BTN_ZOOM200:
        currentScale = 2;
        zoomPan(0);
        break;
    case toolbars.BTN_ZOOM100:
        currentScale = 1;
        zoomPan(0);
        break;
    case toolbars.BTN_ZOOM75:
        currentScale = 0.75;
        zoomPan(0);
        break;
    case toolbars.BTN_ZOOM50:
        currentScale = 0.5;
        zoomPan(0);
        break;
    case toolbars.BTN_ZOOMIN:
        zoomPan(zoomDefault);
        break;
    case toolbars.BTN_ZOOMOUT:
        if (currentScale > 0) {
            zoomPan(panDefault)
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
    }
}

function first() {}

function next() {}

function previous() {}

function last() {//    iv.swapBefore();
//    iv.cDocId = iv.documentsLength;
//    iv.changeDocument();
//    $('#first').prop('disabled', false);
//    $('#previous').prop('disabled', false);
//    $('#next').prop('disabled', true);
//    $('#last').prop('disabled', true);
//    $('#btnImage').text('#' + iv.cDocId);
}

//open layer popup
function layerPopup() {
    //reset popup layer
    for (var j = 1; j <= iv.cDocId; j++) {
        $("#layerBody .row:eq(1)").remove();
    }
    //re-add popup layer
    for (var k = 0; k < iv.cDocId; k++) {
        addNewLayer(k + 1, Document.layers[k].layerName, Document.layers[k].layerOwner, Document.layers[k].layerDisplay, Document.layers[k].layerModified, Document.layers[k].layerCreated, iv.layerActive);
    }
    //reset layer name
    $('#layerName').val("");
}

//print include annotation
function printAll() {
    //reset rotate, scale, select before print
    var tmpScale = currentScale;
    var tmpRotate = currentRotation;
    currentRotation = 0;
    currentScale = 1;
    smartTransform(iv.svg.svgId, 0);
    unSelectMultipleSvg();

    //print then return rotate, scale
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

//button cancel of comment popup
//remove comment if cancel create new
function cancelCommentProperties() {
    if (selectedAnnos.lenth == 0) {
        $("#" + iv.drawAnno.node.id).remove();
        $('.' + commentExample.COMMENTCLASS).remove();
    }
}

//button ok of comment popup
function commentProperties() {
    //create new
    if (selectedAnnos.length == 0) {
        $('.' + commentExample.COMMENTCLASS).children().text($("#tbComment").val());
        $('.' + commentExample.COMMENTCLASS).attr('value', $('#commentTxtArea').val());

        //remove tmpClass and push to allAnnos object
        $('.' + commentExample.COMMENTCLASS).removeAttr("class");
        if (allAnnos.indexOf(iv.drawAnno.node.id) == -1) {
            allAnnos.push(iv.drawAnno.node.id.replace("Image", "Text").slice(0, 9) + (iv.drawAnno.node.id.slice(10) - 2));
            allAnnos.push(iv.drawAnno.node.id);
        }
    }//exist comment
    else {
        $('#' + selectedAnnos[1]).attr('value', $("#commentTxtArea").val());
        $('#' + selectedAnnos[1]).children().text($("#tbComment").val());
    }
}

//button ok of text popup
function textProperties() {
    //create new
    if (selectedAnnos.length == 0) {
        //properties of text
        $("#" + iv.drawAnno.node.id).text($(textExample.COMMENTTEXT).val());
        $("#" + iv.drawAnno.node.id).attr("fill", $('#color').find(":selected").text());
        $("#" + iv.drawAnno.node.id).attr("font-family", $('#font').find(":selected").text());
        //bug font size: set text of line 539 to tspan, not delete tspan
        $("#" + iv.drawAnno.node.id).attr("fontSize", $('#fontSize').find(":selected").text());
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

        //create rect around
        var ctx = document.getElementById("SvgjsSvg1001")
          , textElm = ctx.getElementById(iv.drawAnno.node.id)
          , SVGRect = textElm.getBBox();
        if (allAnnos.indexOf(iv.drawAnno.node.id) == -1) {
            allAnnos.push(iv.drawAnno.node.id + "R");
            allAnnos.push(iv.drawAnno.node.id);
        }
        var rect = document.createElementNS("http://www.w3.org/2000/svg", "rect");
        rect.setAttribute("id", iv.drawAnno.node.id + "R");
        rect.setAttribute("x", SVGRect.x);
        rect.setAttribute("y", SVGRect.y);
        rect.setAttribute("width", SVGRect.width);
        rect.setAttribute("height", SVGRect.height);
        rect.setAttribute("fill", $('#fill').find(":selected").text());
        rect.setAttribute("stroke", $('#border').find(":selected").text());
        ctx.insertBefore(rect, textElm);
    }//exist text
    else {
        //properties of text
        $('#' + selectedAnnos[1][1]).text($(textExample.COMMENTTEXT).val());
        $('#' + selectedAnnos[1]).attr("fill", $('#color').find(":selected").text());
        $('#' + selectedAnnos[1]).attr("font-family", $('#font').find(":selected").text());
        //bug font size: set text of line 539 to tspan, not delete tspan
        $('#' + selectedAnnos[1]).attr("fontSize", $('#fontSize').find(":selected").text());
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

        //set properties of exist rect around
        $('#' + selectedAnnos[1] + 'R').attr("fill", $('#fill').find(":selected").text());
        $('#' + selectedAnnos[1] + 'R').attr("stroke", $('#border').find(":selected").text());
    }
}

//button ok of default popup
function okProperties() {
    //not select, set properties variable
    if (selectedAnnos.length == 0) {
        propertiesType.penColor = $(textExample.INPUTCOLOR).val();
        propertiesType.fillColor = $(textExample.INPUTFILL).val();
        propertiesType.highlightColor = $(textExample.INPUTHIGHLIGHT).val();
        propertiesType.lineSize = $(textExample.WIDTH).val();
    } else {
        //properties of line
        if ($('#' + selectedAnnos).prop("tagName") == "line") {
            $('#' + selectedAnnos).attr("stroke-width", $(textExample.WIDTH).val());
            $('#' + selectedAnnos).attr("stroke", $(textExample.INPUTCOLOR).val());
        } else if ($('#' + selectedAnnos).prop("tagName") == "rect") {
            //properties of highlight
            if ($('#' + selectedAnnos).attr("fill-opacity") == "0.4") {
                $('#' + selectedAnnos).attr("fill", $(textExample.INPUTHIGHLIGHT).val());
            }//properties of rect
            else {
                $('#' + selectedAnnos).attr("stroke", $(textExample.INPUTCOLOR).val());
                $('#' + selectedAnnos).attr("fill", $(textExample.INPUTFILL).val());
                $('#' + selectedAnnos).attr("stroke-width", $(textExample.WIDTH).val());
            }
        }
    }
}

//default properties popup
function modalLineProperties() {
    $("#" + propertiesType.penColor).prop("checked", true);
    $("#1" + propertiesType.fillColor).prop("checked", true);
    $("#2" + propertiesType.highlightColor).prop("checked", true);
    $(textExample.WIDTH).val(propertiesType.lineSize);
    $("#myModalLine").modal();
}

//button properties of toolbar, controller to correct popup
function properties() {
    switch (selectedAnnos.length) {
    case 1:
        // disable
        $("#" + $('#' + selectedAnnos).attr("stroke")).prop("checked", true);
        $(textExample.WIDTH).val($('#' + selectedAnnos).attr("stroke-width"));
        $("#1" + $('#' + selectedAnnos).attr("fill")).prop("checked", true);
        $("#2" + $('#' + selectedAnnos).attr("fill")).prop("checked", true);
        $("#myModalLine").modal();
        break;
    case 2:
        if ($('#' + selectedAnnos).attr('id').startsWith('SvgjsText')) {
            var textId = "#" + selectedAnnos[1];
            $(textExample.COMMENTTEXT).val($(textId).text());
            $(textExample.COMMENTTEXT).css("font-family", $(textId).attr("font-family"));
            $("#color").val(selectOptionByText("#color", $(textId).attr("fill")));
            $("#fill").val(selectOptionByText("#fill", $("#" + selectedAnnos).attr("fill")));
            $("#border").val(selectOptionByText("#border", $("#" + selectedAnnos).attr("stroke")));
            $("#font").val(selectOptionByText("#font", $(textId).attr("font-family")));
            $("#fontSize").val(selectOptionByText("#fontSize", $(textId).attr("fontsize")));
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
        if ($('#' + selectedAnnos).attr('id').startsWith('SvgjsImage')) {
            $("#commentTxtArea").val($('#' + selectedAnnos[1]).attr('value'));
            $("#tbComment").val($('#' + selectedAnnos[1])[0].textContent);
            $("#myModalComment").modal();
            break;
        }
        modalLineProperties();
        break;
    default:
        modalLineProperties();
        break;
    }
}

// Create new annotation by selecting type of annotation on the screen.
function createSettingAnnotation(annoType) {
    switch (annoType) {
    case annoTypes.LINE:
        iv.settingAnno = iv.svg.line().stroke(propertiesType.penColor).stroke({
            width: propertiesType.lineSize
        });
        break;
    case annoTypes.RECTANGLE:
        iv.settingAnno = iv.svg.rect().stroke(propertiesType.penColor).fill(propertiesType.fillColor).stroke({
            width: propertiesType.lineSize
        });
        break;
    case annoTypes.TEXT:
        iv.settingAnno = "text";
        break;
    case annoTypes.HIGHLIGHT:
        iv.settingAnno = iv.svg.rect().fill(propertiesType.highlightColor).fill({
            opacity: 0.4
        });
        break;
    case annoTypes.COMMENT:
        iv.settingAnno = "comment";
        break;
    }

    //not set if text and comment
    if (iv.settingAnno != "text" && iv.settingAnno != "comment") {
        setAttribute(iv.settingAnno.node.id, "pointer-events", "all");
    }
}

// Process zoomin or zoomout.
function zoomPan(amount) {
    console.log("zoomPan");
    currentScale = Math.ceil10(currentScale * 10, -2) / 10 + amount;
    smartTransform(iv.svg.svgId, currentRotation);
}

// Process dynamically transform, automatically translate to start position after rotating.
function smartTransform(svgId, rotate) {
    var transform = getTransform(0, 0, rotate, currentScale);
    setTransform(svgId, transform);

    var cTop = $(svgId).position().top;
    var cLeft = $(svgId).position().left;

    var cTranslateLeft = iv.svgStartLeft - cLeft;
    var cTranslateTop = iv.svgStartTop - cTop;

    var transform = getTransform(cTranslateTop, cTranslateLeft, rotate, currentScale);
    setTransform(svgId, transform);
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
        "translate(" + translateLeft + "px," + translateTop + "px)" + " scale(" + scale + "," + scale + ")" + " rotate(" + rotate + "deg)") ;
    } else {
        return ( "translate(" + translateLeft + "," + translateTop + ")" + " scale(" + scale + "," + scale + ")" + " rotate(" + rotate + ")") ;
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
            if (childNodes[c]instanceof SVGElement) {
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
// In case point click in selected Annotations area(in selectedAnnos) => drawFlg = false, this means preparing for selecting, dragging or resizing.
// In case point click out of selected Annotations area(not in selectedAnnos) => drawFlg = true, this means preparing for drawing.
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
