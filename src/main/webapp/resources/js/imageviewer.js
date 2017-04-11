window.onload = function() {
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
	var layerItems = [];
	var layerModified = [];
	var layerCreated = [];
	var layerActive = jsonLayer.Active;
	var contrast = 100;
	var brightness = 100;
	var checkGrayscale = false;

	var container = d3.select('body').append('div').attr('id', 'container');
	var vis = container.append('svg').on("mousedown", mousedown).on("mouseup",
			mouseup);
	vis.append("image").attr("x", 0).attr("y", 0).attr("xlink:href",
			jsonLayer.Image).attr("id", "0").attr("transform",
			"rotate(0) scale(1)");
	x1[0] = 0;
	y1[0] = 0;

	var img = new Image();
	img.onload = function() {
		x2[0] = -parseInt(this.width);
		y2[0] = -parseInt(this.height);
		vis.attr("width", -x2[0]).attr("height", -y2[0]);
	}
	img.src = jsonLayer.Image;

	$.contextMenu({
		selector : '#container',
		callback : function(key, options) {
			switch (key) {
			case "cut":
				$("#cut").trigger("click");
				break;
			case "copy":
				$("#copy").trigger("click");
				break;
			case "paste":
				$("#paste").trigger("click");
				break;
			case "properties":
				$("#properties").trigger("click");
				break;
			}
		},
		items : {
			"cut" : {
				name : "Cut",
				icon : "cut",
				accesskey : "u"
			},
			"copy" : {
				name : "Copy",
				icon : "copy",
				accesskey : "c"
			},
			"paste" : {
				name : "Paste",
				icon : "paste",
				accesskey : "a"
			},
			"properties" : {
				name : "Properties",
				icon : "edit",
				accesskey : "p"
			}
		}
	});

	function addNewLayer(countLayer, layerName, layerOwner, layerDisplay,
			layerItems, layerModified, layerCreated, layerActive) {
		var layerBody = "";
		layerBody += '<div class="row">' + '<div class="col-md-1" id="layer'
				+ countLayer + '">' + layerName + '</div>'
				+ '<div class="col-md-1">' + layerOwner + '</div>'
				+ '<div class="col-md-1">';
		if (layerDisplay) {
			layerBody += '<input type="checkbox" name="display" value="'
					+ layerName + '" checked>';
		} else {
			layerBody += '<input type="checkbox" name="display" value="'
					+ layerName + '">';
		}
		layerBody += '</div>' + '<div class="col-md-1">' + layerItems
				+ '</div>' + '<div class="col-md-2">' + layerModified
				+ '</div>' + '<div class="col-md-2">' + layerCreated + '</div>'
				+ '<div class="col-md-1">';
		if (layerActive == layerName) {
			layerBody += '<input type="radio" name="active" value="'
					+ layerName + '" checked="checked"></div></div>';
			$('#layerActive').val(layerName);
		} else {
			layerBody += '<input type="radio" name="active" value="'
					+ layerName + '"></div></div>';
		}
		$('#layerBody').append(layerBody);
	}

	for (var k = 0; k < layerCount; k++) {
		layer = jsonLayer.Layer[k];
		layerName[k] = layer.Name;
		layerOwner[k] = layer.Owner;
		layerDisplay[k] = true;
		layerItems[k] = layer.Items;
		layerModified[k] = layer.Modified;
		layerCreated[k] = layer.Created;
		addNewLayer(k + 1, layerName[k], layerOwner[k], layerDisplay[k],
				layerItems[k], layerModified[k], layerCreated[k], layerActive);
		var annotationsLength = layer.Annotations.length;
		for (var j = 0; j < annotationsLength; j++) {
			var counter = layer.Annotations[j];
			x1[counter.id] = parseInt(counter.x1);
			y1[counter.id] = parseInt(counter.y1);
			x2[counter.id] = parseInt(counter.x2);
			y2[counter.id] = parseInt(counter.y2);
			if (counter.type == "line") {
				vis.append("line").attr("x1", counter.x1)
						.attr("y1", counter.y1).attr("x2", counter.x2).attr(
								"y2", counter.y2).attr("id", counter.id).attr(
								"stroke", counter.stroke).attr("stroke-width",
								counter.strokew).attr("class",
								counter.class + " " + layer.Name).attr(
								"transform", "rotate(0) scale(1)").on("click",
								selectClick);
			}
			if (counter.type == "rect") {
				vis.append("rect").attr("x", counter.x1).attr("y", counter.y1)
						.attr("width", counter.x2).attr("height", counter.y2)
						.attr("id", counter.id).attr("stroke", counter.stroke)
						.attr("fill", counter.strokew).attr("class",
								counter.class + " " + layer.Name).attr(
								"transform", "rotate(0) scale(1)").on("click",
								selectClick);
			}
			if (counter.id > i) {
				i = counter.id;
			}
		}
	}

	$('input[type=radio][name=active]').change(function() {
		$('#layerActive').val(this.value);
	});

	$("#select").click(function() {
		$('[name=toolOption]').prop('checked', false);
	});

	function selectBefore() {
		$("#1").remove();
		$("#2").remove();
		$("#3").remove();
		$("#4").remove();
		$("#" + selectId).attr("onmousedown", null);
		$("#" + selectId).attr("onmouseup", null);
		$("#" + selectId).on("click", selectClick);
	}

	function selectClick(event) {
		$('[name=toolOption]').prop('checked', false);
		var g;
		if ($(this).parent().attr("transform") == null) {
			g = vis.append("g").attr("transform", "translate(0 0)");
		} else {
			g = vis.append("g").attr("transform",
					$(this).parent().attr("transform"));
		}
		selectBefore();
		selectId = $(this).attr("id");
		if ($(this).prop("tagName") == "line") {
			g.append("line").attr("x1", $(this).attr("x1")).attr("y1",
					$(this).attr("y1")).attr("x2", $(this).attr("x2")).attr(
					"y2", $(this).attr("y2")).attr("id", selectId).attr(
					"stroke", $(this).attr("stroke")).attr("stroke-width",
					$(this).attr("stroke-width")).attr("class",
					$(this).attr("class")).attr("onmousedown",
					"startMove(event, 'group')").attr("onmouseup", "endMove()")
					.attr("transform", $(this).attr("transform"));
			g.append("rect").attr("x", $(this).attr("x1") - 5).attr("y",
					$(this).attr("y1") - 5).attr("width", "10").attr("height",
					"10").attr("fill", "yellow").attr("stroke", "red").attr(
					"onmousedown", "startMove(event, 'group')").attr(
					"onmouseup", "endMove()").attr("id", "1").attr("transform",
					$(this).attr("transform"));
			x1[1] = x1[selectId] - 5;
			y1[1] = y1[selectId] - 5;
			g.append("rect").attr("x", $(this).attr("x2") - 5).attr("y",
					$(this).attr("y2") - 5).attr("width", "10").attr("height",
					"10").attr("fill", "yellow").attr("stroke", "red").attr(
					"onmousedown", "startMove(event, 'group')").attr(
					"onmouseup", "endMove()").attr("id", "2").attr("transform",
					$(this).attr("transform"));
			x1[2] = x2[selectId] - 5;
			y1[2] = y2[selectId] - 5;
		}
		if ($(this).prop("tagName") == "rect") {
			g.append("rect").attr("x", $(this).attr("x")).attr("y",
					$(this).attr("y")).attr("width", $(this).attr("width"))
					.attr("height", $(this).attr("height"))
					.attr("id", selectId).attr("fill", $(this).attr("fill"))
					.attr("stroke", $(this).attr("stroke")).attr(
							"fill-opacity", $(this).attr("fill-opacity")).attr(
							"class", $(this).attr("class")).attr("onmousedown",
							"startMove(event, 'group')").attr("onmouseup",
							"endMove()").attr("transform",
							$(this).attr("transform"));
			g.append("rect").attr("x", $(this).attr("x") - 5).attr("y",
					$(this).attr("y") - 5).attr("width", "10").attr("height",
					"10").attr("fill", "yellow").attr("stroke", "red").attr(
					"onmousedown", "startMove(event, 'group')").attr(
					"onmouseup", "endMove()").attr("id", "1").attr("transform",
					$(this).attr("transform"));
			x1[1] = x1[selectId] - 5;
			y1[1] = y1[selectId] - 5;
			g.append("rect").attr(
					"x",
					parseInt($(this).attr("x"))
							+ parseInt($(this).attr("width")) - 5).attr("y",
					$(this).attr("y") - 5).attr("width", "10").attr("height",
					"10").attr("fill", "yellow").attr("stroke", "red").attr(
					"onmousedown", "startMove(event, 'group')").attr(
					"onmouseup", "endMove()").attr("id", "2").attr("transform",
					$(this).attr("transform"));
			x1[2] = x1[selectId] + parseInt($(this).attr("width")) - 5;
			y1[2] = y1[selectId] - 5;
			g.append("rect").attr("x", $(this).attr("x") - 5).attr(
					"y",
					parseInt($(this).attr("y"))
							+ parseInt($(this).attr("height")) - 5).attr(
					"width", "10").attr("height", "10").attr("fill", "yellow")
					.attr("stroke", "red").attr("onmousedown",
							"startMove(event, 'group')").attr("onmouseup",
							"endMove()").attr("id", "3").attr("transform",
							$(this).attr("transform"));
			x1[3] = x1[selectId] - 5;
			y1[3] = y1[selectId] + parseInt($(this).attr("height")) - 5;
			g.append("rect").attr(
					"x",
					parseInt($(this).attr("x"))
							+ parseInt($(this).attr("width")) - 5).attr(
					"y",
					parseInt($(this).attr("y"))
							+ parseInt($(this).attr("height")) - 5).attr(
					"width", "10").attr("height", "10").attr("fill", "yellow")
					.attr("stroke", "red").attr("onmousedown",
							"startMove(event, 'group')").attr("onmouseup",
							"endMove()").attr("id", "4").attr("transform",
							$(this).attr("transform"));
			x1[4] = x1[selectId] + parseInt($(this).attr("width")) - 5;
			y1[4] = y1[selectId] + parseInt($(this).attr("height")) - 5;
		}
		if ($(this).prop("tagName") == "foreignObject") {
			g.append("foreignObject").attr("x", $(this).attr("x")).attr("y",
					$(this).attr("y")).attr("width", $(this).attr("width"))
					.attr("height", $(this).attr("height"))
					.attr("id", selectId).attr("class", $(this).attr("class"))
					.attr("transform", $(this).attr("transform"));
			$('#' + selectId).attr("id", "o" + selectId);
			$('#t' + selectId).attr("id", "ot" + selectId);
			if ($("<b></b>").addClass($("#" + selectId).attr("class"))
					.hasClass("text")) {
				$('#' + selectId).append('<textarea id="t' + selectId + '"/>');
				$("#t" + selectId).val($("#ot" + selectId).val());
				$("#t" + selectId).css("color",
						$("#ot" + selectId).css("color"));
				$("#t" + selectId).css("backgroundColor",
						$("#ot" + selectId).css("backgroundColor"));
				$("#t" + selectId).css("border",
						$("#ot" + selectId).css("border"));
				$("#t" + selectId).css("font-family",
						$("#ot" + selectId).css("font-family"));
				$("#t" + selectId).css("fontSize",
						$("#ot" + selectId).css("fontSize"));
				$("#t" + selectId).css("font-style",
						$("#ot" + selectId).css("font-style"));
				$("#t" + selectId).css("width", $(this).attr("width"));
				$("#t" + selectId).css("height", $(this).attr("height"));
			} else {
				$('#i' + selectId).attr("id", "oi" + selectId);
				$('#' + selectId)
						.append(
								'<img id="i'
										+ selectId
										+ '" src="resources/images/imageViewer/comment.jpg"/>');
				$('#' + selectId).append('<textarea id="t' + selectId + '"/>');
				$("#t" + selectId).val($("#ot" + selectId).val());
				$("#t" + selectId).css("width", $(this).attr("width"));
				$("#t" + selectId).css("height", $(this).attr("height") / 2);
				$("#i" + selectId).css("width", $(this).attr("width"));
				$("#i" + selectId).css("height", $(this).attr("height") / 2);

			}
			g.append("rect").attr("x", $(this).attr("x") - 5).attr("y",
					$(this).attr("y") - 5).attr("width", "10").attr("height",
					"10").attr("fill", "yellow").attr("stroke", "red").attr(
					"onmousedown", "startMove(event, 'group')").attr(
					"onmouseup", "endMove()").attr("id", "1").attr("transform",
					$(this).attr("transform"));
			x1[1] = x1[selectId] - 5;
			y1[1] = y1[selectId] - 5;
			g.append("rect").attr(
					"x",
					parseInt($(this).attr("x"))
							+ parseInt($(this).attr("width")) - 5).attr("y",
					$(this).attr("y") - 5).attr("width", "10").attr("height",
					"10").attr("fill", "yellow").attr("stroke", "red").attr(
					"onmousedown", "startMove(event, 'group')").attr(
					"onmouseup", "endMove()").attr("id", "2").attr("transform",
					$(this).attr("transform"));
			x1[2] = x1[selectId] + parseInt($(this).attr("width")) - 5;
			y1[2] = y1[selectId] - 5;
			g.append("rect").attr("x", $(this).attr("x") - 5).attr(
					"y",
					parseInt($(this).attr("y"))
							+ parseInt($(this).attr("height")) - 5).attr(
					"width", "10").attr("height", "10").attr("fill", "yellow")
					.attr("stroke", "red").attr("onmousedown",
							"startMove(event, 'group')").attr("onmouseup",
							"endMove()").attr("id", "3").attr("transform",
							$(this).attr("transform"));
			x1[3] = x1[selectId] - 5;
			y1[3] = y1[selectId] + parseInt($(this).attr("height")) - 5;
			g.append("rect").attr(
					"x",
					parseInt($(this).attr("x"))
							+ parseInt($(this).attr("width")) - 5).attr(
					"y",
					parseInt($(this).attr("y"))
							+ parseInt($(this).attr("height")) - 5).attr(
					"width", "10").attr("height", "10").attr("fill", "yellow")
					.attr("stroke", "red").attr("onmousedown",
							"startMove(event, 'group')").attr("onmouseup",
							"endMove()").attr("id", "4").attr("transform",
							$(this).attr("transform"));
			x1[4] = x1[selectId] + parseInt($(this).attr("width")) - 5;
			y1[4] = y1[selectId] + parseInt($(this).attr("height")) - 5;
		}
		$(this).remove();
	}

	function zoom(zoom) {
		rotate += 90;
		startAnnotation();
		rotate -= 90;
		scale = zoom;
		redraw();
		$('#lbZoom').text(Math.floor(scale * 100) + "%");
	}

	$("#zoomout").click(function() {
		zoom(Math.ceil10(scale * 10, -2) / 10 - 0.1);
	});

	$("#zoomin").click(function() {
		zoom(Math.floor10(scale * 10, -2) / 10 + 0.1);
	});

	$('#controlZoom').onkeyup = controlZoom.onchange = function() {
		var val = parseInt(this.value);
		if (val > 100 || val < 0)
			return false;
		if (val > 50) {
			zoom(0.06 * val - 2);
		} else {
			zoom(val / 50);
		}
	}

	$("input[name=toolOption]").on("click", selectBefore);

	function mousedown() {
		var m = d3.mouse(this);
		i = parseInt(i);
		x = m[0];
		y = m[1];
		if ($("#line").prop("checked")) {
			i++;
			anno = vis.append("line").attr("x1", x).attr("y1", y).attr("x2", x)
					.attr("y2", y).attr("id", i).attr("stroke", "black").attr(
							"stroke-width", scale).attr("class", layerActive).on("click", selectClick);
			vis.on("mousemove", mousemove);
		}
		if ($("#rectangle").prop("checked")) {
			i++;
			anno = vis.append("rect").attr("x", x).attr("y", y)
					.attr("width", 0).attr("height", 0).attr("id", i).attr(
							"fill", "white").attr("stroke", "black").attr("class", layerActive).on(
							"click", selectClick);
			vis.on("mousemove", mousemove);
		}
		if ($("#text").prop("checked")) {
			i++;
			anno = vis.append("foreignObject").attr("x", x).attr("y", y).attr(
					"width", 0).attr("height", 0).attr("class", "text").attr(
					"id", i).attr("class", layerActive).on("click", selectClick);
			$('#' + i).append('<textarea id="t' + i + '"/>');
			$("#t" + i).css("color", "black");
			$("#t" + i).css("backgroundColor", "transparent");
			$("#t" + i).css("border", "1px solid black");
			$("#t" + i).css("font-family", "Arial");
			$("#t" + i).css("fontSize", 20);
			$("#t" + i).css("font-style", "normal");
			$("#t" + i).css("width", 0);
			$("#t" + i).css("height", 0);
			vis.on("mousemove", mousemove);
		}
		if ($("#highlight").prop("checked")) {
			i++;
			anno = vis.append("rect").attr("x", x).attr("y", y)
					.attr("width", 0).attr("height", 0).attr("fill", "yellow")
					.attr("stroke", "none").attr("fill-opacity", "0.4").attr("class", layerActive).on(
							"click", selectClick).attr("id", i);
			vis.on("mousemove", mousemove);
		}
		if ($("#comment").prop("checked")) {
			i++;
			anno = vis.append("foreignObject").attr("x", x).attr("y", y).attr(
					"width", 0).attr("height", 0).attr("id", i).attr("class", layerActive).on("click",
					selectClick);
			$('#' + i)
					.append(
							'<img id="i'
									+ i
									+ '" src="resources/images/imageViewer/comment.jpg"/>');
			$('#' + i).append(
					'<textarea id="t' + i + '">' + jsonLayer.User
							+ '</textarea>');
			$("#t" + i).css("width", 0);
			$("#t" + i).css("height", 0);
			$("#i" + i).css("width", 0);
			$("#i" + i).css("height", 0);
			vis.on("mousemove", mousemove);
		}
	}

	function mousemove() {
		var m = d3.mouse(this);
		if ($("#line").prop("checked")) {
			anno.attr("x2", m[0]).attr("y2", m[1]);
		}
		if ($("#rectangle").prop("checked")) {
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
		if ($("#highlight").prop("checked")) {
			if (m[1] - y < 0)
				anno.attr("y", m[1]);
			if (m[0] - x < 0)
				anno.attr("x", m[0]);
			anno.attr("width", Math.abs(m[0] - x));
			anno.attr("height", Math.abs(m[1] - y));
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

	function mouseup() {
		vis.on("mousemove", null);
		$('#' + i).attr("transform",
				"rotate(" + rotate + ") scale(" + scale + ")");
		switch ($('#' + i).prop("tagName")) {
		case "line":
			x1[i] = parseInt($('#' + i).attr("x1")) / scale;
			x2[i] = parseInt($('#' + i).attr("x2")) / scale;
			y1[i] = parseInt($('#' + i).attr("y1")) / scale;
			y2[i] = parseInt($('#' + i).attr("y2")) / scale;

			$('#' + i).attr("stroke-width", 1);
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
			break;
		case "rect":
		case "foreignObject":
			x1[i] = parseInt($('#' + i).attr("x")) / scale;
			y1[i] = parseInt($('#' + i).attr("y")) / scale;
			x2[i] = parseInt($('#' + i).attr("width")) / scale;
			y2[i] = parseInt($('#' + i).attr("height")) / scale;
			switch (rotate) {
			case 0:
				$('#' + i).attr("x", x1[i]);
				$('#' + i).attr("y", y1[i]);
				$('#' + i).attr("width", x2[i]);
				$('#' + i).attr("height", y2[i]);
				$("#t" + i).css("width", x2[i] * scale);
				if ($("<b></b>").addClass($("#" + i).attr("class")).hasClass(
						"text")) {
					$("#t" + i).css("height", y2[i] * scale);
				} else {
					$("#t" + i).css("height", y2[i] * scale / 2);
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
				if ($("<b></b>").addClass($("#" + i).attr("class")).hasClass(
						"text")) {
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
				if ($("<b></b>").addClass($("#" + i).attr("class")).hasClass(
						"text")) {
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
				if ($("<b></b>").addClass($("#" + i).attr("class")).hasClass(
						"text")) {
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
			break;
		}
	}

	$("#properties")
			.click(
					function() {
						if ($("#" + selectId).prop("tagName") == "foreignObject") {
							if ($("#i" + selectId).length == 0) {
								$("#commentText").val($("#t" + selectId).val());
								$("#commentText").css("color",
										$("#t" + selectId).css("color"));
								$("#commentText").css("font-family",
										$("#t" + selectId).css("font-family"));
								$("#commentText").css("fontSize",
										$("#t" + selectId).css("fontSize"));
								$("#commentText").css("font-style",
										$("#t" + selectId).css("font-style"));
								$("#color")
										.val($("#t" + selectId).css("color"));
								$("#fill").val(
										$("#t" + selectId).css(
												"backgroundColor"));
								$("#border").val(
										$("#t" + selectId).css("border"));
								$("#font").val(
										$("#t" + selectId).css("font-family"));
								$("#fontSize").val(
										$("#t" + selectId).css("fontSize"));
								if ($("#t" + selectId).css("font-style") == "normal") {
									$("#fontStyle").val("normal");
								}
								if ($("#t" + selectId).css("font-style") == "italic") {
									$("#fontStyle").val("italic");
								}
								if ($("#t" + selectId).css("text-decoration") == "underline") {
									$("#fontStyle").val("underline");
								}
								if ($("#t" + selectId).css("font-weight") == "bold") {
									$("#fontStyle").val("bold");
								}
								$("#myModalText").modal();
							} else {
								$("#tbComment").val($("#t" + selectId).val());
								$("#myModalComment").modal();
							}
						} else {
							// disable
							$("#" + $("#" + selectId).attr("stroke")).prop(
									"checked", true);
							$("#width").val(
									$("#" + selectId).attr("stroke-width"));
							$("#1" + $("#" + selectId).attr("fill")).prop(
									"checked", true);
							$("#2" + $("#" + selectId).attr("fill")).prop(
									"checked", true);
							$("#myModalLine").modal();
						}
					});

	$("#commentProperties").click(function() {
		$("#t" + selectId).val($("#tbComment").val());
	});

	$("#textProperties").click(
			function() {
				$("#t" + selectId).val($("#commentText").val());
				$("#t" + selectId).css("color",
						$('#color').find(":selected").text());
				$("#t" + selectId).css("backgroundColor",
						$('#fill').find(":selected").text());
				$("#t" + selectId).css("border",
						"1px solid " + $('#border').find(":selected").text());
				$("#t" + selectId).css("font-family",
						$('#font').find(":selected").text());
				$("#t" + selectId).css("fontSize",
						$('#fontSize').find(":selected").text());
				if ($('#fontStyle').find(":selected").text() == "Regular") {
					$("#t" + selectId).css("font-style", "normal");
					$("#t" + selectId).css("font-weight", "");
					$("#t" + selectId).css("text-decoration", "");
				}
				if ($('#fontStyle').find(":selected").text() == "Bold") {
					$("#t" + selectId).css("font-style", "");
					$("#t" + selectId).css("font-weight", "bold");
					$("#t" + selectId).css("text-decoration", "");
				}
				if ($('#fontStyle').find(":selected").text() == "Italic") {
					$("#t" + selectId).css("font-style", "italic");
					$("#t" + selectId).css("font-weight", "");
					$("#t" + selectId).css("text-decoration", "");
				}
				if ($('#fontStyle').find(":selected").text() == "Underline") {
					$("#t" + selectId).css("font-style", "");
					$("#t" + selectId).css("font-weight", "");
					$("#t" + selectId).css("text-decoration", "underline");
				}
			});

	$("#okProperties").click(
			function() {
				if ($("#" + selectId).prop("tagName") == "line") {
					$("#" + selectId).attr("stroke-width", $('#width').val());
					$("#" + selectId).attr("stroke",
							$('input[name=color]:checked').val());
				}
				if ($("#" + selectId).prop("tagName") == "rect") {
					if ($("#" + selectId).attr("fill-opacity") == "0.4") {
						$("#" + selectId).attr("fill",
								$('input[name=highlight]:checked').val());
					} else {
						$("#" + selectId).attr("stroke",
								$('input[name=color]:checked').val());
						$("#" + selectId).attr("fill",
								$('input[name=fill]:checked').val());
					}
				}
			});

	$("#btnGrayscale").click(function() {
		$('#controls').val(contrast);
		$('#controls1').val(brightness);
		if(checkGrayscale){
			$("#cbox1").prop('checked',true);
		}
		else{
			$("#cbox1").prop('checked',false);
		}
	});

	$("#okGrayscale").click(
			function() {
				contrast = parseInt($('#controls').val());
				brightness = parseInt($('#controls1').val());
				checkGrayscale = cbox1.checked;
				var val;
				if (checkGrayscale) {			
					val = (contrast + brightness) / 2 - 50;
					if (val > 50 || val < -50)
						return false;
				}
				else{
					val=50;
				}
				$('#container').css("backgroundColor",
						val > 0 ? 'white' : 'black');
				$('#container').css("opacity", Math.abs(val / 100) * 2);
			});

	$("#btnLayer").click(
			function() {
				for (var j = 1; j <= layerCount; j++) {
					$("#layerBody .row:eq(1)").remove();
				}
				for (var k = 0; k < layerCount; k++) {
					addNewLayer(k + 1, layerName[k], layerOwner[k],
							layerDisplay[k], layerItems[k], layerModified[k],
							layerCreated[k], layerActive);
				}
				$('#layerName').val("");
			});

	$("#addLayer").click(
			function() {
				var k = 0;
				if ($('#layerName').val() != "") {
					var countRow = $("#layerBody .row").length;
					for (var j = 1; j < countRow; j++) {
						if ($('#layerName').val() == $('#layer' + j).html()) {
							k++;
						}
					}
					if (k == 0) {
						addNewLayer(countRow, $('#layerName').val(),
								jsonLayer.User, true, 0, 2017, 2017, false);
					} else {
						alert("Layer exist");
					}
				}
			});

	$("#removeLayer").click(function() {
		if ($('#layerName').val() != "") {
			var countRow = $("#layerBody .row").length;
			for (var j = 1; j < countRow; j++) {
				if ($('#layerName').val() == $('#layer' + j).html()) {
					return $('#layer' + j).parent().remove();
				}
			}
			alert("Layer isn't exist");

		}
	});

	$("#renameLayer").click(
			function() {
				if ($('#layerName').val() != "") {
					var countRow = $("#layerBody .row").length;
					for (var j = 1; j < countRow; j++) {
						if ($('#layerName').val() == $('#layer' + j).html()) {
							return alert("Layer name exist");
						}
					}
					for (var j = 1; j < countRow; j++) {
						if ($('#layerActive').val() == $('#layer' + j).html()) {
							$(
									"input[type=radio][name=active][value="
											+ $('#layer' + j).text() + "]")
									.attr('value', $('#layerName').val());
							$('#layer' + j).text($('#layerName').val());
							$('#layerActive').val($('#layerName').val());
							return;
						}
					}
				}
			});

	$("#activeLayer").click(
			function() {
				if ($('#layerName').val() != "") {
					var countRow = $("#layerBody .row").length;
					for (var j = 1; j < countRow; j++) {
						if ($('#layerActive').val() == $('#layer' + j).html()) {
							$('#layerActive').val($('#layerName').val());
							$("input[type=radio][name=active][value="
											+ $('#layerName').val() + "]")
									.prop("checked", true).trigger("click");
							return; 
						}
					}
					alert("Layer isn't exist");
				}
			});

	$("#displayLayer").click(
			function() {
				if ($('#layerName').val() != "") {
					var countRow = $("#layerBody .row").length;
					for (var j = 1; j < countRow; j++) {
						if ($('#layerName').val() == $('#layer' + j).html()) {
							return $(
									"input[type=checkbox][name=display][value="
											+ $('#layerName').val() + "]")
									.trigger("click");
						}
					}
					alert("Layer isn't exist");
				}
			});

	$("#okLayer").click(
			function() {
				layerCount = $("#layerBody .row").length - 1;
				for (var j = 1; j <= layerCount; j++) {
					layerName[j - 1] = $(
							"div.row:eq(" + j + ") div:nth-child(1)").text();
					layerOwner[j - 1] = $(
							"div.row:eq(" + j + ") div:nth-child(2)").text();
					layerDisplay[j - 1] = $(
							"div.row:eq(" + j + ") div:nth-child(3) input").is(
							':checked');					
					layerItems[j - 1] = $(
							"div.row:eq(" + j + ") div:nth-child(4)").text();
					layerModified[j - 1] = $(
							"div.row:eq(" + j + ") div:nth-child(5)").text();
					layerCreated[j - 1] = $(
							"div.row:eq(" + j + ") div:nth-child(6)").text();
					if ($("div.row:eq(" + j + ") div:nth-child(7) input").is(
							':checked')) {
						layerActive = layerName[j - 1];
					}
					if($("div.row:eq(" + j + ") div:nth-child(3) input").is(
							':checked')){
						$('.'+layerName[j - 1]).css("visibility", "visible");
					}
					else{
						$('.'+layerName[j - 1]).css("visibility", "hidden");
					}
				}
			});

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

	$("#print").click(function() {
		var strPrint = "<script type='text/javascript'>";
		for (var j = 5; j <= i; j++) {
			if ($("#t" + j).length) {
				var valPrint = $("#t" + j).val();
				strPrint += "$('#t' +" + j + ").val('" + valPrint + "');";
			}
		}
		strPrint += "</script>";
		printImage(document.getElementById("container").innerHTML + strPrint);
	});

	$("#print0").click(function() {
		printImage('<img src="' + jsonLayer.Image + '">');
	});

	$("#cut").click(function() {
		cut = selectId;
		$("#" + selectId).parent().css("visibility", "hidden");
	});

	$("#copy").click(
			function() {
				i++;
				rotate += 90;
				startAnnotation();
				rotate -= 90;
				redraw();
				$("#" + selectId).clone().appendTo("svg").attr("id", i).attr(
						"onmousedown", null).attr("onmouseup", null).on(
						"click", selectClick);
				x1[i] = x1[selectId];
				x2[i] = x2[selectId];
				y1[i] = y1[selectId];
				y2[i] = y2[selectId];
				$("#" + selectId).parent().css("visibility", "hidden");
				cut = selectId;
			});

	$("#paste").click(function() {
		$("#" + cut).css("visibility", "visible");
		$("#" + cut).parent().css("visibility", "visible");
	});

	function rotateAll() {
		startAnnotation();
		redraw();
	}

	$("#rotate").click(function() {
		rotate = (rotate + 90) % 360;
		rotateAll();
	});

	$("#rotateC").click(function() {
		rotate = (rotate + 270) % 360;
		rotateAll();
	});

	function startAnnotation() {
		var gLength = $("g").length;
		for (var j = 0; j < gLength; j++) {
			if ($('g:eq( ' + j + ' )').attr("transform") != "translate(0 0)") {
				var index1 = $('g:eq( ' + j + ' )').attr("transform").indexOf(
						"(");
				var index2 = $('g:eq( ' + j + ' )').attr("transform").indexOf(
						" ");
				var index3 = $('g:eq( ' + j + ' )').attr("transform").indexOf(
						")");
				var indexX = parseInt($('g:eq( ' + j + ' )').attr("transform")
						.slice(index1 + 1, index2))
						/ scale;
				var indexY = parseInt($('g:eq( ' + j + ' )').attr(
						"transf" + "orm").slice(index2 + 1, index3))
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
					for (var k = 1; k <= 4; k++) {
						x1[k] -= indexY;
						y1[k] += indexX;
					}
					break;
				case 90:
					x1[indexId] += indexX;
					y1[indexId] += indexY;
					if ($('g:eq( ' + j + ' )').children().prop("tagName") == "line") {
						x2[indexId] += indexX;
						y2[indexId] += indexY;
					}
					for (var k = 1; k <= 4; k++) {
						x1[k] += indexX;
						y1[k] += indexY;
					}
					break;
				case 180:
					x1[indexId] += indexY;
					y1[indexId] -= indexX;
					if ($('g:eq( ' + j + ' )').children().prop("tagName") == "line") {
						x2[indexId] += indexY;
						y2[indexId] -= indexX;
					}
					for (var k = 1; k <= 4; k++) {
						x1[k] += indexY;
						y1[k] -= indexX;
					}
					break;
				case 270:
					x1[indexId] -= indexX;
					y1[indexId] -= indexY;
					if ($('g:eq( ' + j + ' )').children().prop("tagName") == "line") {
						x2[indexId] -= indexX;
						y2[indexId] -= indexY;
					}
					for (var k = 1; k <= 4; k++) {
						x1[k] -= indexX;
						y1[k] -= indexY;
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
						break;
					case 90:
						$('#' + k).attr("transform",
								"rotate(90) scale(" + scale + ")");
						$('#' + k).attr("x1", x1[k]);
						$('#' + k).attr("x2", x2[k]);
						$('#' + k).attr("y1", y1[k] + y2[0]);
						$('#' + k).attr("y2", y2[k] + y2[0]);
						break;
					case 180:
						$('#' + k).attr("transform",
								"rotate(180) scale(" + scale + ")");
						$('#' + k).attr("x1", x1[k] + x2[0]);
						$('#' + k).attr("x2", x2[k] + x2[0]);
						$('#' + k).attr("y1", y1[k] + y2[0]);
						$('#' + k).attr("y2", y2[k] + y2[0]);
						break;
					case 270:
						$('#' + k).attr("transform",
								"rotate(270) scale(" + scale + ")");
						$('#' + k).attr("x1", x1[k] + x2[0]);
						$('#' + k).attr("x2", x2[k] + x2[0]);
						$('#' + k).attr("y1", y1[k]);
						$('#' + k).attr("y2", y2[k]);
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
			$('svg').attr("width", -x2[0] * scale);
			$('svg').attr("height", -y2[0] * scale);
		} else {
			$('svg').attr("width", -y2[0] * scale);
			$('svg').attr("height", -x2[0] * scale);
		}
	}
}