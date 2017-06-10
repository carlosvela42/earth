$(function () {
  earth.onDeleteButtonClick(function () {
    var processIds = [];
    $('#processTbody > tr').each(function() {
      if($(this).find('.deleteCheckBox').prop('checked')){
        processIds.push($(this).attr('processId'));
      }
    });
    $.form(window.baseUrl + "process/deleteList", {"processIds": processIds,
      workspaceId: $('#workspaceSelection').val()}).submit();
  });
});

// Add new process start
var processDefinition;
window.onload = function () {
  $('.documentDataSavePath').click(function(){
    if($(this).val()== "database"){
      $('#fileArea').hide();
      $('#databaseArea').show();
    } else if($(this).val()== "file"){
        $('#fileArea').show();
      $('#databaseArea').hide();
    }
  });

  $('#fileUpload').change(function (){
    var oFReader = new FileReader();
    oFReader.readAsText($(this).get(0).files[0]);
    oFReader.onload = function (oFREvent) {
      console.log(this.result);
      processDefinition = this.result;
    }
  });
  $('#fileDownload').click(function(){
    document.location.href = baseUrl+"downloadFile";
  });
};
function getFileExtension(filename){
  return filename.substr(filename.lastIndexOf('.')+1)
};
// Add new process end