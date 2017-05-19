$( "#imageViewerWrapper" ).load( "imageviewer/template" , function() {
  console.log('success');

  imageViewer = new IV();
  imageViewer.init();
  imageViewer.vis = container.append('svg').on("mousedown", imageViewer.mousedown).on("mouseup",
			mouseup).attr("id", "svg");
  $("input[name=toolOption]").on("click", imageViewer.selectBefore);
  $("#first").click(function() {
		imageViewer.cDocId = 1;
		changeDocument();
		$('#first').prop('disabled', true);
		$('#previous').prop('disabled', true);
		$('#next').prop('disabled', false);
		$('#last').prop('disabled', false);
		$('#btnImage').text('#1');
	});
	$("#previous").click(function() {
		imageViewer.cDocId--;
		changeDocument();
		$('#next').prop('disabled', false);
		$('#last').prop('disabled', false);
		if (imageViewer.cDocId == 1) {
			$('#first').prop('disabled', true);
			$('#previous').prop('disabled', true);
		}
		$('#btnImage').text('#' + imageViewer.cDocId);
	});
	$("#next").click(function() {
		imageViewer.cDocId++;
		changeDocument();
		$('#first').prop('disabled', false);
		$('#previous').prop('disabled', false);
		if (imageViewer.cDocId == documentsLength) {
			$('#next').prop('disabled', true);
			$('#last').prop('disabled', true);
		}
		$('#btnImage').text('#' + imageViewer.cDocId);
	});
	$("#last").click(function() {
		imageViewer.cDocId = documentsLength;
		changeDocument();
		$('#first').prop('disabled', false);
		$('#previous').prop('disabled', false);
		$('#next').prop('disabled', true);
		$('#last').prop('disabled', true);
		$('#btnImage').text('#' + imageViewer.cDocId);
	});
	$("#rotate").click(function() {
		imageViewer.rotate = (imageViewer.rotate + 90) % 360;
		rotateAll();
	});

	$("#rotateC").click(function() {
		imageViewer.rotate = (imageViewer.rotate + 270) % 360;
		rotateAll();
	});
	
	$("#cut").click(function() {
		imageViewer.cut = imageViewer.selectId;
		$("#" + imageViewer.selectId).parent().css("visibility", "hidden");
		imageViewer.selectBefore();
	});
	
	$("#copy").click(
			function() {
				var im = imageViewer;
				var selectId = im.selectId;
				im.i++;
				im.rotate += 90;
				im.startAnnotation();
				im.rotate -= 90;
				im.redraw();
				if($("#r" + im.selectId).length==0){
				$("#" + im.selectId).clone().appendTo("svg").attr("id", im.i).attr(
						"onmousedown", null).attr("onmouseup", null).on(
						"click", im.selectClick);}else{
							$("#" + selectId).clone().appendTo("svg").attr("id", im.i).attr(
									"onmousedown", null).attr("onmouseup", null);
				$("#r" + selectId).clone().appendTo("svg").attr("id", "r"+im.i).attr(
						"onmousedown", null).attr("onmouseup", null).on(
						"click", im.selectClick);}
				im.x1[i] = im.x1[selectId];
				im.x2[i] = im.x2[selectId];
				im.y1[i] = im.y1[selectId];
				im.y2[i] = im.y2[selectId];
				$("#" + im.selectId).parent().css("visibility", "hidden");
				im.cut = selectId;
			});
	
	$("#paste").click(function() {
		$("#" + imageViewer.cut).css("visibility", "visible");
		$("#" + imageViewer.cut).parent().css("visibility", "visible");
	});
	
	$("#print0").click(function() {
		printImage('<img src="' + jQuery.parseJSON( imageViewer.jsonLayer.viewInformation ).Image + '">');
	});
	
	$("#print").click(function() {
		printAnno(imageViewer.i);
	});
});