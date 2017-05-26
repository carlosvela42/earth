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
<form action="${rc.getContextPath()}/site/insertOne" object="site"
	method="post">
	<input type="hidden" id="directoryIds" name="directoryIds">
	<div>
		<b id="message" style="color: red;"></b>
	</div>
	<table>
		<tr>
			<td><label>Site_ID：</label></td>
			<td><input type="text" id="txtSiteId" name="siteId"
				value="${site.siteId}" height="20px" width="150px"
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
		<#if directorys??>
                <#list directorys as directory>
                <tr id="row${directory?index}">
                     <td><input type="checkbox" id="chooseRow${directory?index}"name="ChooseRow" value="${directory.dataDirectoryId}"></td>
                     <td id="dataDirectoryId${directory?index}">${directory.dataDirectoryId}</td>
                     <td id="folderPath${directory?index}">${directory.folderPath}</td>
                     <td id="newCreateFile${directory?index}">${directory.newCreateFile}</td>
                     <td id="reservedDiskVolSize${directory?index}">${directory.reservedDiskVolSize}</td>
                     <td id="diskVolSize${directory?index}">${directory.diskVolSize}</td>
                </tr>
                </#list>
            </#if>
        </table>
	<div>
		<input type="button" value="決定" class="button" onclick="validate()">
		<a href="${rc.getContextPath()}/site/showList" class="button">キャンセル</a>
	</div>

	</table>

	<#if messages??> <#list messages as message>
    <div>
        <b style="color: red;">${message.getContent()}</b>
    </div>
    </#list> </#if>
</form>
</@standard.standardPage>
