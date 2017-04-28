function addNewLayer(countLayer, layerName, layerOwner, layerDisplay,
		 layerModified, layerCreated, layerActive) {
	var layerBody = "";
	layerBody += '<div class="row">' + '<div class="col-md-1" id="layer'
			+ countLayer + '">' + layerName + '</div>'
			+ '<div class="col-md-1">' + layerOwner + '</div>'
			+ '<div class="col-md-1">';
	if (layerDisplay) {
		layerBody += '<input type="checkbox" name="display" value="'
				+ layerName + '" checked>';
	} else {
		layerBody += '<input type="checkbox" name="display" value="'
				+ layerName + '">';
	}
	layerItems = $("."+layerName).length;
	layerBody += '</div>' + '<div class="col-md-1">' + layerItems + '</div>'
			+ '<div class="col-md-2">' + layerModified + '</div>'
			+ '<div class="col-md-2">' + layerCreated + '</div>'
			+ '<div class="col-md-1">';
	if (layerActive == layerName) {
		layerBody += '<input type="radio" name="active" value="' + layerName
				+ '" checked="checked"></div></div>';
		$('#layerActive').val(layerName);
	} else {
		layerBody += '<input type="radio" name="active" value="' + layerName
				+ '"></div></div>';
	}
	$('#layerBody').append(layerBody);
}

$('input[type=radio][name=active]').change(function() {
	$('#layerActive').val(this.value);
});

$("#addLayer").click(
		function() {
			var k = 0;
			if ($('#layerName').val() != "") {
				var countRow = $("#layerBody .row").length;
				for (var j = 1; j < countRow; j++) {
					if ($('#layerName').val() == $('#layer' + j).html()) {
						k++;
					}
				}
				if (k == 0) {
					addNewLayer(countRow, $('#layerName').val(),
							jsonLayer.User, true, 0, 2017, 2017, false);
				} else {
					alert("Layer exist");
				}
			}
		});

$("#removeLayer").click(
		function() {
			if ($('#layerName').val() != "") {
				var countRow = $("#layerBody .row").length;
				for (var j = 1; j < countRow; j++) {
					if ($('#layerName').val() == $('#layer' + j).html()) {
						if ($('input[name=active]:checked').val() == $(
								'#layerName').val()) {
							for (var k = 1; k < countRow; k++) {
								// goi ham active
							}
						}
						$('#layer' + j).text("");
						return $('#layer' + j).parent().hide();
					}
				}
				alert("Layer isn't exist");

			}
		});

$("#renameLayer").click(
		function() {
			if ($('#layerName').val() != "") {
				var countRow = $("#layerBody .row").length;
				for (var j = 1; j < countRow; j++) {
					if ($('#layerName').val() == $('#layer' + j).html()) {
						return alert("Layer name exist");
					}
				}
				for (var j = 1; j < countRow; j++) {
					if ($('#layerActive').val() == $('#layer' + j).html()) {
						$(
								"input[type=radio][name=active][value="
										+ $('#layer' + j).text() + "]").attr(
								'value', $('#layerName').val());
						$('#layer' + j).text($('#layerName').val());
						$('#layerActive').val($('#layerName').val());
						return;
					}
				}
			}
		});

$("#activeLayer").click(
		function() {
			if ($('#layerName').val() != "") {
				var countRow = $("#layerBody .row").length;
				for (var j = 1; j < countRow; j++) {
					if ($('#layerName').val() == $('#layer' + j).html()) {
						$('#layerActive').val($('#layerName').val());
						$(
								"input[type=radio][name=active][value="
										+ $('#layerName').val() + "]").prop(
								"checked", true).trigger("click");
						return;
					}
				}
				alert("Layer isn't exist");
			}
		});

$("#displayLayer").click(
		function() {
			if ($('#layerName').val() != "") {
				var countRow = $("#layerBody .row").length;
				for (var j = 1; j < countRow; j++) {
					if ($('#layerName').val() == $('#layer' + j).html()) {
						return $(
								"input[type=checkbox][name=display][value="
										+ $('#layerName').val() + "]").trigger(
								"click");
					}
				}
				alert("Layer isn't exist");
			}
		});

$("#btnLayer").click(
		function() {
			for (var j = 1; j <= layerCount; j++) {
				$("#layerBody .row:eq(1)").remove();
			}
			for (var k = 0; k < layerCount; k++) {
				addNewLayer(k + 1, layerName[k], layerOwner[k],
						layerDisplay[k], layerModified[k],
						layerCreated[k], layerActive);
			}
			$('#layerName').val("");
		});

$("#okLayer").click(
		function() {
			layerCount = $("#layerBody .row").length - 1;
			for (var j = 1; j <= layerCount; j++) {
				layerName[j - 1] = $("div.row:eq(" + j + ") div:nth-child(1)")
						.text();
				layerOwner[j - 1] = $("div.row:eq(" + j + ") div:nth-child(2)")
						.text();
				layerDisplay[j - 1] = $(
						"div.row:eq(" + j + ") div:nth-child(3) input").is(
						':checked');
				layerItems[j - 1] = $("div.row:eq(" + j + ") div:nth-child(4)")
						.text();
				layerModified[j - 1] = $(
						"div.row:eq(" + j + ") div:nth-child(5)").text();
				layerCreated[j - 1] = $(
						"div.row:eq(" + j + ") div:nth-child(6)").text();
				if ($("div.row:eq(" + j + ") div:nth-child(7) input").is(
						':checked')) {
					layerActive = layerName[j - 1];
				}
				if ($("div.row:eq(" + j + ") div:nth-child(3) input").is(
						':checked')) {
					$('.' + layerName[j - 1]).each(function() {
						if ($(this).parent().css("visibility") == "hidden") {

						} else {
							$(this).css("visibility", "visible");
						}
					});
				} else {
					$('.' + layerName[j - 1]).css("visibility", "hidden");
				}
			}
			selectBefore();
		});