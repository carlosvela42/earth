IV.prototype.addZoomButton = function() {
	var self = this;
	IV.prototype.zoom = function(zoom) {
	self.rotate += 90;
	self.startAnnotation();
	self.rotate -= 90;
	self.scale = zoom;
	self.redraw();
	$('#lbZoom').text(Math.floor(self.scale * 100) + "%");

	}

	$("#zoomout").click(function() {
		self.zoom(Math.ceil10(self.scale * 10, -2) / 10 - 0.1);
	});

	$("#zoomin").click(function() {
		self.zoom(Math.floor10(self.scale * 10, -2) / 10 + 0.1);
	});
	
	$("#zoomFullPage").click(function() {		
		if(($('#container').width()-getScrollBarWidth())/-self.x2[0]>($('#container').height()-getScrollBarWidth())/-self.y2[0]){
			self.zoom(($('#container').height()-getScrollBarWidth())/-self.y2[0]);
		}
		else
		{
			self.zoom(($('#container').width()-getScrollBarWidth())/-self.x2[0]);
		}
	});
	
	$("#zoomFullWidth").click(function() {
		self.zoom(($('#container').width()-getScrollBarWidth())/-self.x2[0]);
	});

	$("#zoom200").click(function() {
		self.zoom(2);
	});
	
	$("#zoom100").click(function() {
		self.zoom(1);
	});
	
	$("#zoom75").click(function() {
		self.zoom(0.75);
	});
	
	$("#zoom50").click(function() {
		self.zoom(0.5);
	});

	$('#controlZoom').onkeyup = controlZoom.onchange = function() {
		var val = parseInt(this.value);
		if (val > 100 || val < 0)
			return false;
		if (val > 50) {
			self.zoom(0.06 * val - 2);
		} else {
			self.zoom(val / 50);
		}
	}
}