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
  
  $(".global .btn").click(function() {
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

function initWorkspaceSelectBox() {
  $("#workspaceSelection").change(function(){
    var form = $("#filter");
    form.find("[name='workspaceId']").val($(this).val());
    form.submit();
  });
}

// All Common Init Function should be here
$(function () {
  initWorkspaceSelectBox();
})


// Common button
$(function () {
  console.log("AAA");
  $("#saveButton").click(function () {
    var formId = $(this).data("form_id");
    $("#" + formId).submit();
  })
});