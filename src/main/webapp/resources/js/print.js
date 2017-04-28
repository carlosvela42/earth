function printImage(image) {
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

$("#print0").click(function() {
	printImage('<img src="' + jsonLayer.Image + '">');
});

function printAnno(i){
	var strPrint = "<script type='text/javascript'>";
	for (var j = 5; j <= i; j++) {
		if ($("#t" + j).length) {
			var valPrint = $("#t" + j).val();
			strPrint += "document.getElementById('t' +" + j + ").value='" + valPrint + "';";
		}
	}
	strPrint += "</script>";
	var tmpRotate = rotate;
	var tmpScale = scale;
	rotate = 0;
	scale = 1;
	startAnnotation();	
	redraw();
	var tmpSelect = selectId;
	selectBefore();
	printImage(document.getElementById("container").innerHTML + strPrint);
	rotate = tmpRotate+90;
	scale = tmpScale;	
	startAnnotation();	
	rotate -= 90;
	redraw();
	$("#"+tmpSelect).trigger("click");
}

$("#print").click(function() {
	printAnno(i);
});