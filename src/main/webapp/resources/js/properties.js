$("#color").change(function() {
	$("#commentText").css("color", $("#color").val());
});

$("#fill").change(function() {
	$("#commentText").css("backgroundColor", $("#fill").val());
});

$("#border").change(function() {
	$("#commentText").css("border", $("#border").val());
});

$("#font").change(function() {
	$("#commentText").css("font-family", $("#font").val());
});

$("#fontSize").change(function() {
	$("#commentText").css("fontSize", $("#fontSize").val());
});

$("#fontStyle").change(function() {
	if ($("#fontStyle").val() == "normal") {
		$("#commentText").css("font-style", "normal");
		$("#commentText").css("font-weight", "");
	}
	if ($("#fontStyle").val() == "italic") {
		$("#commentText").css("font-style", "italic");
		$("#commentText").css("font-weight", "");
	}
	if ($("#fontStyle").val() == "boldItalic") {
		$("#commentText").css("font-style", "italic");
		$("#commentText").css("font-weight", "bold");
	}
	if ($("#fontStyle").val() == "bold") {
		$("#commentText").css("font-weight", "bold");
		$("#commentText").css("font-style", "");
	}
});

function property(selectId) {
	if ($("#1").length == 0) {
		$("#" + penColor).prop("checked", true);
		$("#1" + fillColor).prop("checked", true);
		$("#2" + highlightColor).prop("checked", true);
		$("#width").val(lineSize);
		$("#myModalLine").modal();
	} else {
		if ($("#" + selectId).prop("tagName") == "foreignObject") {
			if ($("#i" + selectId).length == 0) {
				$("#commentText").val($("#t" + selectId).val());
				$("#commentText").css("color", $("#t" + selectId).css("color"));
				$("#commentText").css("font-family",
						$("#t" + selectId).css("font-family"));
				$("#commentText").css("fontSize",
						$("#t" + selectId).css("fontSize"));
				$("#commentText").css("font-style",
						$("#t" + selectId).css("font-style"));
				$("#color").val($("#t" + selectId).css("color"));
				$("#fill").val($("#t" + selectId).css("backgroundColor"));
				$("#border").val($("#t" + selectId).css("border"));
				$("#font").val($("#t" + selectId).css("font-family"));
				$("#fontSize").val($("#t" + selectId).css("fontSize"));
				if ($("#t" + selectId).css("font-style") == "normal") {
					$("#fontStyle").val("normal");
				}
				if ($("#t" + selectId).css("font-style") == "italic") {
					$("#fontStyle").val("italic");
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
			$("#" + $("#" + selectId).attr("stroke")).prop("checked", true);
			$("#width").val($("#" + selectId).attr("stroke-width"));
			$("#1" + $("#" + selectId).attr("fill")).prop("checked", true);
			$("#2" + $("#" + selectId).attr("fill")).prop("checked", true);
			$("#myModalLine").modal();
		}
	}
}

function textProperty(selectId) {
	$("#t" + selectId).val($("#commentText").val());
	$("#t" + selectId).css("color", $('#color').find(":selected").text());
	$("#t" + selectId).css("backgroundColor",
			$('#fill').find(":selected").text());
	$("#t" + selectId).css("border",
			"1px solid " + $('#border').find(":selected").text());
	$("#t" + selectId).css("font-family", $('#font').find(":selected").text());
	$("#t" + selectId).css("fontSize", $('#fontSize').find(":selected").text());
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
}

function okProperties(selectId) {
	if ($("#1").length == 0) {
		penColor = $('input[name=color]:checked').val();	
		fillColor = $('input[name=fill]:checked').val();	
		highlightColor = $('input[name=highlight]:checked').val();
		lineSize = $('#width').val();
	} else {
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
				$("#" + selectId).attr("stroke-width", $('#width').val());
			}
		}
	}
}

$("#properties").click(function() {
	property(selectId);
});

$("#commentProperties").click(function() {
	$("#t" + selectId).val($("#tbComment").val());
});

$("#textProperties").click(function() {
	textProperty(selectId);
});

$("#okProperties").click(function() {
	okProperties(selectId);
});
$('input[name=color]').change(function(){
	$('#example').attr('stroke',$('input[name=color]:checked').val());
	$("#color").val($( "#color option:selected" ).text());	
});
$('input[name=fill]').change(function(){
	$('#example').attr('fill',$('input[name=fill]:checked').val());
});
$('#width').change(function(){
	$('#example').attr('stroke-width',$('#width').val());
});
