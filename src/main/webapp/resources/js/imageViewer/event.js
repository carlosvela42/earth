var ctrlDown = false;
var CTRLKEY = 17;
var CMDKEY = 91;
var VKEY = 86;
var CKEY = 67;
var zoomDefault=0.1;
var panDefault=-0.1;

function onSelect(e) {
	console.log("onSelect!");
	this.front();
	iv.selectedAnno = this;
	select(iv.selectedAnno);
}

function onClick(e) {
	var id = "#" + $(this).attr('id');
	console.log("onClick:id=" + id);
	processClick(id);
}

function onCopy() {
	console.log("onCopy!");
	iv.clipBoardAnno = iv.selectedAnno.clone();
}

function onPaste() {
	console.log("onPaste!");
	if (iv.clipBoard != null) {
		pasteAnno = iv.clipBoardAnno.clone();
		pasteAnno.move(0,0);
	}
}

function onCut() {
	console.log("onCut!");
	iv.clipBoard = iv.selectedAnno.clone();
}

function onMouseDown(e) {
	console.log("onMouseDown!");
//	unSelect(iv.drawAnno);
	iv.drawAnno = iv.settingAnno.clone();
	iv.drawAnno.front();
	iv.drawAnno.draw(e);
}

function onMouseUp(e) {
	console.log("onMouseUp!");
	iv.drawAnno.draw('stop', e);
	iv.drawAnno.on("click", onSelect);
	if (allAnnos.indexOf(iv.drawAnno.node.id) == -1) {
		allAnnos.push(iv.drawAnno.node.id);
	}

//	iv.drawAnno.draggable();
//	select(iv.drawAnno);
}

function onRotate(type) {
	console.log("onRotate!");
	currentRotation = currentRotation + (90 * type);
	smartTransform(iv.svg.svgId);
	
	if (Math.abs(currentRotation) == 360) {
		currentRotation = 0;
	}
}

function onKeyDown(e) {
	e = e || window.event;
	var key = e.which || e.keyCode; // keyCode detection
	var ctrl = e.CTRLKEY ? e.CTRLKEY : ((key === CTRLKEY) ? true : false); // ctrl detection
	if (key == VKEY && ctrl) {
		console.log("Ctrl + V Pressed !");
		paste();
		return;
	} else if (key == CKEY && ctrl) {
		console.log("Ctrl + C Pressed !");
		copy();
		return;
	}
}

function select(element) {
	console.log("select!");
	element.draggable();
	if (element != null) {
//		element.selectize();
		element.draggable();
//		element.on("beforedrag", beforeDrag);
//		element.on("dragmove", dragMove);
//		element.draggable().on('beforedrag', beforeDrag);
//		element.draggable().on('dragmove', dragMove);
	}

}

function unSelect(element) {
	console.log("unSelect!");
	if (element != null)
		element.selectize(false);
//		element.draggable(false);
//		element.off("beforeDrag", beforeDrag);
//		element.off("dragMove", dragMove);
}

function processClick(id) {
	console.log("processClick:id=" + id);
	if (id==toolbars.BTN_SELECT) {
		iv.svg.off("mousedown", onMouseDown);
		iv.svg.off("mouseup", onMouseUp);
		return;
	} else if (id == annoTypes.LINE || id == annoTypes.RECTANGLE
			|| id == annoTypes.TEXT || id == annoTypes.HIGHLIGHT
			|| id == annoTypes.COMMENT || id == annoTypes.TESTLINE) {
		iv.svg.on("mousedown", onMouseDown, false);
		iv.svg.on("mouseup", onMouseUp, false);
		createSettingAnnotation(id);
		return;
	}
	
	switch(id) {
		case toolbars.BTN_ZOOMIN :
			zoomPan(zoomDefault);
			break;
		case toolbars.BTN_ZOOMOUT :
			if (currentScale > 0) {
				zoomPan(panDefault)
			}
			break;
		case toolbars.BTN_ROTATE_LEFT :
			onRotate(rotateType.LEFT);
			break;
		case toolbars.BTN_ROTATE_RIGHT :
			onRotate(rotateType.RIGHT);
			break;
	}
}

function createSettingAnnotation(annoType) {
	switch(annoType) {
		case annoTypes.LINE :
			iv.settingAnno = iv.svg.line().stroke({ width: 1 });
			break;
		case annoTypes.RECTANGLE :
			iv.settingAnno = iv.svg.rect().fill('none').stroke({ width: 1 });
			break;
		case annoTypes.TEXT :
			iv.settingAnno = iv.svg.text('This is just the start, ')
			break;
		case annoTypes.HIGHLIGHT :
			iv.settingAnno = iv.svg.rect().fill('#f06');
			break;
		case annoTypes.COMMENT :
			iv.settingAnno = iv.svg.rect().fill('#f06');
			break;
	}
}

function zoomPan(amount) {
	console.log("zoomPan");
	currentScale = Math.ceil10(currentScale * 10, -2) / 10 + amount;
	smartTransform(iv.svg.svgId);
}

function smartTransform(svgId) {
	var transform = getTransform(cTranslateTop, cTranslateLeft, currentRotation, currentScale);
	setTransform(iv.svg.svgId, transform);
	
	cTop = $(svgId).position().top;
	cLeft = $(svgId).position().left;
	
	iv.cTranslateLeft = iv.svgStartLeft-cLeft;
	iv.cTranslateTop = iv.svgStartTop-cTop;

	transform = getTransform(iv.cTranslateTop, iv.cTranslateLeft, currentRotation, currentScale);
	setTransform(iv.svg.svgId, transform);
}

function isMsiBrowser() {
	var ua = window.navigator.userAgent;
	var msie = ua.indexOf("MSIE ");

	// In case Browser is Internet explorer.
	if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./)) {
		return true;
	}
	return false;
}

function getTransform(translateTop, translateLeft, rotate, scale) {
	if (isMsiBrowser()) {
		return ("transform", "translate("+ translateLeft + "px," + translateTop + "px)" + " scale("+scale+"," + scale + ")" + " rotate(" + rotate + "deg)");
	} else {
		return ("translate("+ translateLeft + "," + translateTop + ")" + " scale("+scale+"," + scale + ")" + " rotate(" + rotate + ")");
	}
}

function setTransform(svgId, transform) {
	if (isMsiBrowser()) {
		$(iv.svg.svgId).css("transform-origin", "top left");
		$(svgId).css("transform", transform);
	} else {
		$(iv.svg.svgId).attr("transform-origin", "top left");
		$(svgId).attr("transform", transform);
	}
}

function beforeDrag(e) {
	console.log("beforeDrag");
	e.preventDefault();
}

function dragMove(e) {
	console.log("dragMove");
	e.preventDefault()
	this.move(e.detail.p.x, e.detail.p.y)
}