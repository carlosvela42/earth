var baseUrl;
$(function () {
  baseUrl = "${rc.getContextPath()}/process/";

  // $('#addButton').click(function(){
  //   document.location.href = baseUrl+"addNew?workspaceId="+$("#workspaceSelection").val();
  // });

  $('#editButton').click(function(){
    var processId;
    $('#processTbody > tr').each(function() {
      if($(this).find('.deleteCheckBox').prop('checked')){
        processId = Number($(this).attr('processId'));
      }
    });
    document.location.href = baseUrl+"showDetail?workspaceId="+$("#workspaceSelection").val()+"&processId="+processId;
  });
  $('#deleteButton').click(function(){
    var deleteProcessForm = {};
    var processIds = [];
    $('#processTbody > tr').each(function() {
      if($(this).find('.deleteCheckBox').prop('checked')){
        processIds.push(Number($(this).attr('processId')));
      }
    });
    deleteProcessForm.processIds = processIds;
    deleteProcessForm.confirmDelete = $('#deleteConfirm').prop('checked');
    deleteProcessForm.workspaceId = $('#workspaceSelection').val();
    $.ajax({
      url: baseUrl+"deleteList",
      contentType:'application/json',
      data: JSON.stringify(deleteProcessForm),
      dataType: 'json',
      type: 'POST',
      success: function(data) {
        if(data.message){
          alert(data.message);
        }else if(data.result){
          document.location.href = baseUrl+"showList?workspaceId="+$('#workspaceSelection').val();
        }
      }
    });
  });

  $('.searchTxt').keyup(function(){
    var array = [];
    $('.searchTxt').each(function(){
      if($(this).val()){
        var searchTxt = $(this).val();
        var col = $(this).attr('col');
        var count = 0;
        $('#processTbody > tr').each(function() {
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
      $('#processTbody > tr').each(function() {
        $(this).show();
      });
    }
  });
});

// Add new process start
var baseUrl;
var processDefinition;
window.onload = function () {
  baseUrl = "${rc.getContextPath()}/process/";
  $('#fileArea').hide();
  $('#databaseArea').show();
  $('.documentDataSavePath').click(function(){
    if($(this).val()== "database"){
      $('#fileArea').hide();
      $('#databaseArea').show();
      documentDataSavePath = $(this).val();
    } else if($(this).val()== "file"){
      $('#fileArea').show();
      $('#databaseArea').hide();
      documentDataSavePath = $(this).val();
    }
  });
  $('#cancel').click(function(){
    document.location.href = baseUrl+"showList";
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