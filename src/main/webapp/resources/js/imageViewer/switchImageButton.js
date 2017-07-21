IV.prototype.changeDocument = function () {
    imageViewer.currentDocument = imageViewer.documents[imageViewer.cDocId - 1];
    imageViewer.documentImage = jQuery.parseJSON(imageViewer.currentDocument.viewInformation).Image;
    switch (imageViewer.currentDocument.documentType) {
        case "1":
            var img = new Image();
            img.onload = function () {
                imageViewer.x2[0] = -parseInt(this.width);
                imageViewer.y2[0] = -parseInt(this.height);
                imageViewer.vis.append("image").attr("x", 0).attr("y", 0).attr("xlink:href", imageViewer.documentImage).attr(
                    "id", "0").attr("transform", "rotate(0) scale(1)").attr(
                    "width", -imageViewer.x2[0]).attr("height", -imageViewer.y2[0]);
                imageViewer.load();
                imageViewer.switchImage();
            }
            img.src = imageViewer.documentImage;
            break;
        case "2":
            var url = imageViewer.documentImage;
            $('body').append('<canvas id="the-canvas" ></canvas>');
            // The workerSrc property shall be specified.
            PDFJS.workerSrc = IV.url('resources/js/lib/pdf.worker.js');
            var pdfDoc = null,
                pageNum = imageViewer.currentDocument.pageCount,
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
                    imageViewer.documentImage = canvas.toDataURL("image/png");
                    canvas.style.display = "none";
                    var img = new Image();
                    img.onload = function () {
                        imageViewer.x2[0] = -parseInt(this.width);
                        imageViewer.y2[0] = -parseInt(this.height);
                        imageViewer.vis.append("image").attr("x", 0).attr("y", 0).attr("xlink:href", imageViewer.documentImage).attr("id", "0").attr("transform", "rotate(0) scale(1)").attr("width", -imageViewer.x2[0]).attr("height", -imageViewer.y2[0]);
                        imageViewer.load();
                        imageViewer.switchImage();
                    }
                    img.src = imageViewer.documentImage;
                    //vis.append("foreignObject").attr("id", "0").attr("transform","rotate(0) scale(1)");
                    //$('#0').append(canvas);

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

            $('body').append('<button type="button" class="btn btn-default" title="prev" id="prev"></button>');
            document.getElementById('prev').addEventListener('click', onPrevPage);

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

            $('body').append('<button type="button" class="btn btn-default" title="next2" id="next2"></button>');
            document.getElementById('next2').addEventListener('click', onNextPage);

            /**
             * Asynchronously downloads PDF.
             */
            PDFJS.getDocument(url).then(function (pdfDoc_) {
                pdfDoc = pdfDoc_;
                //document.getElementById('page_count').textContent = pdfDoc.numPages;

                // Initial/first page rendering
                renderPage(pageNum);
            });
            break;
        case "3":
            $(function () {
                Tiff.initialize({
                    TOTAL_MEMORY: 16777216 * 10
                });
                var xhr = new XMLHttpRequest();
                xhr.open('GET', imageViewer.documentImage);
                xhr.responseType = 'arraybuffer';
                xhr.onload = function (e) {
                    var buffer = xhr.response;
                    var tiff = new Tiff({
                        buffer: buffer
                    });
                    tiff.setDirectory(imageViewer.currentDocument.pageCount - 1);
                    var canvas = tiff.toCanvas();
                    imageViewer.documentImage = canvas.toDataURL("image/png");
                    var img = new Image();
                    img.onload = function () {
                        imageViewer.x2[0] = -parseInt(this.width);
                        imageViewer.y2[0] = -parseInt(this.height);
                        imageViewer.vis.append("image").attr("x", 0).attr("y", 0).attr("xlink:href", imageViewer.documentImage).attr("id", "0").attr("transform", "rotate(0) scale(1)").attr("width", -imageViewer.x2[0]).attr("height", -imageViewer.y2[0]);
                        imageViewer.load();
                        imageViewer.switchImage();
                    }
                    img.src = imageViewer.documentImage;

//				vis.append("foreignObject").attr("id", "0").attr(
//						"transform", "rotate(0) scale(1)");
//				$('#0').append(canvas);			

                    return;

                };
                xhr.send();
            });
            break;
    }
    imageViewer.layerCount = imageViewer.currentDocument.layers.length;
    imageViewer.loadBefore();
}
IV.prototype.loadBefore = function () {
    // luu annotation voi anh truoc->xoa trang object, reset page
    $('#chooseImage').empty();
    $("#svg").empty();
}

