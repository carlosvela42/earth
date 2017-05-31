iv = new ImageViewer();

function initImageViewer() {
	// Check Browser support SVG or not.
	if (SVG.supported) {
		iv.svg = new SVG('imageDrawing');
		iv.matrix = new SVG.Matrix;
		// Default annotation is Rectangle.
		iv.settingAnno = iv.svg.rect();
	} else {
		alert('SVG not supported');
		return;
	}
	
	// Load template tool bar and display them onto image viewer screen.
	loadTemplate("template", function(data, res) {
		if (res == result.SUCCESS) {
			$("#toolBar").html(data);
			// Tool bars onClick event.
			
			for (let item in toolbars) {
				$(toolbars[item]).click(onClick);
			}
			
			
			for (let annoType in annoTypes) {
				$(annoTypes[annoType]).click(onClick);
			}

			iv.svg.svgId = "#" + iv.svg.node.id;
			iv.svgStartLeft = $(iv.svg.svgId).position().left;
			iv.svgStartTop = $(iv.svg.svgId).position().top;
			
//			var offset = imageDrawing.position();
//			iv.svgStartX = offset.left;
//			iv.svgStartY = offset.top;
//			console.log("Image offset top:" + offset.top);
		}
	});
	
	// Load list of documents and display them on the image viewer screen.
	loadDocument("http://localhost:8080/Earth/WS/displayImage?workspaceId=003&workitemId=1&folderItemNo=2&documentNo=3", function(data, res) {
		if (res == result.SUCCESS) {
			iv.documents = data.result;
			iv.currentDocument =  iv.documents[0];
			iv.documentType = iv.currentDocument.documentType;
			iv.documentPath = getResource(jQuery.parseJSON(iv.currentDocument.viewInformation).Image);
			switch (iv.documentType) {
				case documentType.IMAGE:
					displayImage();
					break;
				case documentType.PDF:
					convertPdfToImage();
					break;
				case documentType.TIFF:
					convertTiffToImage();
					break;
			}
		}
	});
	
	// Register all events.
	// Keyboard event.
	$(document).keydown(function(e) {
		onKeyDown(e);
	});
}

function displayImage() {
	if (iv.documentPath != null) {
		var img = new Image();
		img.onload = function(){
			iv.svg.size(this.width, this.height);
//			var rect = iv.svg.rect(this.width,this.height).move(0,0);
			iv.svg.image(iv.documentPath);
			console.log("Image Size:" + this.width+' '+ this.height);
			iv.imgWidth=this.width;
			iv.imgHeight=this.height;
		};
		img.src = iv.documentPath;
	}
}

function convertPdfToImage() {
    var url = iv.documentPath;
    $('body').append('<canvas id="the-canvas" ></canvas>');
    // The workerSrc property shall be specified.
    PDFJS.workerSrc =getResource('resources/js/lib/pdf.worker.js');
    var pdfDoc = null,
        pageNum = iv.currentDocument.pageCount,
        pageRendering = false,
        pageNumPending = null,
        scale = 0.8,
        canvas = document.getElementById('the-canvas'),
        ctx = canvas.getContext('2d');

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
        	iv.documentPath = canvas.toDataURL("image/png");
              canvas.style.display="none";
              displayImage();

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
     * Asynchronously downloads PDF.
     */
    PDFJS.getDocument(url).then(function(pdfDoc_) {
      pdfDoc = pdfDoc_;
      //document.getElementById('page_count').textContent = pdfDoc.numPages;

      // Initial/first page rendering
      renderPage(pageNum);
    });

}

function convertTiffToImage() {
	$(function () {
		  Tiff.initialize({TOTAL_MEMORY: 16777216 * 10});
		  var xhr = new XMLHttpRequest();
		  xhr.open('GET', iv.documentPath);
		  xhr.responseType = 'arraybuffer';
		  xhr.onload = function (e) {
		    var buffer = xhr.response;
		    var tiff = new Tiff({buffer: buffer});
		      tiff.setDirectory(iv.currentDocument.pageCount-1);
		      var canvas = tiff.toCanvas();
		      iv.documentPath = canvas.toDataURL("image/png");
		      return displayImage();
		  };
		  xhr.send();
		});
}

function getResource(url) {
	return $("[name='contextPath']").data("context") + "/" + url;
}