<@standard.standardPage title="ADDNEWWORKSPACE">
<script type="text/javascript">
	function input() {
	    // Declare variables
	    var schemaNameInput;
	    schemaNameInput = document.getElementById('txtSchemaName').value;
	    $("#txtDBuser").val(schemaNameInput);
	  
	}
</script>
<form action="${rc.getContextPath()}/workspace/insertOne"
	object="workspaceForm" method="post">
	<table>
		<tr>
			<td><input type="submit" value="決定" class="button"></td>
			<td><a href="${rc.getContextPath()}/workspace"
				class="button">キャンセル</a></td>
		<tr>
			<td>ワークスペースID：</td>
			<td><input type="text" id="txtWorkspace" name="workspaceId"
				value="${workspaceForm.workspaceId!""}" height="20px" width="150px" style="text-align: left" readonly="readonly"></td>
		</tr>
		<tr>
			<td>スキーマ名：</td>
			<td><input type="text" id="txtSchemaName" onkeyup="input()"
				name="schemaName" value="${workspaceForm.schemaName!""}" height="20px" width="150px"
				style="text-align: left"></td>
		</tr>
		<tr>
			<td>DBユーザ：</td>
			<td><input type="text" id="txtDBuser" name="dbUser" value="${workspaceForm.dbUser!""}"
				height="20px" width="150px" style="text-align: left" readonly="readonly"></td>
		</tr>
		<tr>
			<td>DBユーザパスワード：</td>
			<td><input type="password" id="txtDBpassword"
				name="dbPassword" value="${workspaceForm.dbPassword!""}" height="20px" width="150px"
				style="text-align: left"></td>
		</tr>
		<tr>
			<td>オーナー：</td>
			<td><input type="text" id="txtOwner" name="owner" value="${workspaceForm.owner!""}"
				height="20px" width="150px" style="text-align: left"></td>
		</tr>
		<tr>
			<td>DBサーバ:</td>
			<td><input type="text" id="txtDBserver"
				name="dbServer" value="${workspaceForm.dbServer!""}" height="20px" width="150px"
				style="text-align: left"></td>
		</tr>
	</table>

	<#if messages??>
		<#list messages as message>
			<div>
				<b style="color: red;">${message.getContent()}</b>
			</div>
		</#list>
	</#if>
</form>
</@standard.standardPage>