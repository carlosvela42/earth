﻿<html>
<head>
<meta http-equiv="content-type" content="text/html;">
<title></title>
<script type="text/javascript"
	src="${rc.getContextPath()}/resources/js/d3.v3.js"></script>
<script type="text/javascript"
	src="${rc.getContextPath()}/resources/js/jquery.min.js"></script>
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
<script src="${rc.getContextPath()}/resources/js/bootstrap.min.js"></script>
<script src="${rc.getContextPath()}/resources/js/decimalAdjust.js"></script>
<script src="${rc.getContextPath()}/resources/js/jquery.contextMenu.js"></script>
<script src="${rc.getContextPath()}/resources/js/jsonData.js"></script>
<script src="${rc.getContextPath()}/resources/js/imageviewer.js"></script>
<script src="${rc.getContextPath()}/resources/js/move.js"></script>

</head>
<body>
	<div class="btn-group" role="group" aria-label="...">
		<button type="button" class="btn btn-default" title="select"
			id="select">
			<img
				src="${rc.getContextPath()}/resources/images/imageViewer/select.png"
				width="20" />
		</button>
		<div class="btn-group" role="group">
			<button type="button" class="btn btn-default dropdown-toggle"
				data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"
				title="zoom">
				<img
					src="${rc.getContextPath()}/resources/images/imageViewer/zoom.png"
					width="20" /> <span class="caret"></span>
			</button>
			<ul class="dropdown-menu">
				<li><a href="#">Full page</a></li>
				<li><a href="#">Full width</a></li>
				<li><a href="#" id="zoom200">200%</a></li>
				<li><a href="#">100%</a></li>
				<li><a href="#">75%</a></li>
				<li><a href="#">50%</a></li>
				<li><input type="range" id="controlZoom" value="50" min="5"
					max="100" maxlength="3"> <label id="lbZoom">100%</label></li>
			</ul>
		</div>
		<button type="button" class="btn btn-default" title="zoomin"
			id="zoomin">
			<img
				src="${rc.getContextPath()}/resources/images/imageViewer/zoomin.png"
				width="20" />
		</button>
		<button type="button" class="btn btn-default" title="zoomout"
			id="zoomout">
			<img
				src="${rc.getContextPath()}/resources/images/imageViewer/zoomout.png"
				width="20" />
		</button>
		<div class="btn-group" role="group">
			<button type="button" class="btn btn-default dropdown-toggle"
				data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"
				title="draw">
				<img
					src="${rc.getContextPath()}/resources/images/imageViewer/draw.png"
					width="20" /> <span class="caret"></span>
			</button>
			<ul class="dropdown-menu">
				<li><input type="radio" id="line" name="toolOption"
					data-tool="line" checked>LINE</li>
				<li><input type="radio" id="rectangle" name="toolOption"
					data-tool="rectangle">RECTANGLE</li>
				<li><input type="radio" id="text" name="toolOption"
					data-tool="text"> TEXT</li>
				<li><input type="radio" id="highlight" name="toolOption"
					data-tool="highlight"> HIGHLIGHT</li>
				<li><input type="radio" id="comment" name="toolOption"
					data-tool="comment"> COMMENT</li>
			</ul>
		</div>
		<button type="button" class="btn btn-default" title="properties"
			data-toggle="modal" id="properties">
			<img
				src="${rc.getContextPath()}/resources/images/imageViewer/properties.jpg"
				width="20" />
		</button>

		<!-- Modal -->
		<div class="modal fade" id="myModalText" role="dialog">
			<div class="modal-dialog">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title">Text markup properties</h4>
					</div>
					<div class="modal-body">
						<div class="form-group">
							<textarea class="form-control" rows="5" id="commentText"></textarea>
						</div>
						<div class="form-group">
							Pen Color: <select id="color">
								<option value="rgb(0, 0, 0)">Black</option>
								<option value="rgb(255, 0, 0)">Red</option>
							</select>
						</div>
						<div class="form-group">
							Fill Color: <select id="fill">
								<option value="rgba(0, 0, 0, 0)">Transparent</option>
								<option value="rgb(0, 0, 0)">Black</option>
							</select>
						</div>
						<div class="form-group">
							Text Border Color: <select id="border">
								<option value="1px solid rgba(0, 0, 0, 0)">Transparent</option>
								<option value="1px solid rgb(0, 0, 0)">Black</option>
							</select>
						</div>
						<div class="form-group">
							Font: <select id="font">
								<option value="serif">Serif</option>
								<option value="Arial">Arial</option>
							</select>
						</div>
						<div class="form-group">
							Font Size: <select id="fontSize">
								<option value="18px">18</option>
								<option value="20px">20</option>
							</select>
						</div>
						<div class="form-group">
							Font Style: <select id="fontStyle">
								<option value="normal">Regular</option>
								<option value="bold">Bold</option>
								<option value="italic">Italic</option>
								<option value="underline">Underline</option>
							</select>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal"
							id="textProperties">OK</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					</div>
				</div>
			</div>
		</div>

		<!-- Modal -->
		<div class="modal fade" id="myModalComment" role="dialog">
			<div class="modal-dialog">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title">Comment markup properties</h4>
					</div>
					<div class="modal-body">
						<div class="form-group">
							<textarea class="form-control" rows="5" id="comment"></textarea>
						</div>
					</div>
					<div class="modal-footer">
						Label <input id="tbComment" value="">
						<button type="button" class="btn btn-default" data-dismiss="modal"
							id="commentProperties">OK</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					</div>
				</div>
			</div>
		</div>

		<!-- Modal -->
		<div class="modal fade" id="myModalLine" role="dialog">
			<div class="modal-dialog">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title">Markup Properties</h4>
					</div>
					<div class="modal-body">
						<div class="col-md-8">
							Color
							<ul class="nav nav-tabs">
								<li><a data-toggle="tab" href="#home" id="li1">Pen</a></li>
								<li><a data-toggle="tab" href="#menu1" id="li2">Fill</a></li>
								<li><a data-toggle="tab" href="#menu2" id="li3">Highlight</a></li>
							</ul>
							<div class="tab-content">
								<div id="home" class="tab-pane fade in active">
									<input type="radio" name="color" id="black" value="black" /> <label
										for="black"><span class="black"></span></label> <input
										type="radio" name="color" id="red" value="red" /> <label
										for="red"><span class="red"></span></label> <input
										type="radio" name="color" id="green" value="green" /> <label
										for="green"><span class="green"></span></label> <input
										type="radio" name="color" id="yellow" value="yellow" /> <label
										for="yellow"><span class="yellow"></span></label> <input
										type="radio" name="color" id="olive" value="olive" /> <label
										for="olive"><span class="olive"></span></label> <input
										type="radio" name="color" id="orange" value="orange" /> <label
										for="orange"><span class="orange"></span></label> <input
										type="radio" name="color" id="teal" value="teal" /> <label
										for="teal"><span class="teal"></span></label> <input
										type="radio" name="color" id="blue" value="blue" /> <label
										for="blue"><span class="blue"></span></label> <input
										type="radio" name="color" id="violet" value="violet" /> <label
										for="violet"><span class="violet"></span></label> <input
										type="radio" name="color" id="purple" value="purple" /> <label
										for="purple"><span class="purple"></span></label>
								</div>
								<div id="menu1" class="tab-pane fade">
									<h3>Menu 1</h3>
									<input type="radio" name="fill" id="1black" value="black" /> <label
										for="black"><span class="black"></span></label> <input
										type="radio" name="fill" id="1red" value="red" /> <label
										for="red"><span class="red"></span></label> <input
										type="radio" name="fill" id="1green" value="green" /> <label
										for="green"><span class="green"></span></label> <input
										type="radio" name="fill" id="1yellow" value="yellow" /> <label
										for="yellow"><span class="yellow"></span></label> <input
										type="radio" name="fill" id="1olive" value="olive" /> <label
										for="olive"><span class="olive"></span></label> <input
										type="radio" name="fill" id="1orange" value="orange" /> <label
										for="orange"><span class="orange"></span></label> <input
										type="radio" name="fill" id="1teal" value="teal" /> <label
										for="teal"><span class="teal"></span></label> <input
										type="radio" name="fill" id="1blue" value="blue" /> <label
										for="blue"><span class="blue"></span></label> <input
										type="radio" name="fill" id="1violet" value="violet" /> <label
										for="violet"><span class="violet"></span></label> <input
										type="radio" name="fill" id="1purple" value="purple" /> <label
										for="purple"><span class="purple"></span></label>
								</div>
								<div id="menu2" class="tab-pane fade">
									<h3>Menu 2</h3>
									<input type="radio" name="highlight" id="2black" value="black" />
									<label for="black"><span class="black"></span></label> <input
										type="radio" name="highlight" id="2red" value="red" /> <label
										for="red"><span class="red"></span></label> <input
										type="radio" name="highlight" id="2green" value="green" /> <label
										for="green"><span class="green"></span></label> <input
										type="radio" name="highlight" id="2yellow" value="yellow" />
									<label for="yellow"><span class="yellow"></span></label> <input
										type="radio" name="highlight" id="2olive" value="olive" /> <label
										for="olive"><span class="olive"></span></label> <input
										type="radio" name="highlight" id="2orange" value="orange" />
									<label for="orange"><span class="orange"></span></label> <input
										type="radio" name="highlight" id="2teal" value="teal" /> <label
										for="teal"><span class="teal"></span></label> <input
										type="radio" name="highlight" id="2blue" value="blue" /> <label
										for="blue"><span class="blue"></span></label> <input
										type="radio" name="highlight" id="2violet" value="violet" />
									<label for="violet"><span class="violet"></span></label> <input
										type="radio" name="highlight" id="2purple" value="purple" />
									<label for="purple"><span class="purple"></span></label>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							Line Size <br /> <input type="number" min="1" max="100"
								value="1" id="width">

						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal"
							id="okProperties">OK</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					</div>
				</div>

			</div>
		</div>

		<button type="button" id="btnGrayscale" class="btn btn-default" title="grayscale"
			data-toggle="modal" data-target="#myModal2">
			<img
				src="${rc.getContextPath()}/resources/images/imageViewer/grayscale.jpg"
				width="20" />
		</button>

		<!-- Modal -->
		<div class="modal fade" id="myModal2" role="dialog">
			<div class="modal-dialog">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title">Image adjustment dialog</h4>
					</div>
					<div class="modal-body">
						Contrast<br /> <input type="range" id="controls1" value="100"
							min="0" max="100" maxlength="3"> Brightness<br /> <input
							type="range" id="controls" value="100" min="0" max="100"
							maxlength="3">
					</div>
					<div class="modal-footer">
						<label style="display: block; text-align: left;"><input
							type="checkbox" id="cbox1" value="grayscale"> Grayscale</label>
						<button type="button" class="btn btn-default" data-dismiss="modal"
							id="okGrayscale">OK</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					</div>
				</div>

			</div>
		</div>

		<button type="button" id="btnLayer" class="btn btn-default" title="layer"
			data-toggle="modal" data-target="#myModal3">
			<img
				src="${rc.getContextPath()}/resources/images/imageViewer/layer.png"
				width="20" />
		</button>

		<!-- Modal -->
		<div class="modal fade" id="myModal3" role="dialog">
			<div class="modal-dialog">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title">Layer management</h4>
					</div>
					<div class="modal-body" id="layerBody">
						<div class="form-inline">
							<div class="form-group ">
								<div class="col-lg-12">
									<label for="name">Name</label> <input type="text" id="layerName"
										name="layerName" class="form-control ">
								</div>
							</div>
							<div class="form-group ">
								<div class="col-lg-12">
									<label for="active">Active</label> <input type="text"
										id="layerActive" name="layerActive" class="form-control " disabled>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-1">Name</div>
							<div class="col-md-1">Owner</div>
							<div class="col-md-1">Display</div>
							<div class="col-md-1">Items</div>
							<div class="col-md-2">Modified</div>
							<div class="col-md-2">Created</div>
							<div class="col-md-1">Active</div>


						</div>

					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" id="addLayer">Add</button>
						<button type="button" class="btn btn-default" id="removeLayer">Remove</button>
						<button type="button" class="btn btn-default" id="renameLayer">Rename</button>
						<button type="button" class="btn btn-default" id="activeLayer">Active</button>
						<button type="button" class="btn btn-default" id="displayLayer">Display</button>
						<button type="button" class="btn btn-default" data-dismiss="modal"
							id="okLayer">OK</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="btn-group" role="group">
		<button type="button" class="btn btn-default dropdown-toggle"
			data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"
			title="print">
			<img
				src="${rc.getContextPath()}/resources/images/imageViewer/print.png"
				width="20" /> <span class="caret"></span>
		</button>
		<ul class="dropdown-menu">
			<li><a href="#" id="print">Include Annotations</a></li>
			<li><a href="#" id="print0">Only Image</a></li>
		</ul>
	</div>
	<button type="button" class="btn btn-default" title="cut" id="cut">
		<img src="${rc.getContextPath()}/resources/images/imageViewer/cut.png"
			width="20" />
	</button>
	<button type="button" class="btn btn-default" title="copy" id="copy">
		<img
			src="${rc.getContextPath()}/resources/images/imageViewer/copy.png"
			width="20" />
	</button>
	<button type="button" class="btn btn-default" title="paste" id="paste">
		<img
			src="${rc.getContextPath()}/resources/images/imageViewer/paste.png"
			width="20" />
	</button>
	<button type="button" class="btn btn-default"
		title="rotate counter-clockwise" id="rotateC">
		<img
			src="${rc.getContextPath()}/resources/images/imageViewer/rotatecounter.png"
			width="20" />
	</button>
	<button type="button" class="btn btn-default" title="rotate clockwise"
		id="rotate">
		<img
			src="${rc.getContextPath()}/resources/images/imageViewer/rotate.png"
			width="20" />
	</button>
	<button type="button" class="btn btn-default" title="first">
		<img
			src="${rc.getContextPath()}/resources/images/imageViewer/first.jpg"
			width="20" />
	</button>
	<button type="button" class="btn btn-default" title="previous">
		<img
			src="${rc.getContextPath()}/resources/images/imageViewer/previous.png"
			width="20" />
	</button>
	<button type="button" class="btn btn-default" title="next">
		<img
			src="${rc.getContextPath()}/resources/images/imageViewer/next.png"
			width="20" />
	</button>
	<button type="button" class="btn btn-default" title="last">
		<img
			src="${rc.getContextPath()}/resources/images/imageViewer/last.jpg"
			width="20" />
	</button>
	<div class="btn-group" role="group">
		<button type="button" class="btn btn-default dropdown-toggle"
			data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"
			title="#1">
			#1 <span class="caret"></span>
		</button>
		<ul class="dropdown-menu">
			<li><a href="#">#1</a></li>
			<li><a href="#">#2</a></li>
			<li><a href="#">#3</a></li>
			<li><a href="#">#4</a></li>
			<li><a href="#">#5</a></li>
			<li><a href="#">#6</a></li>
		</ul>
	</div>
	<br />
	<script src="${rc.getContextPath()}/resources/js/properties.js"></script>
</body>
</html>
