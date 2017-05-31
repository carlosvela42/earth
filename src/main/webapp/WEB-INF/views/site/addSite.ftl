<@standard.standardPage title="ADDSITE">
<script>
	window.onload = function() {
		var countChecked = function() {
			var str = ""
			$('input[name=ChooseRow]:checked').each(function() {
				str += $(this).attr('value') + ",";
			});

			if (str.length > 0) {
				str = str.substring(0, str.length - 1);
			}
			$("#directoryIds").val(str);
		}
		$("input[name=ChooseRow]").on("click", countChecked);
	}
	function validate() {
		var str = $("#directoryIds").val();

		if (str.length > 0) {
			document.forms[0].submit();
		} else {
			$("#message").html("choose directory");
		}
	}
</script>
<form action="${rc.getContextPath()}/site/insertOne" object="siteForm"
	method="post">
	<input type="hidden" id="directoryIds" name="directoryIds">
	<div>
		<b id="message" style="color: red;"></b>
	</div>
	<table>
		<tr>
			<td><label>Site_ID：</label></td>
			<td><input type="text" id="txtSiteId" name="siteId"
				value="${siteForm.siteId}" height="20px" width="150px"
				style="text-align: left" readonly="readonly"></td>
		</tr>
	</table>
	<br>
	<table border="1" id="directoryTable">
		<tr style="white-space: nowrap;">
			<th colspan="2">データディレクトリID</th>
			<th>フォルダパス</th>
			<th>新規ファイル作成</th>
			<th>確保ディスク容量[MB]</th>
			<th>ディスク容量[MB]</th>
		</tr>
		<#if siteForm??>
			<#if siteForm.directories??>
	                <#list siteForm.directories as directory>
	                <tr id="row${directory?index}">
	                     <td><input type="checkbox" id="chooseRow${directory?index}"name="ChooseRow" value="${directory.dataDirectoryId}" ${directory.checked?string("checked","")}></td>
	                     <td id="dataDirectoryId${directory?index}" value="${directory.dataDirectoryId}">${directory.dataDirectoryId}</td>
	                     <td id="folderPath${directory?index}" value="${directory.folderPath}">${directory.folderPath}</td>
	                     <td id="newCreateFile${directory?index}" value="${directory.newCreateFile}">${directory.newCreateFile}</td>
	                     <td id="reservedDiskVolSize${directory?index}" value="${directory.reservedDiskVolSize}">${directory.reservedDiskVolSize}</td>
	                     <td id="diskVolSize${directory?index}" value="${directory.diskVolSize}"->${directory.diskVolSize}</td>
	                </tr>
	                </#list>
	            </#if>
	      </#if>
        </table>
	<div>
		<input type="submit" value="決定" class="button">
		<a href="${rc.getContextPath()}/site" class="button">キャンセル</a>
	</div>

	</table>

	<#if messages??> <#list messages as message>
    <div>
        <b style="color: red;">${message.getContent()}</b>
    </div>
    </#list> </#if>
</form>
</@standard.standardPage>
