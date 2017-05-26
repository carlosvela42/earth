IV.prototype.startLoad = function(workspaceId,workitemId,folderItemNo,documentNo) {
	$.ajax({
		type : "GET",
		url : IV.url("WS/displayImage?workspaceId="+workspaceId+"&workitemId="+workitemId+"&folderItemNo="+folderItemNo+"&documentNo="+documentNo),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
			imageViewer.documents = data.result;
			for(var j = 0;j<imageViewer.documents.length;j++){
				if(imageViewer.documents[j].documentNo == documentNo){
					return imageViewer.swapImage(j + 1);
				}
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("Failure");
		}
	});
}

IV.prototype.load = function() {
	for (var k = 0; k < imageViewer.layerCount; k++) {
		var layer = imageViewer.documents[imageViewer.cDocId-1].layers[k];
		imageViewer.layerName[k] = layer.layerNo;
		imageViewer.layerOwner[k] = layer.ownerId;
		imageViewer.layerDisplay[k] = true;
		imageViewer.layerModified[k] = layer.lastUpdateTime;
		imageViewer.layerCreated[k] = layer.lastUpdateTime;
		var userInfo = $('#userInfo').text();
		if(imageViewer.layerOwner[k]==userInfo.slice(0,userInfo.indexOf("/")))
		{
			imageViewer.existUser = true;
			imageViewer.layerActive = imageViewer.layerName[k];
		}
		imageViewer.addNewLayer(k + 1, imageViewer.layerName[k], imageViewer.layerOwner[k], imageViewer.layerDisplay[k], imageViewer.layerModified[k], imageViewer.layerCreated[k], imageViewer.layerActive);	
//		xmlDoc = $.parseXML( layer.annotations );
//		if($(xmlDoc).find('line').attr('id').charAt(0)!="r"){		
//			x1[$(xmlDoc).find('line').attr('id')] = $(xmlDoc).find('line').attr('x1');
//			y1[$(xmlDoc).find('line').attr('id')] = $(xmlDoc).find('line').attr('y1');
//			x2[$(xmlDoc).find('line').attr('id')] = $(xmlDoc).find('line').attr('x2');
//			y2[$(xmlDoc).find('line').attr('id')] = $(xmlDoc).find('line').attr('y2');
//		}
//		$('svg').append(layer.annotations);
//		if ($(xmlDoc).find('line').attr('id') > i) {
//		i = $(xmlDoc).find('line').attr('id');
//	}
		var objAnnotations = jQuery.parseJSON( layer.annotations );
		if(objAnnotations!=null){
		var annotationsLength = objAnnotations.length;
		for (var j = 0; j < annotationsLength; j++) {
			var counter = objAnnotations[j];
			
			imageViewer.x1[counter.id] = parseInt(counter.x1);
			imageViewer.y1[counter.id] = parseInt(counter.y1);
			imageViewer.x2[counter.id] = parseInt(counter.x2);
			imageViewer.y2[counter.id] = parseInt(counter.y2);
			
			if (counter.type == "line") {
				imageViewer.vis.append("line").attr("x1", counter.x1)
						.attr("y1", counter.y1).attr("x2", counter.x2).attr(
								"y2", counter.y2).attr("id", counter.id).attr(
								"stroke", counter.stroke).attr("stroke-width",
								counter.strokew).attr("class",
								counter.class + " " + imageViewer.layerName[k]).attr(
								"transform", "rotate(0) scale(1)").on("click",
										imageViewer.selectClick);
				var tempX,tempY;
				if(counter.x2>=counter.x1){ tempX = counter.x1;}else{tempX = counter.x2;}
				if(counter.y2>=counter.y1){ tempY = counter.y1;}else{tempY = counter.y2;}
				imageViewer.vis.append("rect").attr("x", tempX).attr("y", tempY).attr("width", Math.abs(counter.x2-counter.x1))
				.attr("height", Math.abs(counter.y2-counter.y1)).attr("id", "r"+counter.id).attr("fill", "none").attr("class", counter.class + " " + imageViewer.layerName[k]).on(
						"click", imageViewer.selectClick).attr("pointer-events", "all").attr("transform",
								"rotate(0) scale(1)");
			}
			if (counter.type == "rect") {
				imageViewer.vis.append("rect").attr("x", counter.x1).attr("y", counter.y1)
						.attr("width", counter.x2).attr("height", counter.y2)
						.attr("id", counter.id).attr("stroke", counter.stroke)
						.attr("stroke-width", counter.strokew).attr("fill",
								counter.fill).attr("class",
								counter.class + " " + imageViewer.layerName[k]).attr(
								"transform", "rotate(0) scale(1)").on("click",
										imageViewer.selectClick).attr("pointer-events", "all");
			}
			if (counter.id > imageViewer.i) {
				imageViewer.i = counter.id;
			}
		}}
	}
	
	if(!imageViewer.existUser)
	{	
		var j =0;
		do
		{			
			var tmpLayerName = "DEF"+j;
			var existName = false; 			
			for (var k = 0; k < imageViewer.layerCount; k++) {
				if(imageViewer.layerName[k]==tmpLayerName)
				{
					k = imageViewer.layerCount;
					existName = true;
				}
			}
			j++;
			if(!existName)
			{
				imageViewer.addNewLayer(imageViewer.layerCount+1, tmpLayerName,
						$('#userInfo').text().slice(0,$('#userInfo').text().indexOf("/")), true, 2017, 2017, tmpLayerName);
				imageViewer.layerName[imageViewer.layerCount] = tmpLayerName;
				imageViewer.layerOwner[imageViewer.layerCount] = $('#userInfo').text().slice(0,$('#userInfo').text().indexOf("/"));
				imageViewer.layerDisplay[imageViewer.layerCount] = true;				
				imageViewer.layerModified[imageViewer.layerCount] = 2017;
				imageViewer.layerCreated[imageViewer.layerCount] = 2017;
				imageViewer.layerCount += 1;
				imageViewer.layerActive = tmpLayerName;
				j = -1;
			}
		}
		 while (j!=-1);
	}

	var img = new Image();
	img.onload = function() {
		imageViewer.x2[0] = -parseInt(this.width);
		imageViewer.y2[0] = -parseInt(this.height);
		if(($('#container').width()-getScrollBarWidth())/-imageViewer.x2[0]>($('#container').height()-getScrollBarWidth())/-imageViewer.y2[0]){
			imageViewer.zoom(($('#container').height()-getScrollBarWidth())/-imageViewer.y2[0]);
		}
		else
		{
			imageViewer.zoom(($('#container').width()-getScrollBarWidth())/-imageViewer.x2[0]);
		}
	}
	img.src = imageViewer.documentImage;
	
	$('input[name=toolOption]').change(function() {
		if ($("#highlight").prop("checked")) 
		{
			$('#svg').css('cursor','url("resources/images/imageViewer/Pen-50.png"), auto');
		}
		else
		{
			$('#svg').css('cursor','auto');
		}
	});
}
IV.prototype.switchImage = function(){
	imageViewer.documentsLength = imageViewer.documents.length;
	if(imageViewer.documentsLength==1){
		$('#next').prop('disabled', true);
		$('#last').prop('disabled', true);
	}
	else{
		for(var j=1;j<=imageViewer.documentsLength;j++){
		$('#chooseImage').append('<li><a href="#" onclick="imageViewer.swapImage(\''+j+'\')">#'+j+'</a></li>');
	}
		}
}