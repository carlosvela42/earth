function loadDocument(url, callback) {
	$.ajax({
		url: url,
		async: true,
		dataType: 'jsonp',
		type: "GET",
		cache: true,
		success: function(data, textStatus, xhr) {
			return callback(data, result.SUCCESS);
		},
		error: function(xhr, textStatus, errorThrown) {
			return callback ("", result.ERROR);
		}
	});
}

function saveDocument(url, document) {
	
}

function loadTemplate(url, callback){
	$.ajax({
		url: url,
		async: true,
		dataType: 'html',
		type: "GET",
		cache: true,
		success: function(data, textStatus, xhr) {
			return callback(data, result.SUCCESS);
		},
		error: function(xhr, textStatus, errorThrown) {
			return callback ("", result.ERROR);
		}
	});
}