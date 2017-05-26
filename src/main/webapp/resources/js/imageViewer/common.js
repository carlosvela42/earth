var IV = function(){
	this.i = 4;
	this.anno = null; 
	this.subAnno = null; 
	this.x = null;
	this.y = null;
	this.rotate = 0;
	this.scale = 1;
	this.x1 = [];
	this.x2 = [];
	this.y1 = [];
	this.y2 = [];
	this.cut = -1;
	this.selectId = -1;
	this.cDocId = 1;
	this.layerCount = 0;
	this.layerName = [];
	this.layerOwner = [];
	this.layerDisplay = [];
	this.layerModified = [];
	this.layerCreated = [];
	this.mode = "select";
	this.layerActive = null;
	this.penColor = "black";
	this.fillColor = "none";
	this.highlightColor = "yellow";
	this.lineSize = 1;
	this.contrast = 1000;
	this.brightness = 0;
	this.checkGrayscale = true;
	this.existUser = false;
	this.anno = [];
	this.vis = null;
	this.documents = [];
	this.newContrast = 1.0;
	this.newBrightness = 0.0;
	this.filterChild = null;
	this.xGrayscale = null;
	this.container = null;
	this.pageNo = 0;
this.rotated = 0;
this.pasted = false;
	this.x1[0] = 0;
	this.y1[0] = 0;
	this.startAnnotation = function() {
		var x1 = this.x1;
		var x2 = this.x2;
		var y1 = this.y1;
		var y2 = this.y2;
		
		var gLength = $("g").length;
		for (var j = 0; j < gLength; j++) {
			if ($('g:eq( ' + j + ' )').attr("transform") != "translate(0 0)") {
				var index1 = $('g:eq( ' + j + ' )').attr("transform").indexOf("(");
				var index2 = $('g:eq( ' + j + ' )').attr("transform").indexOf(" ");
				var index3 = $('g:eq( ' + j + ' )').attr("transform").indexOf(")");
				var indexX = parseInt($('g:eq( ' + j + ' )').attr("transform")
						.slice(index1 + 1, index2))/ this.scale;
				var indexY = parseInt($('g:eq( ' + j + ' )').attr("transform")
						.slice(index2 + 1, index3))/ this.scale;
				var indexId = $('g:eq( ' + j + ' )').children().attr("id");
				switch (this.rotate) {
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

	this.redraw = function() {
		var x1 = this.x1;
		var x2 = this.x2;
		var y1 = this.y1;
		var y2 = this.y2;
		var rotate = this.rotate;
		var scale = this.scale;
		
		for (var k = 0; k <= this.i; k++) {
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
};
IV.prototype.rotatePoint = function(x1, y1, rotate) {
    var cosAlpha = Math.round(Math.cos((rotate / 180) * Math.PI));
    var sinAlpha = Math.round(Math.sin((rotate / 180) * Math.PI));
    var x2 = x1 * cosAlpha - y1 * sinAlpha;
    var y2 = x1 * sinAlpha + y1 * cosAlpha;
    return [x2, y2];
}

IV.prototype.rotateLine = function(x1, y1, x2, y2, rotate) {
    var rotate1 = rotatePoint(x1, y1, rotate);
    var rotate2 = rotatePoint(x2, y2, rotate);
    var x1_new = rotate1[0];
    var y1_new = rotate1[1];
    var x2_new = rotate2[0];
    var y2_new = rotate2[1];
    return [x1_new, y1_new, x2_new, y2_new];
}

IV.prototype.rotateRectangle = function(width, height, rotate) {
    var cosAlpha = Math.abs(Math.round(Math.cos((rotate / 180) * Math.PI)));
    var sinAlpha = Math.abs(Math.round(Math.sin((rotate / 180) * Math.PI)));
    var w = width * cosAlpha + height * sinAlpha;
    var h = width * sinAlpha + height * cosAlpha;
    return [w, h];
}
IV.url = function(url){
	return $("[name='contextPath']").data("context")+"/"+url;
}

