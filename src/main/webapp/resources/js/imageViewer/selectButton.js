$("#select").click(function() {
	$("#drawSelect").prop("checked", true).trigger("click");
	// $('[name=toolOption]').prop('checked', false);
	$('#svg').css('cursor', 'auto');
});

IV.prototype.selectBefore = function() {
	$("#1").remove();
	$("#2").remove();
	$("#3").remove();
	$("#4").remove();
	var selectId = imageViewer.selectId;
	$("#" + selectId).attr("onmousedown", null);
	$("#" + selectId).attr("onmouseup", null);
	if ($("#r" + selectId).length==0) {
		$("#" + selectId).on("click", IV.selectClick);
	} else {
		$("#r" + selectId).on("click", IV.selectClick);
	}
	imageViewer.selectId = -1;
}

IV.prototype.selectClick = function(event) {
	var vis = imageViewer.vis;
	var x1 = imageViewer.x1;
	var y1 = imageViewer.y1;
	var x2 = imageViewer.x1;
	var y2 = imageViewer.y1;
	var selectClick = imageViewer.selectClick;
	
	$("#drawSelect").prop("checked", true).trigger("click");
	// $('[name=toolOption]').prop('checked', false);
	var g;
	if ($(this).parent().attr("transform") == null) {
		g = vis.append("g").attr("transform", "translate(0 0)");
	} else {
		g = vis.append("g").attr("transform",
				$(this).parent().attr("transform"));
	}
	imageViewer.selectBefore();
	var selectId = imageViewer.selectId = $(this).attr("id");
	if ($(this).prop("tagName") == "line") {
		g.append("line").attr("x1", $(this).attr("x1")).attr("y1",
				$(this).attr("y1")).attr("x2", $(this).attr("x2")).attr("y2",
				$(this).attr("y2")).attr("id", selectId).attr("stroke",
				$(this).attr("stroke")).attr("stroke-width",
				$(this).attr("stroke-width")).attr("class",
				$(this).attr("class")).attr("onmousedown",
				"startMove(event, 'group')").attr("onmouseup", "endMove()")
				.attr("transform", $(this).attr("transform"));
		g.append("rect").attr("x", $("#r" + selectId).attr("x")).attr("y",
				$("#r" + selectId).attr("y")).attr("width",
				$("#r" + selectId).attr("width")).attr("height",
				$("#r" + selectId).attr("height")).attr("id", "r" + selectId)
				.attr("fill", $("#r" + selectId).attr("fill")).attr("stroke",
						$("#r" + selectId).attr("stroke")).attr("stroke-width",
						$("#r" + selectId).attr("stroke-width"))
				.attr("fill-opacity", $("#r" + selectId).attr("fill-opacity"))
				.attr("class", $("#r" + selectId).attr("class")).attr(
						"onmousedown", "startMove(event, 'group')").attr(
						"onmouseup", "endMove()").attr("transform",
						$("#r" + selectId).attr("transform")).attr(
						"pointer-events", "all");
		$("#r" + selectId).remove();
		g.append("rect").attr("x", $(this).attr("x1") - 5).attr("y",
				$(this).attr("y1") - 5).attr("width", "10")
				.attr("height", "10").attr("fill", "yellow").attr("stroke",
						"red").attr("onmousedown", "startMove(event, 'group')")
				.attr("onmouseup", "endMove()").attr("id", "1").attr(
						"transform", $(this).attr("transform"));
		x1[1] = x1[selectId] - 5;
		y1[1] = y1[selectId] - 5;
		g.append("rect").attr("x", $(this).attr("x2") - 5).attr("y",
				$(this).attr("y2") - 5).attr("width", "10")
				.attr("height", "10").attr("fill", "yellow").attr("stroke",
						"red").attr("onmousedown", "startMove(event, 'group')")
				.attr("onmouseup", "endMove()").attr("id", "2").attr(
						"transform", $(this).attr("transform"));
		x1[2] = x2[selectId] - 5;
		y1[2] = y2[selectId] - 5;
	}
	if ($(this).prop("tagName") == "rect") {
		g.append("rect").attr("x", $(this).attr("x")).attr("y",
				$(this).attr("y")).attr("width", $(this).attr("width")).attr(
				"height", $(this).attr("height")).attr("id", selectId).attr(
				"fill", $(this).attr("fill")).attr("stroke",
				$(this).attr("stroke")).attr("stroke-width",
				$(this).attr("stroke-width")).attr("fill-opacity",
				$(this).attr("fill-opacity")).attr("class",
				$(this).attr("class")).attr("onmousedown",
				"startMove(event, 'group')").attr("onmouseup", "endMove()")
				.attr("transform", $(this).attr("transform")).attr(
						"pointer-events", "all");
		if (selectId.charAt(0) != "r") {
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
		} else {
			$("#" + selectId).remove();
			$("#" + selectId.slice(1)).on("click", selectClick);
			$("#" + selectId.slice(1)).trigger("click");
		}
	}
	if ($(this).prop("tagName") == "foreignObject") {
		g.append("foreignObject").attr("x", $(this).attr("x")).attr("y",
				$(this).attr("y")).attr("width", $(this).attr("width")).attr(
				"height", $(this).attr("height")).attr("id", selectId).attr(
				"class", $(this).attr("class")).attr("transform",
				$(this).attr("transform"));
		$('#' + selectId).attr("id", "o" + selectId);
		$('#t' + selectId).attr("id", "ot" + selectId);
		if ($("<b></b>").addClass($("#" + selectId).attr("class")).hasClass(
				"text")) {
			$('#' + selectId).append('<textarea id="t' + selectId + '"/>');
			$("#t" + selectId).val($("#ot" + selectId).val());
			$("#t" + selectId).css("color", $("#ot" + selectId).css("color"));
			$("#t" + selectId).css("backgroundColor",
					$("#ot" + selectId).css("backgroundColor"));
			$("#t" + selectId).css("border", $("#ot" + selectId).css("border"));
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
				$(this).attr("y") - 5).attr("width", "10").attr("height", "10")
				.attr("fill", "yellow").attr("stroke", "red").attr(
						"onmousedown", "startMove(event, 'group')").attr(
						"onmouseup", "endMove()").attr("id", "1").attr(
						"transform", $(this).attr("transform"));
		x1[1] = x1[selectId] - 5;
		y1[1] = y1[selectId] - 5;
		g.append("rect").attr(
				"x",
				parseInt($(this).attr("x")) + parseInt($(this).attr("width"))
						- 5).attr("y", $(this).attr("y") - 5).attr("width",
				"10").attr("height", "10").attr("fill", "yellow").attr(
				"stroke", "red").attr("onmousedown",
				"startMove(event, 'group')").attr("onmouseup", "endMove()")
				.attr("id", "2").attr("transform", $(this).attr("transform"));
		x1[2] = x1[selectId] + parseInt($(this).attr("width")) - 5;
		y1[2] = y1[selectId] - 5;
		g.append("rect").attr("x", $(this).attr("x") - 5).attr(
				"y",
				parseInt($(this).attr("y")) + parseInt($(this).attr("height"))
						- 5).attr("width", "10").attr("height", "10").attr(
				"fill", "yellow").attr("stroke", "red").attr("onmousedown",
				"startMove(event, 'group')").attr("onmouseup", "endMove()")
				.attr("id", "3").attr("transform", $(this).attr("transform"));
		x1[3] = x1[selectId] - 5;
		y1[3] = y1[selectId] + parseInt($(this).attr("height")) - 5;
		g.append("rect").attr(
				"x",
				parseInt($(this).attr("x")) + parseInt($(this).attr("width"))
						- 5).attr(
				"y",
				parseInt($(this).attr("y")) + parseInt($(this).attr("height"))
						- 5).attr("width", "10").attr("height", "10").attr(
				"fill", "yellow").attr("stroke", "red").attr("onmousedown",
				"startMove(event, 'group')").attr("onmouseup", "endMove()")
				.attr("id", "4").attr("transform", $(this).attr("transform"));
		x1[4] = x1[selectId] + parseInt($(this).attr("width")) - 5;
		y1[4] = y1[selectId] + parseInt($(this).attr("height")) - 5;
	}
	$(this).remove();
}
