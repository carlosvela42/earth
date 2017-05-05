	function zoom(zoom) {
		rotate += 90;
		startAnnotation();
		rotate -= 90;
		scale = zoom;
		redraw();
		$('#lbZoom').text(Math.floor(scale * 100) + "%");
	}

	$("#zoomout").click(function() {
		zoom(Math.ceil10(scale * 10, -2) / 10 - 0.1);
	});

	$("#zoomin").click(function() {
		zoom(Math.floor10(scale * 10, -2) / 10 + 0.1);
	});
	
	$("#zoomFullPage").click(function() {
		if(($('#container').width()-getScrollBarWidth())/-x2[0]>($('#container').height()-getScrollBarWidth())/-y2[0]){
			zoom(($('#container').height()-getScrollBarWidth())/-y2[0]);
		}
		else
		{
			zoom(($('#container').width()-getScrollBarWidth())/-x2[0]);
		}
	});
	
	$("#zoomFullWidth").click(function() {
		zoom(($('#container').width()-getScrollBarWidth())/-x2[0]);
	});

	$("#zoom200").click(function() {
		zoom(2);
	});
	
	$("#zoom100").click(function() {
		zoom(1);
	});
	
	$("#zoom75").click(function() {
		zoom(0.75);
	});
	
	$("#zoom50").click(function() {
		zoom(0.5);
	});

	$('#controlZoom').onkeyup = controlZoom.onchange = function() {
		var val = parseInt(this.value);
		if (val > 100 || val < 0)
			return false;
		if (val > 50) {
			zoom(0.06 * val - 2);
		} else {
			zoom(val / 50);
		}
	}