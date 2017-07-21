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

function postImage(url, callback) {
    $.ajax({
        url: url,
        async: true,
        dataType: 'jsonp',
        type: "POST",
        cache: true,
        success: function(data, textStatus, xhr) {
            return callback(data, result.SUCCESS);
        },
        error: function(xhr, textStatus, errorThrown) {
            return callback ("", result.ERROR);
        }
    });
}

function saveDocument(url,sessionId,workspaceId, document, callback) {
	$.ajax({
		url: url,
		async: true,
		dataType: 'jsonp',
		type: "POST",
		cache: true,
		data: JSON.stringify({
			"sessionId":sessionId,
			"workspaceId":workspaceId,
			"document":document
		}),
		contentType: "application/json; charset=utf-8",
		success: function(data, textStatus, xhr) {
			return callback(data, result.SUCCESS);
		},
		error: function(xhr, textStatus, errorThrown) {
			return callback ("", result.ERROR);
		}
	});
}
