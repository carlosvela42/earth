// function getUrlParameter(sParam) {
//   var sPageURL = decodeURIComponent(window.location.search.substring(1)),
//       sURLVariables = sPageURL.split('&'), sParameterName, i;
//   for (i = 0; i < sURLVariables.length; i++) {
//     sParameterName = sURLVariables[i].split('=');
//     if (sParameterName[0] === sParam) {
//       return sParameterName[1] === undefined ? true : sParameterName[1];
//     }
//   }
// };

window.earth = {}

$(function() {
  window.baseUrl = $("#data_baseUrl").val() ? $("#data_baseUrl").val() : "/";
  console.log("baseURL: " + baseUrl);
});

$(function() {
  
  var ww = $(window).width();
  var wh = $(window).height();
  
  
  $("body").css({
    "min-height": wh
  });
  
  $(".global ul").css({
    "height": wh - 60
  });
  
  $(".global .navigation_btn").click(function() {
    $(".global > ul").fadeToggle();
  });
  
  $(".global > ul > li").click(function() {
    $(this).siblings().find("ul").hide("fast");
    $(this).find("ul").stop().fadeToggle("fast");
  });
  
  $(".global > ul > li").hover(function() {
     $(this).addClass("active");
  },
  function(){
    $(this).removeClass("active");
  });
  
  
});

$(window).on('resize', function(){
  
  var ww = $(window).width();
  var wh = $(window).height();
  
  $(".global ul").css({
    "height": wh - 60
  });
  
  $("body").css({
    "min-height": wh
  });
  
});

jQuery(function($) { $.extend({
  form: function(url, data, method) {
    if (method == null) method = 'POST';
    if (data == null) data = {};

    var form = $('<form>').attr({
      method: method,
      action: url
    }).css({
      display: 'none'
    });

    var addData = function(name, data) {
      if ($.isArray(data)) {
        for (var i = 0; i < data.length; i++) {
          var value = data[i];
          addData(name + '[' + i + ']', value);
        }
      } else if (typeof data === 'object') {
        for (var key in data) {
          if (data.hasOwnProperty(key)) {
            addData(name + '[' + key + ']', data[key]);
          }
        }
      } else if (data != null) {
        form.append($('<input>').attr({
          type: 'hidden',
          name: String(name),
          value: String(data)
        }));
      }
    };

    for (var key in data) {
      if (data.hasOwnProperty(key)) {
        addData(key, data[key]);
      }
    }

    return form.appendTo('body');
  }
}); });

function initWorkspaceSelectBox() {
  $("#workspaceSelection li a").click(function(e){
    e.preventDefault();
    $("#workspaceSelection .workspaceName").html($(this).data("name"));
    var form = $("#filter");
    form.find("[name='workspaceId']").val($(this).data("value"));
    form.submit();
  });
}

// All Common Init Function should be here
$(function () {
  initWorkspaceSelectBox();
});


// Common button
$(function () {
  $("#saveButton").click(function () {
    var formId = $(this).data("form_id");
    $("#" + formId).submit();
  })
});

//search box
$(function () {
  $('.clientSearch .condition td > input').keyup(function(){
    var array = [];
    var table = $(this).parents("table");
    $('.clientSearch .condition td > input').each(function(){
      if($(this).val()){
        var searchTxt = $(this).val();
        console.log(searchTxt);
        var col = $(this).attr('col');
        var count = 0;
        table.find('tbody > tr').each(function() {
          if(!searchTxt || $(this).find('td:nth-child('+col+')').text().indexOf(searchTxt)>=0){
            if(!array[count]){
              array[count] = 1;
              $(this).show();
            }else{
              if(array[count] === 1){
                $(this).show();
              }else{
                $(this).hide();
              }
            }
          }else{
            array[count] = 2;
            $(this).hide();
          }
          count++;
        });
      }
    });
    if(array.length === 0){
      table.find('tbody > tr').each(function() {
        $(this).show();
      });
    }
  });
});

earth.Messages = {
  E0013: 'No checked box is selected.',
  E0014: 'You did not tick into CheckBox "Confirm Delete" before deleting.',
};

// Delete button
earth.onDeleteButtonClick = function (cb) {
  $('#deleteButton').click(function(){
    earth.removeMessage(["E0014", "E0013"]);

    if(!$('#deleteConfirm').prop('checked')) {
      earth.addMessage("E0014");
      return;
    }

    if($(".table_list .table_body td:nth-child(1) input:checked").length == 0) {
      earth.addMessage("E0013");
      return;
    }
    cb();
  });
};

earth.addMessage = function (code, args) {
  var source   = $("#message-template").html();
  var template = Handlebars.compile(source);
  var message = earth.Messages[code];
  if(!message){
    alert("Error: message does not exists. Add it to javascript.")
    return;
  }


  var messageDiv = $("#messages");
  if(messageDiv.find("b[data-code='" + code + "']").length == 0) {
    messageDiv.append( template({code: code, message: message}));
  }
};

earth.removeMessage = function (codes) {
  var messageDiv = $("#messages");
  codes.forEach(function (code) {
    messageDiv.find("b[data-code='" + code + "']").remove();
  });
}

