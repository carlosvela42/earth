var i = 4;
var anno, subAnno, x, y;
var rotate = 0;
var scale = 1;
var x1 = [];
var x2 = [];
var y1 = [];
var y2 = [];
var cut = -1;
var selectId = -1;
var layerCount = jsonLayer.Layer.length;
var layerName = [];
var layerOwner = [];
var layerDisplay = [];
var layerModified = [];
var layerCreated = [];
var mode = "select";
var layerActive;
var penColor = "black";
var fillColor = "none";
var highlightColor = "yellow";
var lineSize = 1;
var contrast = 100;
var brightness = 100;
var checkGrayscale = true;
var existUser = false;

x1[0] = 0;
y1[0] = 0;
function startAnnotation() {
	var gLength = $("g").length;
	for (var j = 0; j < gLength; j++) {
		if ($('g:eq( ' + j + ' )').attr("transform") != "translate(0 0)") {
			var index1 = $('g:eq( ' + j + ' )').attr("transform").indexOf("(");
			var index2 = $('g:eq( ' + j + ' )').attr("transform").indexOf(" ");
			var index3 = $('g:eq( ' + j + ' )').attr("transform").indexOf(")");
			var indexX = parseInt($('g:eq( ' + j + ' )').attr("transform")
					.slice(index1 + 1, index2))
					/ scale;
			var indexY = parseInt($('g:eq( ' + j + ' )').attr("transf" + "orm")
					.slice(index2 + 1, index3))
					/ scale;
			var indexId = $('g:eq( ' + j + ' )').children().attr("id");
			switch (rotate) {
			case 0:
				x1[indexId] -= indexY;
				y1[indexId] += indexX;
				if ($('g:eq( ' + j + ' )').children().prop("tagName") == "line") {
					x2[indexId] -= indexY;
					y2[indexId] += indexX;
				}
				if ($('g:eq( ' + j + ' )').children().eq(1).attr("id")) {
					for (var k = 1; k <= 4; k++) {
						x1[k] -= indexY;
						y1[k] += indexX;
					}
				}
				break;
			case 90:
				x1[indexId] += indexX;
				y1[indexId] += indexY;
				if ($('g:eq( ' + j + ' )').children().prop("tagName") == "line") {
					x2[indexId] += indexX;
					y2[indexId] += indexY;
				}
				if ($('g:eq( ' + j + ' )').children().eq(1).attr("id")) {
					for (var k = 1; k <= 4; k++) {
						x1[k] += indexX;
						y1[k] += indexY;
					}
				}
				break;
			case 180:
				x1[indexId] += indexY;
				y1[indexId] -= indexX;
				if ($('g:eq( ' + j + ' )').children().prop("tagName") == "line") {
					x2[indexId] += indexY;
					y2[indexId] -= indexX;
				}
				if ($('g:eq( ' + j + ' )').children().eq(1).attr("id")) {
					for (var k = 1; k <= 4; k++) {
						x1[k] += indexY;
						y1[k] -= indexX;
					}
				}
				break;
			case 270:
				x1[indexId] -= indexX;
				y1[indexId] -= indexY;
				if ($('g:eq( ' + j + ' )').children().prop("tagName") == "line") {
					x2[indexId] -= indexX;
					y2[indexId] -= indexY;
				}
				if ($('g:eq( ' + j + ' )').children().eq(1).attr("id")) {
					for (var k = 1; k <= 4; k++) {
						x1[k] -= indexX;
						y1[k] -= indexY;
					}
				}
				break;
			}
		}
		;
	}
	$('g').attr("transform", "translate(0 0)");
}

