IV.prototype.btnGrayscaleClick = function(){
	$('#controls1').val(imageViewer.contrast);
	$('#controls').val(imageViewer.brightness);
	$('#contrastValue').val(imageViewer.newContrast);
	$('#brightnessValue').val(imageViewer.newBrightness);
	if (imageViewer.checkGrayscale) {
		$("#cbox1").prop('checked', true);
	} else {
		$("#cbox1").prop('checked', false);
	}
}

IV.prototype.thuy = function(){
	imageViewer.checkGrayscale = $('#cbox1').prop("checked");
	$('#0').attr("filter","url(#colorMeMatrix)");
	$('#colorMeMatrix').remove();
	
	var filter = vis.append("filter").attr("id", "colorMeMatrix");
	imageViewer.filterChild = filter.append("feColorMatrix").attr("in","SourceGraphic").attr("type","matrix");
	imageViewer.brightness = $('#controls').val();
	imageViewer.newBrightness = (imageViewer.brightness/1000).toFixed(1);	
	imageViewer.contrast = $('#controls1').val();
	imageViewer.newContrast = (imageViewer.contrast/1000).toFixed(1);	
	if(imageViewer.checkGrayscale){
		imageViewer.xGrayscale = imageViewer.newContrast + " 0 0 0 " + imageViewer.newBrightness +" 0 " + imageViewer.newContrast + " 0 0 "+ imageViewer.newBrightness +" 0 0 " + imageViewer.newContrast + " 0 " + imageViewer.newBrightness + " 0 0 0 1 0";
		imageViewer.filterChild.attr("values", imageViewer.xGrayscale);
	}else{
		imageViewer.filterChild.attr("values", imageViewer.xGrayscale);
	}
	$('#contrastValue').val(imageViewer.newContrast);
	$('#brightnessValue').val(imageViewer.newBrightness);
}