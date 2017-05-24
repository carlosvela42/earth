IV.prototype.printImage = function(image) {
	var windowContent = '<!DOCTYPE html>';
	windowContent += '<html>'
	windowContent += '<head><title>Print</title></head>';
	windowContent += '<body>'
	windowContent += image;
	windowContent += '</body>';
	windowContent += '</html>';
	var printWin = window.open('', '');
	printWin.document.open();
	printWin.document.write(windowContent);
	printWin.document.close();
	printWin.focus();
	printWin.print();
	printWin.close();
}

IV.prototype.printAnno = function(i){
	var strPrint = "<script type='text/javascript'>";
	for (var j = 5; j <= i; j++) {
		if ($("#t" + j).length) {
			var valPrint = $("#t" + j).val();
			strPrint += "document.getElementById('t' +" + j + ").value='" + valPrint + "';";
		}
	}
	strPrint += "</script>";
	var tmpRotate = imageViewer.rotate;
	var tmpScale = imageViewer.scale;
	imageViewer.rotate = 0;
	imageViewer.scale = 1;
	imageViewer.startAnnotation();	
	imageViewer.redraw();
	var tmpSelect = imageViewer.selectId;
	imageViewer.selectBefore();
	imageViewer.printImage(document.getElementById("container").innerHTML + strPrint);
	imageViewer.rotate = tmpRotate+90;
	imageViewer.scale = tmpScale;	
	imageViewer.startAnnotation();	
	imageViewer.rotate -= 90;
	imageViewer.redraw();
	$("#"+tmpSelect).trigger("click");
}

IV.prototype.print0Click = function() {
	imageViewer.printImage('<img src="' + imageViewer.documentImage + '">');
}

IV.prototype.printClick = function() {
	imageViewer.printAnno(imageViewer.i);
}