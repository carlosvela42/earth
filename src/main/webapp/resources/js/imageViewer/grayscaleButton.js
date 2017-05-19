var brightness= 0;
var contrast = 1000;
var newContrast = 1.0;
var newBrightness = 0.0;
var filterChild;
$("#cbox1").prop('checked', true);
$("#btnGrayscale").click(function() {
	$('#controls1').val(contrast);
	$('#controls').val(brightness);
	$('#contrastValue').val(newContrast);
	$('#brightnessValue').val(newBrightness);
	if (checkGrayscale) {
		$("#cbox1").prop('checked', true);
	} else {
		$("#cbox1").prop('checked', false);
	}
});

$("#controls").change(thuy);

$("#controls1").change(thuy);

$("#cbox1").click(thuy);

var x;
function thuy(){
	checkGrayscale = $('#cbox1').prop("checked");
	$('#0').attr("filter","url(#colorMeMatrix)");
	$('#colorMeMatrix').remove();
	
	var filter = vis.append("filter").attr("id", "colorMeMatrix");
	filterChild = filter.append("feColorMatrix").attr("in","SourceGraphic").attr("type","matrix");
	brightness = $('#controls').val();
	newBrightness = (brightness/1000).toFixed(1);
	console.log("brightness : " + newBrightness);
	contrast = $('#controls1').val();
	newContrast = (contrast/1000).toFixed(1);
	console.log("contrast : " + newContrast);
	if(checkGrayscale){
		x = newContrast + " 0 0 0 " + newBrightness +" 0 " + newContrast + " 0 0 "+ newBrightness +" 0 0 " + newContrast + " 0 " + newBrightness + " 0 0 0 1 0";
		filterChild.attr("values", x);
	}else{
		filterChild.attr("values", x);
	}
	$('#contrastValue').val(newContrast);
	$('#brightnessValue').val(newBrightness);
}