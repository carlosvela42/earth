IV.prototype.startLoad = function() {
	$.ajax({
		type : "GET",
		url : IV.url("WS/displayImage?workspaceId=003"),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
			imageViewer.documents = data.result;
			imageViewer.currentDocument =  imageViewer.documents[0];
			imageViewer.documentType = imageViewer.currentDocument.documentType;
			var documentImage = imageViewer.documentImage = IV.url(jQuery.parseJSON( imageViewer.currentDocument.viewInformation ).Image);
			var x2 = imageViewer.x2;
			var y2 = imageViewer.y2;
			switch(imageViewer.documentType){
			case "1":
			var img = new Image();
			img.onload = function() {
				x2[0] = -parseInt(this.width);
				y2[0] = -parseInt(this.height);				
				imageViewer.vis.append("image").attr("x", 0).attr("y", 0).attr("xlink:href", documentImage).attr("id", "0").attr("transform","rotate(0) scale(1)").attr("width",-x2[0]).attr("height",-y2[0]);
					imageViewer.load();
					imageViewer.switchImage();
					}
			img.src = documentImage;
					break;
			case "2":
				var url = documentImage;

				// The workerSrc property shall be specified.
				PDFJS.workerSrc = IV.url('resources/js/lib/pdf.worker.js');
				var pdfDoc = null,
				    pageNum = 1,
				    pageRendering = false,
				    pageNumPending = null,
				    scale = 0.8,
				    canvas = document.getElementById('the-canvas'),
				    ctx = canvas.getContext('2d');
				documentImage = canvas.toDataURL("image/png");
				var img = new Image();
				img.onload = function() {
					x2[0] = -parseInt(this.width);
					y2[0] = -parseInt(this.height);
						vis.append("image").attr("x", 0).attr("y", 0).attr("xlink:href", documentImage).attr("id", "0").attr("transform","rotate(0) scale(1)").attr("width",-x2[0]).attr("height",-y2[0]);
						imageViewer.load();
						imageViewer.switchImage();
						}
				img.src = documentImage;
		      imageViewer.load();
		      imageViewer.switchImage();

				/**
				 * Get page info from document, resize canvas accordingly, and render page.
				 * @param num Page number.
				 */
				function renderPage(num) {
				  pageRendering = true;
				  // Using promise to fetch the page
				  pdfDoc.getPage(num).then(function(page) {
				    var viewport = page.getViewport(scale);
				    canvas.height = viewport.height;
				    canvas.width = viewport.width;

				    // Render PDF page into ca0nvas context
				    var renderContext = {
				      canvasContext: ctx,
				      viewport: viewport
				    };
				    var renderTask = page.render(renderContext);

				    // Wait for rendering to finish
				    renderTask.promise.then(function() {
				      pageRendering = false;
				      if (pageNumPending !== null) {
				        // New page rendering is pending
				        renderPage(pageNumPending);
				        pageNumPending = null;
				      }
				    });
				  });
				  // Update page counters
				  //document.getElementById('page_num').textContent = pageNum;
				}

				/**
				 * If another page rendering in progress, waits until the rendering is
				 * finised. Otherwise, executes rendering immediately.
				 */
				function queueRenderPage(num) {
				  if (pageRendering) {
				    pageNumPending = num;
				  } else {
				    renderPage(num);
				  }
				}

				/**
				 * Displays previous page.
				 */
				function onPrevPage() {
				  if (pageNum <= 1) {
				    return;
				  }
				  pageNum--;
				  queueRenderPage(pageNum);
				}
				document.getElementById('previous').addEventListener('click', onPrevPage);

				/**
				 * Displays next page.
				 */
				function onNextPage() {
				  if (pageNum >= pdfDoc.numPages) {
				    return;
				  }
				  pageNum++;
				  queueRenderPage(pageNum);
				}
				document.getElementById('next').addEventListener('click', onNextPage);

				/**
				 * Asynchronously downloads PDF.
				 */
				PDFJS.getDocument(url).then(function(pdfDoc_) {
				  pdfDoc = pdfDoc_;
				  //document.getElementById('page_count').textContent = pdfDoc.numPages;

				  // Initial/first page rendering
				  renderPage(pageNum);
				});
				break;
				case "3":
					$(function () {
						  Tiff.initialize({TOTAL_MEMORY: 16777216 * 10});
						  var xhr = new XMLHttpRequest();
						  xhr.open('GET', documentImage);
						  xhr.responseType = 'arraybuffer';
						  xhr.onload = function (e) {
						    var buffer = xhr.response;
						    var tiff = new Tiff({buffer: buffer});
						      tiff.setDirectory(0);
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
						      //vis.append("foreignObject").attr("id", "0").attr("transform","rotate(0) scale(1)");
						      //$('#0').append(canvas);
								imageViewer.load();
						      switchImage();
						      return;
						  };
						  xhr.send();
						});
					break;
		}
			imageViewer.layerCount = imageViewer.currentDocument.layers.length;
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("Failure");
		}
	});
}

