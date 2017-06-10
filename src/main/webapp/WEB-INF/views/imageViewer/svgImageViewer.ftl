<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=9;IE=10;IE=11;IE=Edge,chrome=1"/>

<title>SVG Image Viewer Demo</title>

<!-- CSS -->
<link rel="stylesheet"
    href="${rc.getContextPath()}/resources/css/bootstrap.min.css">
<link rel="stylesheet"
    href="${rc.getContextPath()}/resources/css/imageviewer.css">
<link rel="stylesheet"
    href="${rc.getContextPath()}/resources/js/lib/svg/svg.select.css">
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">

<script type="text/javascript" src="${rc.getContextPath()}/resources/js/lib/jquery.min.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/bootstrap.min.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/scrollbarWidth.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/decimalAdjust.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/tiff.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/resources/js/lib/pdf.js"></script>
 <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<!-- SVG.js Plugins -->
<script src="${rc.getContextPath()}/resources/js/lib/svg/svg.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/svg/svg.draw.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/svg/svg.select.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/svg/svg.resize.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/svg/rectable.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/svg/lineable.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/svg/ellipse.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/svg/circle.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/svg/text.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/svg/svg.panzoom.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/svg/svg.draggable.js"></script>

<!-- Earth Development -->
<script> window.baseUrl = "${rc.getContextPath()}" </script>
<script src="${rc.getContextPath()}/resources/js/imageViewer/model.js"></script>
<script src="${rc.getContextPath()}/resources/js/imageViewer/service.js"></script>
<script src="${rc.getContextPath()}/resources/js/imageViewer/event.js"></script>
<script src="${rc.getContextPath()}/resources/js/imageViewer/main.js"></script>

</head>

<body>
    <div id ="mainContent">
        <div id="toolBar"></div>
        <div id="imageDrawing" style="width:800px; height:800px;margin-top:50px;overflow: scroll;"></div>
    </div>
    
    <script>
        initImageViewer();
    </script>
</body>

</html>
