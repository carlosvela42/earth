$( "#imageViewerWrapper" ).load( "imageviewer/template" , function() {
  imageViewer = new IV();
	imageViewer.container = d3.select('body').append('div').attr('id', 'container');
	$('#container').width('800px');
	$('#container').height('800px');
  imageViewer.startLoad("003","1","2","3");
  imageViewer.vis = imageViewer.container.append('svg').on("mousedown", imageViewer.mousedown).on("mouseup",
		  imageViewer.mouseup).attr("id", "svg");
  $("input[name=toolOption]").on("click", imageViewer.selectBefore);
  $("#first").click(imageViewer.firstClick);
	$("#previous").click(imageViewer.previousClick);
	$("#next").click(imageViewer.nextClick);
	$("#last").click(imageViewer.lastClick);
	$("#rotate").click(imageViewer.rotateClick);
	$("#rotateC").click(imageViewer.rotateCClick);	
	$("#cut").click(imageViewer.cutClick);	
	$("#copy").click(imageViewer.copyClick);	
	$("#paste").click(imageViewer.pasteClick);	
	$("#print0").click(imageViewer.print0Click);	
	$("#print").click(imageViewer.printClick);	
	$("#addLayer").click(imageViewer.addLayerClick);	
	$("#removeLayer").click(imageViewer.removeLayerClick);	
	$("#renameLayer").click(imageViewer.renameLayerClick);	
	$("#activeLayer").click(imageViewer.activeLayerClick);	
	$("#displayLayer").click(imageViewer.displayLayerClick);	
	$("#btnLayer").click(imageViewer.btnLayerClick);	
	$("#okLayer").click(imageViewer.okLayerClick);
	$("#select").click(imageViewer.selectBtnClick);
	$("#color").change(imageViewer.colorChange);
	$("#fill").change(imageViewer.fillChange);
	$("#border").change(imageViewer.borderChange);
	$("#font").change(imageViewer.fontChange);
	$("#fontSize").change(imageViewer.fontSizeChange);
	$("#fontStyle").change(imageViewer.fontStyleChange);
	$("#properties").click(imageViewer.propertiesClick);
	$("#commentProperties").click(imageViewer.commentPropertiesClick);
	$("#textProperties").click(imageViewer.textPropertiesClick);
	$("#okProperties").click(imageViewer.okPropertiesClick);
	$('input[name=color]').change(imageViewer.inputColorChange);
	$('input[name=fill]').change(imageViewer.inputFillChange);
	$('#width').change(imageViewer.widthChange);
	$('input[type=radio][name=active]').change(imageViewer.activeChange);
	$("#cbox1").prop('checked', true);
	$("#controls").change(imageViewer.thuy);
	$("#controls1").change(imageViewer.thuy);
	$("#cbox1").click(imageViewer.thuy);
	$("#btnGrayscale").click(imageViewer.btnGrayscaleClick);
	$("#line").click(imageViewer.lineClick);
	$("#rectangle").click(imageViewer.rectangleClick);
	$("#comment").click(imageViewer.commentClick);
	$("#highlight").click(imageViewer.highlightClick);
	$("#text").click(imageViewer.textClick);
	$("#zoomout").click(function() {
		imageViewer.zoom(Math.ceil10(imageViewer.scale * 10, -2) / 10 - 0.1);
	});

	$("#zoomin").click(function() {
		imageViewer.zoom(Math.floor10(imageViewer.scale * 10, -2) / 10 + 0.1);
	});
	
	$("#zoomFullPage").click(function() {		
		if(($('#container').width()-getScrollBarWidth())/-imageViewer.x2[0]>($('#container').height()-getScrollBarWidth())/-imageViewer.y2[0]){
			imageViewer.zoom(($('#container').height()-getScrollBarWidth())/-imageViewer.y2[0]);
		}
		else
		{
			imageViewer.zoom(($('#container').width()-getScrollBarWidth())/-imageViewer.x2[0]);
		}
	});
	
	$("#zoomFullWidth").click(function() {
		imageViewer.zoom(($('#container').width()-getScrollBarWidth())/-imageViewer.x2[0]);
	});

	$("#zoom200").click(function() {
		imageViewer.zoom(2);
	});
	
	$("#zoom100").click(function() {
		imageViewer.zoom(1);
	});
	
	$("#zoom75").click(function() {
		imageViewer.zoom(0.75);
	});
	
	$("#zoom50").click(function() {
		imageViewer.zoom(0.5);
	});

	$('#controlZoom').onkeyup = controlZoom.onchange = function() {
		var val = parseInt(this.value);
		if (val > 100 || val < 0)
			return false;
		if (val > 50) {
			imageViewer.zoom(0.06 * val - 2);
		} else {
			imageViewer.zoom(val / 50);
		}
	}
});