IV.prototype.load = function() {	
	var currentDocument = imageViewer.currentDocument;
	var layerOwner = imageViewer.layerOwner;
	var layerDisplay = imageViewer.layerDisplay;
	var layerModified = imageViewer.layerModified;
	var layerCreated = imageViewer.layerCreated;	
	
	for (var k = 0; k < imageViewer.layerCount; k++) {
		var layer = currentDocument.layers[k];
		layerName[k] = layer.layerNo;
		layerOwner[k] = layer.ownerId;
		layerDisplay[k] = true;
		layerModified[k] = layer.lastUpdateTime;
		layerCreated[k] = layer.lastUpdateTime;
		var userInfo = $('#userInfo').text();
		if(layerOwner[k]==userInfo.slice(0,userInfo.indexOf("/")))
		{
			this.existUser = true;
			this.layerActive = layerName[k];
		}
		IV.addNewLayer(k + 1, layerName[k], layerOwner[k], layerDisplay[k], layerModified[k], layerCreated[k], layerActive);	
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
			var x1 = imageViewer.x1;
			var x2 = imageViewer.x2;
			var y1 = imageViewer.y1;
			var y2 = imageViewer.y2;
			var layer = imageViewer.layer;
			var vis = imageViewer.vis;
			var selectClick = imageViewer.selectClick;
			
			x1[counter.id] = parseInt(counter.x1);
			y1[counter.id] = parseInt(counter.y1);
			x2[counter.id] = parseInt(counter.x2);
			y2[counter.id] = parseInt(counter.y2);
			
			if (counter.type == "line") {
				vis.append("line").attr("x1", counter.x1)
						.attr("y1", counter.y1).attr("x2", counter.x2).attr(
								"y2", counter.y2).attr("id", counter.id).attr(
								"stroke", counter.stroke).attr("stroke-width",
								counter.strokew).attr("class",
								counter.class + " " + layerName[k]).attr(
								"transform", "rotate(0) scale(1)").on("click",
								selectClick);
				var tempX,tempY;
				if(counter.x2>=counter.x1){ tempX = counter.x1;}else{tempX = counter.x2;}
				if(counter.y2>=counter.y1){ tempY = counter.y1;}else{tempY = counter.y2;}
				vis.append("rect").attr("x", tempX).attr("y", tempY).attr("width", Math.abs(counter.x2-counter.x1))
				.attr("height", Math.abs(counter.y2-counter.y1)).attr("id", "r"+counter.id).attr("fill", "none").attr("class", counter.class + " " + layerName[k]).on(
						"click", selectClick).attr("pointer-events", "all").attr("transform",
								"rotate(0) scale(1)");
			}
			if (counter.type == "rect") {
				vis.append("rect").attr("x", counter.x1).attr("y", counter.y1)
						.attr("width", counter.x2).attr("height", counter.y2)
						.attr("id", counter.id).attr("stroke", counter.stroke)
						.attr("stroke-width", counter.strokew).attr("fill",
								counter.fill).attr("class",
								counter.class + " " + layerName[k]).attr(
								"transform", "rotate(0) scale(1)").on("click",
								selectClick).attr("pointer-events", "all");
			}
			if (counter.id > this.i) {
				this.i = counter.id;
			}
		}}
	}
	
	if(!this.existUser)
	{	
		var j =0;
		do
		{			
			var tmpLayerName = "DEF"+j;
			var existName = false; 
			var layerCount = this.layerCount;
			for (var k = 0; k < layerCount; k++) {
				if(layerName[k]==tmpLayerName)
				{
					k = layerCount;
					existName = true;
				}
			}
			j++;
			if(!existName)
			{
				IV.addNewLayer(this.layerCount+1, tmpLayerName,
						$('#userInfo').text().slice(0,$('#userInfo').text().indexOf("/")), true, 2017, 2017, tmpLayerName);				
				this.layerName[layerCount] = tmpLayerName;
				this.layerOwner[layerCount] = $('#userInfo').text().slice(0,$('#userInfo').text().indexOf("/"));
				this.layerDisplay[layerCount] = true;				
				this.layerModified[layerCount] = 2017;
				this.layerCreated[layerCount] = 2017;
				this.layerCount += 1;
				this.layerActive = tmpLayerName;
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
	img.src = this.documentImage;
	
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
	documentsLength = imageViewer.documents.length;
	if(documentsLength==1){
		$('#next').prop('disabled', true);
		$('#last').prop('disabled', true);
	}
	else{
		for(var j=1;j<=documentsLength;j++){
		$('#chooseImage').append('<li><a href="#" onclick="swapImage(\''+j+'\')">#'+j+'</a></li>');
	}
		}
}