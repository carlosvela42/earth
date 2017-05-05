	$("#copy").click(
			function() {
				i++;
				rotate += 90;
				startAnnotation();
				rotate -= 90;
				redraw();
				if($("#r" + selectId).length==0){
				$("#" + selectId).clone().appendTo("svg").attr("id", i).attr(
						"onmousedown", null).attr("onmouseup", null).on(
						"click", selectClick);}else{
							$("#" + selectId).clone().appendTo("svg").attr("id", i).attr(
									"onmousedown", null).attr("onmouseup", null);
				$("#r" + selectId).clone().appendTo("svg").attr("id", "r"+i).attr(
						"onmousedown", null).attr("onmouseup", null).on(
						"click", selectClick);}
				x1[i] = x1[selectId];
				x2[i] = x2[selectId];
				y1[i] = y1[selectId];
				y2[i] = y2[selectId];
				$("#" + selectId).parent().css("visibility", "hidden");
				cut = selectId;
			});