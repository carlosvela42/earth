<!DOCTYPE html>
<html>
<head>

<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title></title>
<script type="text/javascript"
    src="${rc.getContextPath()}/resources/js/lib/pdf.js"></script>
<script type="text/javascript"
	src="${rc.getContextPath()}/resources/js/lib/d3.v3.js"></script>
<script type="text/javascript"
	src="${rc.getContextPath()}/resources/js/lib/jquery.min.js"></script>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="${rc.getContextPath()}/resources/css/bootstrap.min.css">
<link rel="stylesheet"
	href="${rc.getContextPath()}/resources/css/imageviewer.css">

<!-- Optional theme -->
<link rel="stylesheet"
	href="${rc.getContextPath()}/resources/css/bootstrap-theme.min.css">
<link rel="stylesheet"
	href="${rc.getContextPath()}/resources/css/jquery.contextMenu.css">
<!-- Latest compiled and minified JavaScript -->
<script src="${rc.getContextPath()}/resources/js/lib/bootstrap.min.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/scrollbarWidth.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/decimalAdjust.js"></script>
<script
	src="${rc.getContextPath()}/resources/js/lib/jquery.contextMenu.js"></script>
<script src="${rc.getContextPath()}/resources/js/lib/tiff.min.js"></script>
<script src="${rc.getContextPath()}/resources/js/imageViewer/common.js"></script>
<script src="${rc.getContextPath()}/resources/js/imageViewer/move.js"></script>
</head>
<body style="padding-top: 70px">
    <div style="float: right;">
        <#if Session.userInfo??>
            <#assign userInfo =Session.userInfo>
            <label id="userInfo">${userInfo.userId}/${userInfo.userName}</label>
            <a href="${rc.getContextPath()}/logout" class="button">ログアウト</a>
        </#if>
    </div>
<div id="imageViewerWrapper"></div>
<input type="hidden" data-context="${rc.getContextPath()}" name="contextPath" >
<canvas id="the-canvas"></canvas>
	<script
		src="${rc.getContextPath()}/resources/js/imageViewer/content.js"></script>
		<script
        src="${rc.getContextPath()}/resources/js/imageViewer/layerButton.js"></script>
	<script
		src="${rc.getContextPath()}/resources/js/imageViewer/selectButton.js"></script>
	<script
		src="${rc.getContextPath()}/resources/js/imageViewer/zoomButton.js"></script>
	<script
		src="${rc.getContextPath()}/resources/js/imageViewer/drawButton.js"></script>
	<script
		src="${rc.getContextPath()}/resources/js/imageViewer/propertiesButton.js"></script>
	<script
		src="${rc.getContextPath()}/resources/js/imageViewer/grayscaleButton.js"></script>	
	<script src="${rc.getContextPath()}/resources/js/imageViewer/load.js"></script>
	<script
		src="${rc.getContextPath()}/resources/js/imageViewer/printButton.js"></script>
	<script
		src="${rc.getContextPath()}/resources/js/imageViewer/cutButton.js"></script>
	<script
		src="${rc.getContextPath()}/resources/js/imageViewer/copyButton.js"></script>
	<script
		src="${rc.getContextPath()}/resources/js/imageViewer/pasteButton.js"></script>
	<script
		src="${rc.getContextPath()}/resources/js/imageViewer/rotateButton.js"></script>
	<script
		src="${rc.getContextPath()}/resources/js/imageViewer/switchImageButton.js"></script>
	<script
		src="${rc.getContextPath()}/resources/js/imageViewer/rightClick.js"></script>
	   <script
        src="${rc.getContextPath()}/resources/js/imageViewer/custom.js"></script>	
</body>
</html>
