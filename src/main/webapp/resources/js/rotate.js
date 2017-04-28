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