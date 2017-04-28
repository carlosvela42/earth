$("#cut").click(function() {
	cut = selectId;
	$("#" + selectId).parent().css("visibility", "hidden");
	selectBefore();
});