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

<script type="text/javascript" src="${rc.getContextPath()}/resources/js/lib/jquery.min.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/bootstrap.min.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/decimalAdjust.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/tiff.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/resources/js/lib/pdf.js"></script>

<!-- SVG.js Plugins -->
<script src="${rc.getContextPath()}/resources/js/lib/svg/svg.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/svg/svg.draw.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/svg/svg.select.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/svg/svg.resize.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/svg/rectable.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/svg/lineable.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/svg/ellipse.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/svg/circle.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/svg/svg.panzoom.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/svg/svg.draggable.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<!-- Earth Development -->
<script src="${rc.getContextPath()}/resources/js/imageViewer/model.js"></script>
<script src="${rc.getContextPath()}/resources/js/imageViewer/service.js"></script>
<script src="${rc.getContextPath()}/resources/js/imageViewer/event.js"></script>
<script src="${rc.getContextPath()}/resources/js/imageViewer/main.js"></script>

</head>

<body>
    <div id ="mainContent">
        <div id="toolBar"></div>
        <div id="imageDrawing" style="width:500px, height:500px;margin-top:50px;background-color:blue"></div>
    </div>
    
    <script>
        initImageViewer();
    </script>
<input type="hidden" data-context="${rc.getContextPath()}" name="contextPath" >
</body>

</html>
