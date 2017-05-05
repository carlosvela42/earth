window.onload = function() {
	vis.append("image").attr("x", 0).attr("y", 0).attr("xlink:href",
			jsonLayer.Image).attr("id", "0").attr("transform",
			"rotate(0) scale(1)");
	for (var k = 0; k < layerCount; k++) {
		layer = jsonLayer.Layer[k];
		layerName[k] = layer.Name;
		layerOwner[k] = layer.Owner;
		layerDisplay[k] = true;
		layerModified[k] = layer.Modified;
		layerCreated[k] = layer.Created;		
		if(layerOwner[k]==jsonLayer.User)
		{
			existUser = true;
			layerActive = layerName[k];
		}	
		addNewLayer(k + 1, layerName[k], layerOwner[k], layerDisplay[k], layerModified[k], layerCreated[k], layerActive);
		var annotationsLength = layer.Annotations.length;
		for (var j = 0; j < annotationsLength; j++) {
			var counter = layer.Annotations[j];
			x1[counter.id] = parseInt(counter.x1);
			y1[counter.id] = parseInt(counter.y1);
			x2[counter.id] = parseInt(counter.x2);
			y2[counter.id] = parseInt(counter.y2);
			if (counter.type == "line") {
				vis.append("line").attr("x1", counter.x1)
						.attr("y1", counter.y1).attr("x2", counter.x2).attr(
								"y2", counter.y2).attr("id", counter.id).attr(
								"stroke", counter.stroke).attr("stroke-width",
								counter.strokew).attr("class",
								counter.class + " " + layer.Name).attr(
								"transform", "rotate(0) scale(1)").on("click",
								selectClick);
				var tempX,tempY;
				if(counter.x2>=counter.x1){ tempX = counter.x1;}else{tempX = counter.x2;}
				if(counter.y2>=counter.y1){ tempY = counter.y1;}else{tempY = counter.y2;}
				vis.append("rect").attr("x", tempX).attr("y", tempY).attr("width", Math.abs(counter.x2-counter.x1))
				.attr("height", Math.abs(counter.y2-counter.y1)).attr("id", "r"+counter.id).attr("fill", "none").attr("class", counter.class + " " + layer.Name).on(
						"click", selectClick).attr("pointer-events", "all").attr("transform",
								"rotate(0) scale(1)");
			}
			if (counter.type == "rect") {
				vis.append("rect").attr("x", counter.x1).attr("y", counter.y1)
						.attr("width", counter.x2).attr("height", counter.y2)
						.attr("id", counter.id).attr("stroke", counter.stroke)
						.attr("stroke-width", counter.strokew).attr("fill",
								counter.fill).attr("class",
								counter.class + " " + layer.Name).attr(
								"transform", "rotate(0) scale(1)").on("click",
								selectClick).attr("pointer-events", "all");
			}
			if (counter.id > i) {
				i = counter.id;
			}
		}
	}
	
	if(!existUser)
	{	
		var j =0;
		do
		{			
			var tmpLayerName = "DEF"+j;
			var existName = false; 
			for (var k = 0; k < layerCount; k++) {
				if(layerName[k]==tmpLayerName)
				{
					k = layerCount;
					existName = true;
				}
			}
			j++;
			if(!existName)
			{
				addNewLayer(layerCount+1, tmpLayerName,
						jsonLayer.User, true, 2017, 2017, tmpLayerName);				
				layerName[layerCount] = tmpLayerName;
				layerOwner[layerCount] = jsonLayer.User;
				layerDisplay[layerCount] = true;				
				layerModified[layerCount] = 2017;
				layerCreated[layerCount] = 2017;
				layerCount += 1;
				layerActive = tmpLayerName;
				j = -1;
			}
		}
		 while (j!=-1);
	}

	var img = new Image();
	img.onload = function() {
		x2[0] = -parseInt(this.width);
		y2[0] = -parseInt(this.height);
		if(($('#container').width()-getScrollBarWidth())/-x2[0]>($('#container').height()-getScrollBarWidth())/-y2[0]){
			zoom(($('#container').height()-getScrollBarWidth())/-y2[0]);
		}
		else
		{
			zoom(($('#container').width()-getScrollBarWidth())/-x2[0]);
		}
	}
	img.src = jsonLayer.Image;
	
	$('input[name=toolOption]').change(function() {
		if ($("#highlight").prop("checked")) 
		{
			$('#svg').css('cursor','url("resources/images/imageViewer/Pen-50.png"), auto');
		}
		else
		{
			$('#svg').css('cursor','auto');
		}
	});
}