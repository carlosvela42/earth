$("#btnGrayscale").click(function() {
	$('#controls').val(contrast);
	$('#controls1').val(brightness);
	if (checkGrayscale) {
		$("#cbox1").prop('checked', true);
	} else {
		$("#cbox1").prop('checked', false);
	}
});

$("#okGrayscale").click(function() {
	contrast = parseInt($('#controls').val());
	brightness = parseInt($('#controls1').val());
	checkGrayscale = cbox1.checked;
	var val;
	if (checkGrayscale) {
		val = (contrast + brightness) / 2 - 50;
		if (val > 50 || val < -50)
			return false;
	} else {
		val = 50;
	}
	$('#container').css("backgroundColor", val > 0 ? 'white' : 'black');
	$('#container').css("opacity", Math.abs(val / 100) * 2);
});