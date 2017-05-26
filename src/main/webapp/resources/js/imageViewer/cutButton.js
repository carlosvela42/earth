IV.prototype.cutClick = function() {
		imageViewer.cut = imageViewer.selectId;
		$("#" + imageViewer.selectId).parent().css("visibility", "hidden");
		imageViewer.selectBefore();
	}
