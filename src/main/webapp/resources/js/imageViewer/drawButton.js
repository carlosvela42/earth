$("#line").click(
		function() {
			var src = $("#imgDraw").attr("src").slice(0,$("#imgDraw").attr("src").lastIndexOf("/") + 1);
			$("#imgDraw").attr("src", src + "line.png");
		});
$("#rectangle").click(
		function() {
			var src = $("#imgDraw").attr("src").slice(0,
					$("#imgDraw").attr("src").lastIndexOf("/") + 1);
			$("#imgDraw").attr("src", src + "rectangle.png");
		});
$("#comment").click(function() {	
	var src = $("#imgDraw").attr("src").slice(0,
			$("#imgDraw").attr("src").lastIndexOf("/") + 1);
	$("#imgDraw").attr("src", src + "commentIcon.jpg");
});
$("#highlight").click(function() {
	var src = $("#imgDraw").attr("src").slice(0,
			$("#imgDraw").attr("src").lastIndexOf("/") + 1);
	$("#imgDraw").attr("src", src + "highlight.jpg");
});
$("#text").click(function() {
	var src = $("#imgDraw").attr("src").slice(0,
			$("#imgDraw").attr("src").lastIndexOf("/") + 1);
	$("#imgDraw").attr("src", src + "text.png");
});
IV.prototype.mousedown = function () {	
	var m = d3.mouse(this);
	i = parseInt(imageViewer.i);
	x = m[0];
	y = m[1];
	var vis = imageViewer.vis;
	var penColor = imageViewer.penColor;
	var fillColor = imageViewer.fillColor;
	var highlightColor = imageViewer.highlightColor;
	var lineSize = imageViewer.lineSize;
	var scale = imageViewer.scale;
	var selectClick = imageViewer.selectClick;
	var layerActive = imageViewer.layerActive;
	
	if ($("#testLine").prop("checked")) {
		i++;		
		anno = vis.append("line").attr("x1", x).attr("y1", y).attr("x2", x)
				.attr("y2", y).attr("id", i).attr("stroke", penColor).attr(
						"stroke-width", scale).attr("class", layerActive).on(
								"click", selectClick);
		vis.on("mousemove", mousemove);
	}
	if ($("#line").prop("checked")) {
		i++;
		anno = vis.append("line").attr("x1", x).attr("y1", y).attr("x2", x)
				.attr("y2", y).attr("id", i).attr("stroke", penColor).attr(
						"stroke-width", scale).attr("class", layerActive).on(
						"click", selectClick);
		vis.on("mousemove", mousemove);
	}
	if ($("#rectangle").prop("checked")) {
		i++;
		anno = vis.append("rect").attr("x", x).attr("y", y).attr("width", 0)
				.attr("height", 0).attr("id", i).attr("fill", fillColor).attr(
						"stroke", penColor).attr("stroke-width",
						scale * lineSize).attr("class", layerActive).on(
						"click", selectClick).attr("pointer-events", "all");
		// fill none
		vis.on("mousemove", mousemove);
	}
	if ($("#text").prop("checked")) {
		i++;
		anno = vis.append("foreignObject").attr("x", x).attr("y", y).attr(
				"width", 0).attr("height", 0).attr("id", i).attr("class",
				"text " + layerActive).on("click", selectClick);
		$('#' + i).append('<textarea id="t' + i + '"/>');
		$("#t" + i).css("color", penColor);
		$("#t" + i).css("backgroundColor", "transparent");
		$("#t" + i).css("border", "transparent");
		$("#t" + i).css("font-family", "Times New Roman");
		$("#t" + i).css("fontSize", 8);
		$("#t" + i).css("font-style", "normal");
		$("#t" + i).css("width", 0);
		$("#t" + i).css("height", 0);
		vis.on("mousemove", mousemove);
	}
	if ($("#highlight").prop("checked")) {
		i++;
		anno = vis.append("rect").attr("x", x).attr("y", y).attr("width", 0)
				.attr("height", 0).attr("fill", highlightColor).attr("stroke",
						"none").attr("fill-opacity", "0.4").attr("class",
						layerActive).on("click", selectClick).attr("id", i);
		vis.on("mousemove", mousemove);
	}
	if ($("#comment").prop("checked")) {
		i++;
		anno = vis.append("foreignObject").attr("x", x).attr("y", y).attr(
				"width", 0).attr("height", 0).attr("id", i).attr("class",
				layerActive).on("click", selectClick);
		$('#' + i).append(
				'<img id="i' + i
						+ '" src="resources/images/imageViewer/comment.jpg"/>');
		var userInfo = $('#userInfo').text();
		$('#' + i).append(
				'<textarea id="t' + i + '">' + userInfo.slice(0,userInfo.indexOf("/")) + '</textarea>');
		$("#t" + i).css("width", 0);
		$("#t" + i).css("height", 0);
		$("#i" + i).css("width", 0);
		$("#i" + i).css("height", 0);
		vis.on("mousemove", mousemove);
	}
	// if ($("#drawSelect").prop("checked") && mode == "select") {
	// i++;
	// anno = vis.append("rect").attr("x", x).attr("y", y).attr("width", 0)
	// .attr("height", 0).attr("fill", "none").attr("stroke", "black")
	// .attr("id", i);
	// vis.on("mousemove", mousemove);
	// }
}

