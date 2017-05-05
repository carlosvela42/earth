var selectedElement = 0;
var currentX = 0;
var currentY = 0;
var currentMatrix = 0;
function startMove(event, moveType) {
	mode = "move";
	x1m = event.clientX;
	y1m = event.clientY;
	document.documentElement.setAttribute("onmousemove", "moveIt(event)")

	if (moveType == 'single') {
		C = event.target;
	} else {
		C = event.target.parentNode;
	}
}

function moveIt(event) {
	translation = C.getAttributeNS(null, "transform").slice(10, -1).split(' ');
	sx = parseInt(translation[0]);
	sy = parseInt(translation[1]);

	C
			.setAttributeNS(null, "transform", "translate("
					+ (sx + event.clientX - x1m) + " "
					+ (sy + event.clientY - y1m) + ")");
	x1m = event.clientX;
	y1m = event.clientY;
}

function endMove() {
	document.documentElement.setAttributeNS(null, "onmousemove", null)
}