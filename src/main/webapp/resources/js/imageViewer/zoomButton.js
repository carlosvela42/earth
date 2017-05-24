IV.prototype.zoom = function(zoom) {
	imageViewer.rotate += 90;
	imageViewer.startAnnotation();
	imageViewer.rotate -= 90;
	imageViewer.scale = zoom;
	imageViewer.redraw();
	$('#lbZoom').text(Math.floor(imageViewer.scale * 100) + "%");
	}