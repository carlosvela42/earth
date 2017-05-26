<@standard.standardPage title="NEWDIRECTORY">
<script type="text/javascript">
$(document).ready(function(){
	function getSize(){
        var folderPath = $("#txtFolderPath").val();
        folderPath = folderPath.replace(/\\/g, "/");
        var urlSwitchWorkspace = "http://localhost:8080/Earth/directory/getSizeFolder" + "?folderPath=" + folderPath; 
          $.ajax({
              type: 'GET',
              url: urlSwitchWorkspace,
              dataType: "json",
              success: function(data){
                 $("#txtDiskVolSize").val(data);
              }
          });
      }
  
$("#directoryForm").on('keyup keypress', function(e) {
        var keyCode = e.keyCode || e.which;
        if (keyCode === 13) { 
            getSize();
          e.preventDefault();
          return false;
        }
});
}); 
  
	 
</script>
<form action="${rc.getContextPath()}/directory/insertOne"
    object="directoryForm" method="post" id="directoryForm">
    <input type="hidden" id="folderPath"name="workspaceId" value="0">
    <table>
        <tr>
            <td>データディレクトリID</td>
            <td><input type="text" id="txtDirectoryForm" name="dataDirectoryId"
                value="${directoryForm.dataDirectoryId!""}" height="20px" width="150px"
                style="text-align: left" readonly="readonly"></td>
        </tr>
        <tr>
            <td colspan="2">新規ファイル作成</td>
            <td><input type="radio" name="newCreateFile" value="1">許可する<br>
                <input type="radio" name="newCreateFile" value="0"> 許可しない<br></td>
        </tr>
        <tr>
            <td>確保ディスク容量[MB]</td>
            <td><input type="text" id="txtReservedDiskVolSize"
                value="${directoryForm.reservedDiskVolSize!""}" name="reservedDiskVolSize"
                height="20px" width="150px" style="text-align: left"></td>
        </tr>
        <tr>
            <td>ディスク容量[MB]</td>
            <td><input type="text" id="txtDiskVolSize"
                value="${directoryForm.diskVolSize!""}" name="diskVolSize"
                height="20px" width="150px" style="text-align: left" readonly="readonly"></td>
        </tr>
        <tr>
            <td>フォルダパス</td>
            <td><input type="text" id="txtFolderPath"
                value="${directoryForm.folderPath!""}" name="folderPath" 
                height="20px" width="150px" style="text-align: left"></td>
        </tr>
    </table>
    <br>
    <table style="width: 100%;">
        <tr>
            <td align="center" style="width: 50%;"><a
                href="${rc.getContextPath()}/directory/showList" class="button">キャンセル</a></td>
            <td align="center"><input type="submit" value="決定"
                class="button"></td>
        </tr>
    </table>
    <#if messages??> <#list messages as message>
    <div>
        <b style="color: red;">${message.getContent()}</b>
    </div>
    </#list> </#if>
</form>
</@standard.standardPage>
