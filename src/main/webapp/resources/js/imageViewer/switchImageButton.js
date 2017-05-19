function changeDocument() {
	var documents = imageViewer.documents;
	var cDocId = imageViewer.cDocId;
	var x2 = imageViewer.x2;
	var y2 = imageViewer.y2;
	var vis = imageViewer.vis;
	var load = imageViewer.load;
	var switchImage = imageViewer.switchImage;
	currentDocument = documents[cDocId-1];
	documentImage = jQuery.parseJSON(currentDocument.viewInformation).Image;
	switch (currentDocument.documentType) {
	case "1":
		var img = new Image();
		img.onload = function() {
			x2[0] = -parseInt(this.width);
			y2[0] = -parseInt(this.height);
			vis.append("image").attr("x", 0).attr("y", 0).attr("xlink:href",documentImage).attr(
					"id", "0").attr("transform", "rotate(0) scale(1)").attr(
					"width", -x2[0]).attr("height", -y2[0]);
			load();
			switchImage();
		}
		img.src = documentImage;
		break;
	case "3":
		$(function() {
			Tiff.initialize({
				TOTAL_MEMORY : 16777216 * 10
			});
			var xhr = new XMLHttpRequest();
			xhr.open('GET', documentImage);
			xhr.responseType = 'arraybuffer';
			xhr.onload = function(e) {
				var buffer = xhr.response;
				var tiff = new Tiff({
					buffer : buffer
				});
				tiff.setDirectory(currentDocument.pageCount-1);
				var canvas = tiff.toCanvas();
				documentImage = canvas.toDataURL("image/png");
				var img = new Image();
				img.onload = function() {
					x2[0] = -parseInt(this.width);
					y2[0] = -parseInt(this.height);
						vis.append("image").attr("x", 0).attr("y", 0).attr("xlink:href", documentImage).attr("id", "0").attr("transform","rotate(0) scale(1)").attr("width",-x2[0]).attr("height",-y2[0]);
						load();
						switchImage();
						}
				img.src = documentImage;
			 
//				vis.append("foreignObject").attr("id", "0").attr(
//						"transform", "rotate(0) scale(1)");
//				$('#0').append(canvas);			
			
				return;
				
			};
			xhr.send();
		});
		break;
	}
	layerCount = currentDocument.layers.length;
	loadBefore();
}
function loadBefore() {
	// luu annotation voi anh truoc->xoa trang object, reset page	
	$('#chooseImage').empty();
	$("#svg").empty();
}

function save(){
	for(var j=0;j<layerCount;j++){
	documents[cDocId-1].layers[j].layerNo = layerName[j];
	documents[cDocId-1].layers[j].ownerId = layerOwner[j];
	documents[cDocId-1].layers[j].lastUpdateTime = layerModified[j];
	documents[cDocId-1].layers[j].lastUpdateTime = layerCreated[j];
	}
}

function swapImage(imageId) {	
	if (imageViewer.cDocId != imageId) {
		imageViewer.cDocId = imageId;
		changeDocument();
		$('#next').prop('disabled', false);
		$('#last').prop('disabled', false);
		$('#first').prop('disabled', false);
		$('#previous').prop('disabled', false);
		if (imageViewer.cDocId == 1) {
			$('#first').prop('disabled', true);
			$('#previous').prop('disabled', true);
		}
		if (imageViewer.cDocId == documentsLength) {
			$('#next').prop('disabled', true);
			$('#last').prop('disabled', true);
		}
		$('#btnImage').text('#' + imageViewer.cDocId);
	}
}