function mousemove() {
	var m = d3.mouse(this);	
	if (0 <= m[0] <= $('#svg').attr('width')
			&& 0 <= m[1] <= $('#svg').attr('height')) {
		if ($("#line").prop("checked")) {
			anno.attr("x2", m[0]).attr("y2", m[1]);
		}
		if ($("#testLine").prop("checked")) {
			anno.attr("x2", m[0]).attr("y2", m[1]);
		}
		if ($("#rectangle").prop("checked") || $("#highlight").prop("checked")) {
			if (m[1] - y < 0)
				anno.attr("y", m[1]);
			if (m[0] - x < 0)
				anno.attr("x", m[0]);
			anno.attr("width", Math.abs(m[0] - x));
			anno.attr("height", Math.abs(m[1] - y));
		}
		if ($("#text").prop("checked")) {
			if (m[1] - y < 0) {
				anno.attr("y", m[1]);
			}
			if (m[0] - x < 0) {
				anno.attr("x", m[0]);
			}
			anno.attr("width", Math.abs(m[0] - x));
			anno.attr("height", Math.abs(m[1] - y));
			$("#t" + i).css("width", Math.abs(m[0] - x));
			$("#t" + i).css("height", Math.abs(m[1] - y));
		}
		if ($("#comment").prop("checked")) {
			if (m[1] - y < 0) {
				anno.attr("y", m[1]);
			}
			if (m[0] - x < 0) {
				anno.attr("x", m[0]);
			}
			anno.attr("width", Math.abs(m[0] - x));
			anno.attr("height", Math.abs(m[1] - y));
			$("#t" + i).css("width", Math.abs(m[0] - x));
			$("#t" + i).css("height", Math.abs(m[1] - y) / 2);
			$("#i" + i).css("width", Math.abs(m[0] - x));
			$("#i" + i).css("height", Math.abs(m[1] - y) / 2);
		}
	}
}