function redraw() {
	for (var k = 0; k <= i; k++) {
		var transform = $('#' + k).attr("transform");
		if (transform != null) {
			var tempX,tempY;
			switch ($("#" + k).prop("tagName")) {
			case "line":
				switch (rotate) {
				case 0:
					$('#' + k).attr("transform",
							"rotate(0) scale(" + scale + ")");
					$('#' + k).attr("x1", x1[k]);
					$('#' + k).attr("x2", x2[k]);
					$('#' + k).attr("y1", y1[k]);
					$('#' + k).attr("y2", y2[k]);
					$('#r' + k).attr("transform",
							"rotate(0) scale(" + scale + ")");
					if(x2[k]>=x1[k]){ tempX = x1[k];}else{tempX = x2[k];}
					if(y2[k]>=y1[k]){ tempY = y1[k];}else{tempY = y2[k];}
					$('#r' + k).attr("x", tempX);
					$('#r' + k).attr("y", tempY);
					break;
				case 90:
					$('#' + k).attr("transform",
							"rotate(90) scale(" + scale + ")");
					$('#' + k).attr("x1", x1[k]);
					$('#' + k).attr("x2", x2[k]);
					$('#' + k).attr("y1", y1[k] + y2[0]);
					$('#' + k).attr("y2", y2[k] + y2[0]);
					$('#r' + k).attr("transform",
							"rotate(90) scale(" + scale + ")");
					if(x2[k]>=x1[k]){ tempX = x1[k];}else{tempX = x2[k];}
					if(y2[k]>=y1[k]){ tempY = y1[k];}else{tempY = y2[k];}
					$('#r' + k).attr("x", tempX);
					$('#r' + k).attr("y", tempY+ y2[0]);
					break;
				case 180:
					$('#' + k).attr("transform",
							"rotate(180) scale(" + scale + ")");
					$('#' + k).attr("x1", x1[k] + x2[0]);
					$('#' + k).attr("x2", x2[k] + x2[0]);
					$('#' + k).attr("y1", y1[k] + y2[0]);
					$('#' + k).attr("y2", y2[k] + y2[0]);
					$('#r' + k).attr("transform",
							"rotate(180) scale(" + scale + ")");
					if(x2[k]>=x1[k]){ tempX = x1[k];}else{tempX = x2[k];}
					if(y2[k]>=y1[k]){ tempY = y1[k];}else{tempY = y2[k];}
					$('#r' + k).attr("x", tempX+ x2[0]);
					$('#r' + k).attr("y", tempY+ y2[0]);
					break;
				case 270:
					$('#' + k).attr("transform",
							"rotate(270) scale(" + scale + ")");
					$('#' + k).attr("x1", x1[k] + x2[0]);
					$('#' + k).attr("x2", x2[k] + x2[0]);
					$('#' + k).attr("y1", y1[k]);
					$('#' + k).attr("y2", y2[k]);
					$('#r' + k).attr("transform",
							"rotate(270) scale(" + scale + ")");
					if(x2[k]>=x1[k]){ tempX = x1[k];}else{tempX = x2[k];}
					if(y2[k]>=y1[k]){ tempY = y1[k];}else{tempY = y2[k];}
					$('#r' + k).attr("x", tempX+ x2[0]);
					$('#r' + k).attr("y", tempY);
					break;
				}
				break;
			case "rect":
			case "image":
			case "foreignObject":
				switch (rotate) {
				case 0:
					$('#' + k).attr("transform",
							"rotate(0) scale(" + scale + ")");
					$('#' + k).attr("x", x1[k]);
					$('#' + k).attr("y", y1[k]);
					break;
				case 90:
					$('#' + k).attr("transform",
							"rotate(90) scale(" + scale + ")");
					$('#' + k).attr("x", x1[k]);
					$('#' + k).attr("y", y1[k] + y2[0]);
					break;
				case 180:
					$('#' + k).attr("transform",
							"rotate(180) scale(" + scale + ")");
					$('#' + k).attr("x", x1[k] + x2[0]);
					$('#' + k).attr("y", y1[k] + y2[0]);
					break;
				case 270:
					$('#' + k).attr("transform",
							"rotate(270) scale(" + scale + ")");
					$('#' + k).attr("x", x1[k] + x2[0]);
					$('#' + k).attr("y", y1[k]);
					break;
				}
				break;
			}
		}
	}
	if (rotate == 0 || rotate == 180) {
		$('#svg').attr("width", -x2[0] * scale);
		$('#svg').attr("height", -y2[0] * scale);
	} else {
		$('#svg').attr("width", -y2[0] * scale);
		$('#svg').attr("height", -x2[0] * scale);
	}
}
