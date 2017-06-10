iv = new ImageViewer();

function getResource(url) {
    return window.baseUrl + "/" + url;
}

function initImageViewer() {
    // Check Browser support SVG or not.
    if (SVG.supported) {
        iv.svg = new SVG('imageDrawing');
        iv.svg.on("mousedown", onMouseDown);
        iv.svg.on("mouseup", onMouseUp);
        //		iv.svg.size(800,800);
        iv.selectedGroup = iv.svg.group();
        iv.sMultipleSvg = iv.svg.rect().fill('none').stroke({
            width: 2
        });
        iv.settingAnno = iv.svg.rect().fill('none').stroke({
            width: 1
        });
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
            $('input[name=color]').change(function() {
                $('#example').attr('stroke', $('input[name=color]:checked').val());
                $("#color").val($("#color option:selected").text());
            });

            $('input[name=fill]').change(function() {
                $('#example').attr('fill', $('input[name=fill]:checked').val());
            });

            $('#width').change(function() {
                $('#example').attr('stroke-width', $('#width').val());
            });
            $('#controlZoom').onkeyup = controlZoom.onchange = function() {
                var val = parseInt(this.value);
                if (val > 100 || val < 0)
                    return false;
                if (val > 50) {
                    currentScale = 0.06 * val - 2;
                    zoomPan(0);
                } else {
                    currentScale = val / 50;
                    zoomPan(0);
                }
            }
        }
    });
    workspaceId = "003";
    workitemId = "1"
    folderItemNo = "2";
    documentNo = "3";
    // Load list of documents and display them on the image viewer screen.
    loadDocument(getResource("WS/displayImage?workspaceId=" + workspaceId + "&workitemId=" + workitemId + "&folderItemNo=" + folderItemNo + "&documentNo=" + documentNo), function(data, res) {
        if (res == result.SUCCESS) {
            iv.documents = data.result;

            iv.currentDocument = iv.documents[1];
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
}

function displayImage() {
    if (iv.documentPath != null) {
        var img = new Image();
        img.onload = function() {
            e = iv.svg.image(iv.documentPath);
            console.log("Image Size:" + this.width + ' ' + this.height);
            iv.imgWidth = this.width;
            iv.imgHeight = this.height;
            load();
        }
        ;
        img.src = iv.documentPath;
    }
}

//render new line of popup layer to html append -> can refactor
function addNewLayer(countLayer, layerName, layerOwner, layerDisplay, layerModified, layerCreated, layerActive) {
    var layerBody = "";
    layerBody += '<div class="row">' + '<div class="col-md-1" id="layer' + countLayer + '">' + layerName + '</div>' + '<div class="col-md-1">' + layerOwner + '</div>' + '<div class="col-md-1">';
    if (layerDisplay) {
        layerBody += '<input type="checkbox" name="display" value="' + layerName + '" checked>';
    } else {
        layerBody += '<input type="checkbox" name="display" value="' + layerName + '">';
    }
    layerItems = $("." + layerName).length;
    layerBody += '</div>' + '<div class="col-md-1">' + layerItems + '</div>' + '<div class="col-md-2">' + layerModified + '</div>' + '<div class="col-md-2">' + layerCreated + '</div>' + '<div class="col-md-1">';
    if (layerActive == layerName) {
        layerBody += '<input type="radio" name="active" value="' + layerName + '" checked="checked"></div></div>';
        $('#layerActive').val(layerName);
    } else {
        layerBody += '<input type="radio" name="active" value="' + layerName + '"></div></div>';
    }
    $('#layerBody').append(layerBody);
}

//load layer to web service
function load() {
    //var cDOcId: layer Length
    iv.cDocId = iv.currentDocument.layers.length;

    for (var k = 0; k < iv.cDocId; k++) {
        //add layer to Document object
        var layer = iv.documents[iv.cDocId - 1].layers[k];
        var tmpLayer = new Layer(layer.layerNo,layer.ownerId,true,layer.lastUpdateTime,layer.lastUpdateTime);
        Document.layers[k] = tmpLayer;

        //if user login not layer owner then future create new layer
        if (tmpLayer.layerOwner == userInfo) {
            iv.existUser = true;
            iv.layerActive = tmpLayer.layerName;
        }

        addNewLayer(k + 1, tmpLayer.layerName, tmpLayer.layerOwner, tmpLayer.layerDisplay, tmpLayer.layerModified, tmpLayer.layerCreated, iv.layerActive);

        //process json data: field annotation of layer in DB
        iv.annotest = layer.annotations.slice(5).slice(0, -6);

        //code add annotation to screen, bug -> comment
        //document.getElementById('SvgjsSvg1001').innerHTML = document.getElementById('SvgjsSvg1001').innerHTML + iv.annotest;

        //read xml string, add id to allAnnos object -> duplicate with id auto generation
        var j = 0;
        do {
            xmlDoc = $.parseXML(layer.annotations),
            $xml = $(xmlDoc),
            $title = $xml.find("svg").children()[j].id;
            if (allAnnos.indexOf($title) == -1) {
                allAnnos.push($title);
            }
            j++;
        } while (typeof $xml.find("svg").children()[j] != "undefined")
    }

    //if user login is not layer, create layer
    if (!iv.existUser) {
        var j = 0;
        do {
            //default layer name is DEF + id auto increament, if layer name exist then auto increament to create
            var tmpLayerName = "DEF" + j;
            var existName = false;
            for (var k = 0; k < iv.cDocId; k++) {
                if (Document.layers[k].layerName == tmpLayerName) {
                    k = iv.cDocId;
                    existName = true;
                }
            }
            j++;

            //if layer name is not exist then create, add j = -1 to end loop
            if (!existName) {
                addNewLayer(iv.cDocId + 1, tmpLayerName, userInfo, true, 2017, 2017, tmpLayerName);
                var newLayer = new Layer(tmpLayerName,userInfo,true,2017,2017);
                Document.layers[iv.cDocId] = newLayer;
                iv.cDocId += 1;
                iv.layerActive = tmpLayerName;
                j = -1;
            }
        } while (j != -1);
    }

    //change cursor when draw comment
    $('input[name=toolOption]').change(function() {
        if ($("#highlight").prop("checked")) {
            $('#SvgjsSvg1001').css('cursor', 'url(' + getResource(cursorImage) + '), auto');
        } else {
            $('#SvgjsSvg1001').css('cursor', 'auto');
        }
    });
}

function convertPdfToImage() {
    var url = iv.documentPath;
    $('body').append('<canvas id="the-canvas" ></canvas>');
    // The workerSrc property shall be specified.
    PDFJS.workerSrc = getResource('resources/js/lib/pdf.worker.js');
    var pdfDoc = null
      , pageNum = iv.currentDocument.pageCount
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
                canvas.style.display = "none";
                displayImage();

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
    PDFJS.getDocument(url).then(function(pdfDoc_) {
        pdfDoc = pdfDoc_;
        renderPage(pageNum);
    });

}

function convertTiffToImage() {
    $(function() {
        Tiff.initialize({
            TOTAL_MEMORY: 16777216 * 10
        });
        var xhr = new XMLHttpRequest();
        xhr.open('GET', iv.documentPath);
        xhr.responseType = 'arraybuffer';
        xhr.onload = function(e) {
            var buffer = xhr.response;
            var tiff = new Tiff({
                buffer: buffer
            });
            tiff.setDirectory(iv.currentDocument.pageCount - 1);
            var canvas = tiff.toCanvas();
            iv.documentPath = canvas.toDataURL("image/png");
            return displayImage();
        }
        ;
        xhr.send();
    });
}