IV.prototype.save = function () {
    for (var j = 0; j < imageViewer.layerCount; j++) {
        imageViewer.documents[imageViewer.cDocId - 1].layers[j].layerNo = imageViewer.layerName[j];
        imageViewer.documents[imageViewer.cDocId - 1].layers[j].ownerId = imageViewer.layerOwner[j];
        imageViewer.documents[imageViewer.cDocId - 1].layers[j].lastUpdateTime = imageViewer.layerModified[j];
        imageViewer.documents[imageViewer.cDocId - 1].layers[j].lastUpdateTime = imageViewer.layerCreated[j];
    }
}

IV.prototype.swapBefore = function () {
    imageViewer.documents[imageViewer.cDocId - 1].scale = imageViewer.scale;
    imageViewer.documents[imageViewer.cDocId - 1].rotate = imageViewer.rotate;
    for (var j = 1; j <= imageViewer.layerCount; j++) {
        if (typeof imageViewer.documents[imageViewer.cDocId - 1].layers[j - 1] == 'undefined') {
            imageViewer.documents[imageViewer.cDocId - 1].layers[j - 1] = imageViewer.documents[imageViewer.cDocId - 1].layers[j - 2];
        }
        imageViewer.documents[imageViewer.cDocId - 1].layers[j - 1].layerNo = imageViewer.layerName[j - 1];
        imageViewer.documents[imageViewer.cDocId - 1].layers[j - 1].ownerId = imageViewer.layerOwner[j - 1];
        imageViewer.documents[imageViewer.cDocId - 1].layers[j - 1].layerDisplay = imageViewer.layerDisplay[j - 1];
        imageViewer.documents[imageViewer.cDocId - 1].layers[j - 1].lastUpdateTime = imageViewer.layerModified[j - 1];
        imageViewer.documents[imageViewer.cDocId - 1].layers[j - 1].layerCreated = imageViewer.layerCreated[j - 1];
        imageViewer.documents[imageViewer.cDocId - 1].layerActive = imageViewer.layerActive;
        imageViewer.documents[imageViewer.cDocId - 1].layers[j - 1].annotations = '{}';
        $('.' + imageViewer.layerName[j - 1]).each(function (index) {
            imageViewer.documents[imageViewer.cDocId - 1].layers[j - 1].annotations += $(this)[0].outerHTML;
        });
    }
}

IV.prototype.swapImage = function (imageId) {
    if (imageViewer.cDocId != imageId) {
        imageViewer.swapBefore();
        imageViewer.cDocId = imageId;
        imageViewer.changeDocument();
        $('#next').prop('disabled', false);
        $('#last').prop('disabled', false);
        $('#first').prop('disabled', false);
        $('#previous').prop('disabled', false);
        if (imageViewer.cDocId == 1) {
            $('#first').prop('disabled', true);
            $('#previous').prop('disabled', true);
        }
        if (imageViewer.cDocId == imageViewer.documentsLength) {
            $('#next').prop('disabled', true);
            $('#last').prop('disabled', true);
        }
        $('#btnImage').text('#' + imageViewer.cDocId);
    }
}

IV.prototype.lastClick = function () {
    imageViewer.swapBefore();
    imageViewer.cDocId = imageViewer.documentsLength;
    imageViewer.changeDocument();
    $('#first').prop('disabled', false);
    $('#previous').prop('disabled', false);
    $('#next').prop('disabled', true);
    $('#last').prop('disabled', true);
    $('#btnImage').text('#' + imageViewer.cDocId);
}

IV.prototype.nextClick = function () {
    imageViewer.swapBefore();
    imageViewer.cDocId++;
    imageViewer.changeDocument();
    $('#first').prop('disabled', false);
    $('#previous').prop('disabled', false);
    if (imageViewer.cDocId == imageViewer.documentsLength) {
        $('#next').prop('disabled', true);
        $('#last').prop('disabled', true);
    }
    $('#btnImage').text('#' + imageViewer.cDocId);
}

IV.prototype.previousClick = function () {
    imageViewer.swapBefore();
    imageViewer.cDocId--;
    imageViewer.changeDocument();
    $('#next').prop('disabled', false);
    $('#last').prop('disabled', false);
    if (imageViewer.cDocId == 1) {
        $('#first').prop('disabled', true);
        $('#previous').prop('disabled', true);
    }
    $('#btnImage').text('#' + imageViewer.cDocId);
}

IV.prototype.firstClick = function () {
    imageViewer.swapBefore();
    imageViewer.cDocId = 1;
    imageViewer.changeDocument();
    $('#first').prop('disabled', true);
    $('#previous').prop('disabled', true);
    $('#next').prop('disabled', false);
    $('#last').prop('disabled', false);
    $('#btnImage').text('#1');
}