function mouseup() {
	var vis = imageViewer.vis;
	var scale = imageViewer.scale;
	var x1 = imageViewer.x1;
	var x2 = imageViewer.x2;
	var y1 = imageViewer.y1;
	var y2 = imageViewer.y2;
	var lineSize = imageViewer.lineSize;
	var rotate = imageViewer.rotate;
	var selectClick = imageViewer.selectClick;
	var i = imageViewer.i;
	vis.on("mousemove", null);	
	$('#' + i).attr("transform", "rotate(" + rotate + ") scale(" + scale + ")");
	if ($("#testLine").prop("checked")) {
		if (parseInt($('#' + i).attr("x1")) == parseInt($('#' + i).attr("x2"))
				&& parseInt($('#' + i).attr("y1")) == parseInt($('#' + i).attr(
						"y2"))) {
			$("#" + i).remove();
			i--;
			if ($("#" + i).attr("transform")) {
				return;
			} else {
				$("#" + i).attr("transform",
						"rotate(" + rotate + ") scale(" + scale + ")");
			}
		}
		x1[i] = parseInt($('#' + i).attr("x1")) / scale;
		x2[i] = parseInt($('#' + i).attr("x2")) / scale;
		y1[i] = parseInt($('#' + i).attr("y1")) / scale;
		y2[i] = parseInt($('#' + i).attr("y2")) / scale;
		$('#' + i).attr("stroke-width", lineSize);
		var tempX,tempY;
		switch (rotate) {
		case 0:			
			$('#' + i).attr("x1", x1[i]);
			$('#' + i).attr("x2", x2[i]);
			$('#' + i).attr("y1", y1[i]);
			$('#' + i).attr("y2", y2[i]);
			if(x2[i]>=x1[i]){ tempX = x1[i];}else{tempX = x2[i];}
			if(y2[i]>=y1[i]){ tempY = y1[i];}else{tempY = y2[i];}
			vis.append("rect").attr("x", tempX).attr("y", tempY).attr("width", Math.abs(x2[i]-x1[i]))
			.attr("height", Math.abs(y2[i]-y1[i])).attr("id", "r"+i).attr("fill", "none").on(
					"click", selectClick).attr("pointer-events", "all").attr("transform",
							"rotate(" + rotate + ") scale(" + scale + ")");
			break;
		case 90:
			$('#' + i).attr("x1", y1[i]);
			$('#' + i).attr("x2", y2[i]);
			$('#' + i).attr("y1", -x1[i]);
			$('#' + i).attr("y2", -x2[i]);
			if(x2[i]>=x1[i]){ tempX = x2[i];}else{tempX = x1[i];}
			if(y2[i]>=y1[i]){ tempY = y1[i];}else{tempY = y2[i];}
			vis.append("rect").attr("x", tempY).attr("y", -tempX).attr("width", Math.abs(y2[i]-y1[i]))
			.attr("height", Math.abs(x2[i]-x1[i])).attr("id", "r"+i).attr("fill", "none").on(
					"click", selectClick).attr("pointer-events", "all").attr("transform",
							"rotate(" + rotate + ") scale(" + scale + ")");
			break;
		case 180:
			$('#' + i).attr("x1", -x1[i]);
			$('#' + i).attr("x2", -x2[i]);
			$('#' + i).attr("y1", -y1[i]);
			$('#' + i).attr("y2", -y2[i]);
			if(x2[i]>=x1[i]){ tempX = x2[i];}else{tempX = x1[i];}
			if(y2[i]>=y1[i]){ tempY = y2[i];}else{tempY = y1[i];}
			vis.append("rect").attr("x", -tempX).attr("y", -tempY).attr("width", Math.abs(x2[i]-x1[i]))
			.attr("height", Math.abs(y2[i]-y1[i])).attr("id", "r"+i).attr("fill", "none").on(
					"click", selectClick).attr("pointer-events", "all").attr("transform",
							"rotate(" + rotate + ") scale(" + scale + ")");
			break;
		case 270:
			$('#' + i).attr("x1", -y1[i]);
			$('#' + i).attr("x2", -y2[i]);
			$('#' + i).attr("y1", x1[i]);
			$('#' + i).attr("y2", x2[i]);
			if(x2[i]>=x1[i]){ tempX = x1[i];}else{tempX = x2[i];}
			if(y2[i]>=y1[i]){ tempY = y2[i];}else{tempY = y1[i];}
			vis.append("rect").attr("x", -tempY).attr("y", tempX).attr("width", Math.abs(y2[i]-y1[i]))
			.attr("height", Math.abs(x2[i]-x1[i])).attr("id", "r"+i).attr("fill", "none").on(
					"click", selectClick).attr("pointer-events", "all").attr("transform",
							"rotate(" + rotate + ") scale(" + scale + ")");
			break;
		}
		x1[i] = parseInt($('#' + i).attr("x1"));
		x2[i] = parseInt($('#' + i).attr("x2"));
		y1[i] = parseInt($('#' + i).attr("y1"));
		y2[i] = parseInt($('#' + i).attr("y2"));
		switch (rotate) {
		case 90:
			y1[i] -= y2[0];
			y2[i] -= y2[0];
			break;
		case 180:
			x1[i] -= x2[0];
			x2[i] -= x2[0];
			y1[i] -= y2[0];
			y2[i] -= y2[0];
			break;
		case 270:
			x1[i] -= x2[0];
			x2[i] -= x2[0];
			break;
		}
		// $('#' + i).wrap('<foreignObject x="" y="361" width="87" height="118" id="11" class="text DEF0" transform="rotate(0) scale(1)"></foreignObject>');
	}
	if ($("#line").prop("checked")) {
		if (parseInt($('#' + i).attr("x1")) == parseInt($('#' + i).attr("x2"))
				&& parseInt($('#' + i).attr("y1")) == parseInt($('#' + i).attr(
						"y2"))) {
			$("#" + i).remove();
			i--;
			if ($("#" + i).attr("transform")) {
				return;
			} else {
				$("#" + i).attr("transform",
						"rotate(" + rotate + ") scale(" + scale + ")");
			}
		}
		x1[i] = parseInt($('#' + i).attr("x1")) / scale;
		x2[i] = parseInt($('#' + i).attr("x2")) / scale;
		y1[i] = parseInt($('#' + i).attr("y1")) / scale;
		y2[i] = parseInt($('#' + i).attr("y2")) / scale;
		$('#' + i).attr("stroke-width", lineSize);
		switch (rotate) {
		case 0:
			$('#' + i).attr("x1", x1[i]);
			$('#' + i).attr("x2", x2[i]);
			$('#' + i).attr("y1", y1[i]);
			$('#' + i).attr("y2", y2[i]);
			break;
		case 90:
			$('#' + i).attr("x1", y1[i]);
			$('#' + i).attr("x2", y2[i]);
			$('#' + i).attr("y1", -x1[i]);
			$('#' + i).attr("y2", -x2[i]);
			break;
		case 180:
			$('#' + i).attr("x1", -x1[i]);
			$('#' + i).attr("x2", -x2[i]);
			$('#' + i).attr("y1", -y1[i]);
			$('#' + i).attr("y2", -y2[i]);
			break;
		case 270:
			$('#' + i).attr("x1", -y1[i]);
			$('#' + i).attr("x2", -y2[i]);
			$('#' + i).attr("y1", x1[i]);
			$('#' + i).attr("y2", x2[i]);
			break;
		}
		x1[i] = parseInt($('#' + i).attr("x1"));
		x2[i] = parseInt($('#' + i).attr("x2"));
		y1[i] = parseInt($('#' + i).attr("y1"));
		y2[i] = parseInt($('#' + i).attr("y2"));
		switch (rotate) {
		case 90:
			y1[i] -= y2[0];
			y2[i] -= y2[0];
			break;
		case 180:
			x1[i] -= x2[0];
			x2[i] -= x2[0];
			y1[i] -= y2[0];
			y2[i] -= y2[0];
			break;
		case 270:
			x1[i] -= x2[0];
			x2[i] -= x2[0];
			break;
		}
		// $('#' + i).wrap('<foreignObject x="" y="361" width="87" height="118" id="11" class="text DEF0" transform="rotate(0) scale(1)"></foreignObject>');
	}
	if (($("#rectangle").prop("checked") || $("#text").prop("checked")
			|| $("#highlight").prop("checked") || $("#comment").prop("checked")
	// || $( "#drawSelect").prop("checked")
	)) {
		// if (mode == "move") {
		// return mode = "select";
		// }
		if (parseInt($('#' + i).attr("width")) == 0
				|| parseInt($('#' + i).attr("height")) == 0) {
			$("#" + i).remove();
			i--;
			if ($("#" + i).attr("transform")) {
				return;
			} else {
				$("#" + i).attr("transform",
						"rotate(" + rotate + ") scale(" + scale + ")");
			}
		}
		x1[i] = parseInt($('#' + i).attr("x")) / scale;
		y1[i] = parseInt($('#' + i).attr("y")) / scale;
		x2[i] = parseInt($('#' + i).attr("width")) / scale;
		y2[i] = parseInt($('#' + i).attr("height")) / scale;
		if ($("#rectangle").prop("checked")) {
			$('#' + i).attr("stroke-width", lineSize);
		}
		switch (rotate) {
		case 0:
			$('#' + i).attr("x", x1[i]);
			$('#' + i).attr("y", y1[i]);
			$('#' + i).attr("width", x2[i]);
			$('#' + i).attr("height", y2[i]);
			$("#t" + i).css("width", x2[i]);
			if ($("<b></b>").addClass($("#" + i).attr("class"))
					.hasClass("text")) {
				$("#t" + i).css("height", y2[i]);
			} else {
				$("#t" + i).css("height", y2[i]/ 2);
				$("#i" + i).css("width", x2[i] * scale);
				$("#i" + i).css("height", y2[i] * scale / 2);
			}
			break;
		case 90:
			$('#' + i).attr("x", y1[i]);
			$('#' + i).attr("y", -x1[i] - x2[i]);
			$('#' + i).attr("width", y2[i]);
			$('#' + i).attr("height", x2[i]);
			$("#t" + i).css("width", y2[i] * scale);
			if ($("<b></b>").addClass($("#" + i).attr("class"))
					.hasClass("text")) {
				$("#t" + i).css("height", x2[i] * scale);
			} else {
				$("#t" + i).css("height", x2[i] * scale / 2);
				$("#i" + i).css("width", y2[i] * scale);
				$("#i" + i).css("height", x2[i] * scale / 2);
			}
			break;
		case 180:
			$('#' + i).attr("x", -x1[i] - x2[i]);
			$('#' + i).attr("y", -y1[i] - y2[i]);
			$('#' + i).attr("width", x2[i]);
			$('#' + i).attr("height", y2[i]);
			$("#t" + i).css("width", x2[i] * scale);
			if ($("<b></b>").addClass($("#" + i).attr("class"))
					.hasClass("text")) {
				$("#t" + i).css("height", y2[i] * scale);
			} else {
				$("#t" + i).css("height", y2[i] * scale / 2);
				$("#i" + i).css("width", x2[i] * scale);
				$("#i" + i).css("height", y2[i] * scale / 2);
			}
			break;
		case 270:
			$('#' + i).attr("x", -y1[i] - y2[i]);
			$('#' + i).attr("y", x1[i]);
			$('#' + i).attr("width", y2[i]);
			$('#' + i).attr("height", x2[i]);
			$("#t" + i).css("width", y2[i] * scale);
			if ($("<b></b>").addClass($("#" + i).attr("class"))
					.hasClass("text")) {
				$("#t" + i).css("height", x2[i] * scale);
			} else {
				$("#t" + i).css("height", x2[i] * scale / 2);
				$("#i" + i).css("width", y2[i] * scale);
				$("#i" + i).css("height", x2[i] * scale / 2);
			}
			break;
		}
		x1[i] = parseInt($('#' + i).attr("x"));
		y1[i] = parseInt($('#' + i).attr("y"));
		x2[i] = parseInt($('#' + i).attr("width"));
		y2[i] = parseInt($('#' + i).attr("height"));
		switch (rotate) {
		case 90:
			y1[i] -= y2[0];
			break;
		case 180:
			x1[i] -= x2[0];
			y1[i] -= y2[0];
			break;
		case 270:
			x1[i] -= x2[0];
			break;
		}
		// if ($("#drawSelect").prop("checked")) {
		// rotate += 90;
		// startAnnotation();
		// rotate -= 90;
		// redraw();
		// var rectangle = document.getElementById(i);
		// var svg = document.getElementById("svg");
		// var g = vis.append("g").attr("transform", "translate(0 0)").attr(
		// "id", "gSelect");
		// for (var j = 5; j < i; j++) {
		// var closedPath = document.getElementById(j);
		// if (closedPath != null) {
		// var svgRoot = closedPath.farthestViewportElement;
		// var rect = svgRoot.createSVGRect();
		// rect.x = rectangle.x.animVal.value;
		// rect.y = rectangle.y.animVal.value;
		// rect.height = rectangle.height.animVal.value;
		// rect.width = rectangle.width.animVal.value;
		// var hasIntersection = svgRoot.checkIntersection(closedPath,
		// rect);
		// if (hasIntersection) {
		// $('#' + j).attr("class", $('#' + j).attr("class"))
		// .attr("onmousedown",
		// "startMove(event, 'group')").attr(
		// "onmouseup", "endMove()");
		// $('#gSelect').append($('#' + j));
		// if ($('#' + j).prop("tagName") == "line") {
		// g.append("rect").attr("x",
		// $('#' + j).attr("x1") - 5).attr("y",
		// $('#' + j).attr("y1") - 5).attr("width",
		// "10").attr("height", "10").attr("fill",
		// "yellow").attr("stroke", "red").attr(
		// "onmousedown", "startMove(event, 'group')")
		// .attr("onmouseup", "endMove()").attr("id",
		// "1").attr("transform",
		// $('#' + j).attr("transform")).attr(
		// "class", $('#' + j).attr("class"));
		// x1[1] = x1[selectId] - 5;
		// y1[1] = y1[selectId] - 5;
		// g.append("rect").attr("x",
		// $('#' + j).attr("x2") - 5).attr("y",
		// $('#' + j).attr("y2") - 5).attr("width",
		// "10").attr("height", "10").attr("fill",
		// "yellow").attr("stroke", "red").attr(
		// "onmousedown", "startMove(event, 'group')")
		// .attr("onmouseup", "endMove()").attr("id",
		// "2").attr("transform",
		// $('#' + j).attr("transform")).attr(
		// "class", $('#' + j).attr("class"));
		// x1[2] = x2[selectId] - 5;
		// y1[2] = y2[selectId] - 5;
		// }
		// if ($('#' + j).prop("tagName") == "rect") {
		// g.append("rect")
		// .attr("x", $('#' + j).attr("x") - 5).attr(
		// "y", $('#' + j).attr("y") - 5)
		// .attr("width", "10").attr("height", "10")
		// .attr("fill", "yellow").attr("stroke",
		// "red").attr("onmousedown",
		// "startMove(event, 'group')").attr(
		// "onmouseup", "endMove()").attr(
		// "id", "1").attr("transform",
		// $('#' + j).attr("transform")).attr(
		// "class", $('#' + j).attr("class"));
		// x1[1] = x1[selectId] - 5;
		// y1[1] = y1[selectId] - 5;
		// g.append("rect")
		// .attr(
		// "x",
		// parseInt($('#' + j).attr("x"))
		// + parseInt($('#' + j).attr(
		// "width")) - 5)
		// .attr("y", $('#' + j).attr("y") - 5).attr(
		// "width", "10").attr("height", "10")
		// .attr("fill", "yellow").attr("stroke",
		// "red").attr("onmousedown",
		// "startMove(event, 'group')").attr(
		// "onmouseup", "endMove()").attr(
		// "id", "2").attr("transform",
		// $('#' + j).attr("transform")).attr(
		// "class", $('#' + j).attr("class"));
		// x1[2] = x1[selectId]
		// + parseInt($('#' + j).attr("width")) - 5;
		// y1[2] = y1[selectId] - 5;
		// g.append("rect")
		// .attr("x", $('#' + j).attr("x") - 5).attr(
		// "y",
		// parseInt($('#' + j).attr("y"))
		// + parseInt($('#' + j).attr(
		// "height")) - 5)
		// .attr("width", "10").attr("height", "10")
		// .attr("fill", "yellow").attr("stroke",
		// "red").attr("onmousedown",
		// "startMove(event, 'group')").attr(
		// "onmouseup", "endMove()").attr(
		// "id", "3").attr("transform",
		// $('#' + j).attr("transform")).attr(
		// "class", $('#' + j).attr("class"));
		// x1[3] = x1[selectId] - 5;
		// y1[3] = y1[selectId]
		// + parseInt($('#' + j).attr("height")) - 5;
		// g.append("rect")
		// .attr(
		// "x",
		// parseInt($('#' + j).attr("x"))
		// + parseInt($('#' + j).attr(
		// "width")) - 5)
		// .attr(
		// "y",
		// parseInt($('#' + j).attr("y"))
		// + parseInt($('#' + j).attr(
		// "height")) - 5)
		// .attr("width", "10").attr("height", "10")
		// .attr("fill", "yellow").attr("stroke",
		// "red").attr("onmousedown",
		// "startMove(event, 'group')").attr(
		// "onmouseup", "endMove()").attr(
		// "id", "4").attr("transform",
		// $('#' + j).attr("transform")).attr(
		// "class", $('#' + j).attr("class"));
		// x1[4] = x1[selectId]
		// + parseInt($('#' + j).attr("width")) - 5;
		// y1[4] = y1[selectId]
		// + parseInt($('#' + j).attr("height")) - 5;
		// }
		// if ($('#' + j).prop("tagName") == "foreignObject") {
		// g.append("rect")
		// .attr("x", $('#' + j).attr("x") - 5).attr(
		// "y", $('#' + j).attr("y") - 5)
		// .attr("width", "10").attr("height", "10")
		// .attr("fill", "yellow").attr("stroke",
		// "red").attr("onmousedown",
		// "startMove(event, 'group')").attr(
		// "onmouseup", "endMove()").attr(
		// "id", "1").attr("transform",
		// $('#' + j).attr("transform")).attr(
		// "class", $('#' + j).attr("class"));
		// x1[1] = x1[selectId] - 5;
		// y1[1] = y1[selectId] - 5;
		// g.append("rect")
		// .attr(
		// "x",
		// parseInt($('#' + j).attr("x"))
		// + parseInt($('#' + j).attr(
		// "width")) - 5)
		// .attr("y", $('#' + j).attr("y") - 5).attr(
		// "width", "10").attr("height", "10")
		// .attr("fill", "yellow").attr("stroke",
		// "red").attr("onmousedown",
		// "startMove(event, 'group')").attr(
		// "onmouseup", "endMove()").attr(
		// "id", "2").attr("transform",
		// $('#' + j).attr("transform")).attr(
		// "class", $('#' + j).attr("class"));
		// x1[2] = x1[selectId]
		// + parseInt($('#' + j).attr("width")) - 5;
		// y1[2] = y1[selectId] - 5;
		// g.append("rect")
		// .attr("x", $('#' + j).attr("x") - 5).attr(
		// "y",
		// parseInt($('#' + j).attr("y"))
		// + parseInt($('#' + j).attr(
		// "height")) - 5)
		// .attr("width", "10").attr("height", "10")
		// .attr("fill", "yellow").attr("stroke",
		// "red").attr("onmousedown",
		// "startMove(event, 'group')").attr(
		// "onmouseup", "endMove()").attr(
		// "id", "3").attr("transform",
		// $('#' + j).attr("transform")).attr(
		// "class", $('#' + j).attr("class"));
		// x1[3] = x1[selectId] - 5;
		// y1[3] = y1[selectId]
		// + parseInt($('#' + j).attr("height")) - 5;
		// g.append("rect")
		// .attr(
		// "x",
		// parseInt($('#' + j).attr("x"))
		// + parseInt($('#' + j).attr(
		// "width")) - 5)
		// .attr(
		// "y",
		// parseInt($('#' + j).attr("y"))
		// + parseInt($('#' + j).attr(
		// "height")) - 5)
		// .attr("width", "10").attr("height", "10")
		// .attr("fill", "yellow").attr("stroke",
		// "red").attr("onmousedown",
		// "startMove(event, 'group')").attr(
		// "onmouseup", "endMove()").attr(
		// "id", "4").attr("transform",
		// $('#' + j).attr("transform")).attr(
		// "class", $('#' + j).attr("class"));
		// x1[4] = x1[selectId]
		// + parseInt($('#' + j).attr("width")) - 5;
		// y1[4] = y1[selectId]
		// + parseInt($('#' + j).attr("height")) - 5;
		// }
		// }
		// }
		// }
		// $("#" + i).remove();
		// i--;
		// }
	}
}
var container = d3.select('body').append('div').attr('id', 'container');
$('#container').width('800px');
$('#container').height('800px');