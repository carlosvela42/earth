IV.prototype.copyClick = function() {
				imageViewer.i++;
				imageViewer.rotate += 90;
				imageViewer.startAnnotation();
				imageViewer.rotate -= 90;
				imageViewer.redraw();
				if($("#r" + imageViewer.selectId).length==0){
				$("#" + imageViewer.selectId).clone().appendTo("svg").attr("id", imageViewer.i).attr(
						"onmousedown", null).attr("onmouseup", null).on(
						"click", imageViewer.selectClick);}else{
							$("#" + imageViewer.selectId).clone().appendTo("svg").attr("id", imageViewer.i).attr(
									"onmousedown", null).attr("onmouseup", null);
				$("#r" + imageViewer.selectId).clone().appendTo("svg").attr("id", "r"+imageViewer.i).attr(
						"onmousedown", null).attr("onmouseup", null).on(
						"click", imageViewer.selectClick);}
				imageViewer.x1[imageViewer.i] = imageViewer.x1[imageViewer.selectId];
				imageViewer.x2[imageViewer.i] = imageViewer.x2[imageViewer.selectId];
				imageViewer.y1[imageViewer.i] = imageViewer.y1[imageViewer.selectId];
				imageViewer.y2[imageViewer.i] = imageViewer.y2[imageViewer.selectId];
				$("#" + imageViewer.selectId).parent().css("visibility", "hidden");
				imageViewer.cut = imageViewer.selectId;
			}