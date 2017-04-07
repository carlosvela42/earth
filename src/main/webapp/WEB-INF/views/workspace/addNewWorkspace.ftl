<form action="${rc.getContextPath()}/workspace/insertOne"
	object="mgrWorkspaceConnect" method="post">
	<table>
		<tr>
			<td><input type="submit" value="決定" class="button"></td>
			<td><a href="${rc.getContextPath()}/workspace/list"
				class="button">キャンセル</a></td>
		<tr>
			<td><label>ワークスペースID：</label></td>
			<td><input type="text" id="txtWorkspace" name="workspaceId"
				height="20px" width="150px" style="text-align: left"></td>
		</tr>
		<tr>
			<td><label>スキーマ名： </label></td>
			<td><input type="text" id="txtSchemaName"
				name="schemaName" height="20px" width="150px"
				style="text-align: left"></td>
		</tr>
		<tr>
			<td><label>DBユーザ： </label></td>
			<td><input type="text" id="txtDBuser" name="dbUser"
				height="20px" width="150px" style="text-align: left"></td>
		</tr>
		<tr>
			<td><label>DBユーザパスワード： </label></td>
			<td><input type="password" id="txtDBpassword"
				name="dbPassword" height="20px" width="150px"
				style="text-align: left"></td>
		</tr>
		<tr>
			<td><label>オーナー： </label></td>
			<td><input type="text" id="txtOwner" name="owner"
				height="20px" width="150px" style="text-align: left"></td>
		</tr>
		<tr>
			<td><label>DBサーバ： </label></td>
			<td><input type="text" id="txtDBserver"
				name="dbServer" height="20px" width="150px"
				style="text-align: left"></td>
		</tr>

	</table>

	<#if messages??> <#list messages as message>
	<div>
		<b style="color: red;">${message.getContent()}</b>
	</div>
	</#list> </#if>
</form>
