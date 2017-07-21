function init(parentId, params, docType, page) {
	if (SVG.supported) {
		var url = window.baseUrl + '/WS/getThumbnail?' + params;
		if (page == null || page == '') {
			return;
		}

		$.ajax({
			url : url,
			async : true,
			dataType : 'jsonp',
			type : "GET",
			cache : true,
			success : function(data, textStatus, xhr) {
				var thumb = new SVG(parentId);
				
				displayImage(thumb, params, docType, page, function() {
					if (data.thumbernail !== null && data.thumbernail !== 'null') {
						$('#'+ thumb.node.id).append(data.thumbernail);
					}

					$('#' + thumb.node.id).dblclick(function() {
						var imageViewerUrl = window.baseUrl + '/imageviewer/svgImageViewer?' + params;
						window.open(imageViewerUrl, '_blank');
					});
				});
			},
			error : function(xhr, textStatus, errorThrown) {
				console.log("load thumbernail of document("
						+ params + ") error!");
			}
		});
	} else {
		alert('SVG not supported');
		return;
	}
}

function displayImage(thumb, params, docType, page, callback) {
	var imageUrl = window.baseUrl + '/WS/getDocumentBinary?' + params;
	switch (docType) {
	case documentType.IMAGE:
		thumb.image(imageUrl);
		break;
	case documentType.PDF:
		convertPdfToImage(thumb, imageUrl, parseInt(page), function(data) {
			thumb.image(data);
		});
		break;
	case documentType.TIFF:
		convertTiffToImage(thumb, imageUrl, page, function(data) {
			thumb.image(data);
		});
		break;
	}
	return callback();
}

function convertTiffToImage(thumb, imgUrl, page, callback) {
	$(function() {
		Tiff.initialize({
			TOTAL_MEMORY : 16777216 * 10
		});
		var xhr = new XMLHttpRequest();
		xhr.open('GET', imgUrl);
		xhr.responseType = 'arraybuffer';
		xhr.onload = function(e) {
			var buffer = xhr.response;
			var tiff = new Tiff({
				buffer : buffer
			});
			tiff.setDirectory(page - 1);
			var canvas = tiff.toCanvas();
			var result = canvas.toDataURL("image/png");
			return callback(result);
		};
		xhr.send();
	});
}

function convertPdfToImage(thumb, imgUrl, page, callback) {
    $('#' + thumb.node.id).append('<canvas id="the-canvas" ></canvas>');
    // The workerSrc property shall be specified.
    PDFJS.workerSrc = getResource('resources/js/lib/pdf.worker.js');
    var pdfDoc = null
        , pageNum = page
        , pageRendering = false
        , pageNumPending = null
        , scale = 0.8
        , canvas = document.getElementById('the-canvas')
        , ctx = canvas.getContext('2d');

    /**
     * Get page info from document, resize canvas accordingly, and render page.
     *
     * @param num
     *            Page number.
     */
    function renderPage(num) {
        pageRendering = true;
        // Using promise to fetch the page
        pdfDoc.getPage(num).then(function (page) {
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
            renderTask.promise.then(function () {
                result = canvas.toDataURL("image/png");
                canvas.style.display = "none";
                return callback (result);

                pageRendering = false;
                if (pageNumPending !== null) {
                    // New page rendering is pending
                    renderPage(pageNumPending);
                    pageNumPending = null;
                }
            });
        });
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
    PDFJS.getDocument(imgUrl).then(function (pdfDoc_) {
        pdfDoc = pdfDoc_;
        renderPage(pageNum);
    });
}