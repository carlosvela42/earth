	IV.prototype.rotateAll = function() {
		imageViewer.startAnnotation();
		imageViewer.redraw();
	}
	var isRotated = false;
	IV.prototype.rotateCClick = function() {
		imageViewer.rotate = (imageViewer.rotate + 270) % 360;
		imageViewer.rotateAll();
	}
	
	IV.prototype.rotateClick = function() {
		imageViewer.rotate = (imageViewer.rotate + 90) % 360;
		imageViewer.rotateAll();
	}
