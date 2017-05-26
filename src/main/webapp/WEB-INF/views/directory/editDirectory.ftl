<@standard.standardPage title="EDITDIRECTORY">
<script>
$(function() {
	$('input[name=CreateFile]:radio').change(
			function() {
				if($("input[name=CreateFile]:checked").val()=="enable"){
					$("#newCreateFile").val(1);
				}else{
					$("#newCreateFile").val(0);
				}
			});
})
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
  
$("#editDirectoryForm").on('keyup keypress', function(e) {
        var keyCode = e.keyCode || e.which;
        if (keyCode === 13) { 
            getSize();
          e.preventDefault();
          return false;
        }
});
}); 
</script>
<form action="${rc.getContextPath()}/directory/updateOne" id="editDirectoryForm"
	object="directoryForm" method="post">
	<input type="hidden" id="folderPath" name="workspaceId" value="0">
	<table>
		<tr>
			<td>データディレクトリID</td>
			<td><input type="text" id="txtDirectoryForm"
				name="dataDirectoryId" value="${directoryForm.dataDirectoryId!"
				"}" height="20px" width="150px" style="text-align: left"
				readonly="readonly"></td>
		</tr>
		<tr>
			<td colspan="2">新規ファイル作成</td>
			<td><#if directoryForm.newCreateFile=="1"> 
			         <#assign enable="checked"></#if> 
			    <#if directoryForm.newCreateFile=="0">
				     <#assign disable="checked"></#if> 
				<input type="radio" name="CreateFile" value="enable"${enable!""}>許可する<br>
				<input type="radio" name="CreateFile" value="disable"${disable!""}>許可しない<br> 
				<input type="hidden" id="newCreateFile" name="newCreateFile" height="20px" width="150px"
				            style="text-align: left" value="${directoryForm.newCreateFile!""}">


			</td>
		</tr>
		<tr>
			<td>確保ディスク容量[MB]</td>
			<td><input type="text" id="txtReservedDiskVolSize"
				value="${directoryForm.reservedDiskVolSize!"
				"}" name="reservedDiskVolSize" height="20px" width="150px"
				style="text-align: left"></td>
		</tr>
		<tr>
			<td>ディスク容量[MB]</td>
			<td><input type="text" id="txtDiskVolSize"
				value="${directoryForm.diskVolSize!" "}" name="diskVolSize"
				height="20px" width="150px" style="text-align: left"
				readonly="readonly"></td>
		</tr>
		<tr>
			<td>フォルダパス</td>
			<td><input type="text" id="txtFolderPath"
				value="${directoryForm.folderPath!" "}" name="folderPath"
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
