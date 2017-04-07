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
			$("#commentText").css("text-decoration", "");
			$("#commentText").css("font-weight", "");
		}
		if ($("#fontStyle").val() == "italic") {
			$("#commentText").css("font-style", "italic");
			$("#commentText").css("text-decoration", "");
			$("#commentText").css("font-weight", "");
		}
		if ($("#fontStyle").val() == "underline") {
			$("#commentText").css("text-decoration", "underline");
			$("#commentText").css("font-style", "");
			$("#commentText").css("font-weight", "");
		}
		if ($("#fontStyle").val() == "bold") {
			$("#commentText").css("font-weight", "bold");
			$("#commentText").css("font-style", "");
			$("#commentText").css("text-decoration", "");
		}
	});