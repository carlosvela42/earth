<@standard.standardPage title="EDITWORKSPACE">
<script type="text/javascript">
    function input() {
        // Declare variables
        var schemaNameInput;
        schemaNameInput = document.getElementById('txtSchemaName').value;
        $("#txtDBuser").val(schemaNameInput);
      
    }
</script>
<form action="${rc.getContextPath()}/workspace/updateOne"
	object="workspaceForm" method="post">
	<table>
		<tr>
			<td><input type="submit" value="決定" class="button"></td>
			<td><a href="${rc.getContextPath()}/workspace/list"
				class="button">キャンセル</a></td>
		</tr>
		<#if workspaceForm??>
	        <tr>
	            <td>
	                <label>ワークスペースID：</label>
	            </td>
	            <td>
	                <input type="text" id="txtWorkspace" name="workspaceId" value="${workspaceForm.workspaceId!""}" readonly="readonly" height="20px" width="150px" style="text-align: left">
	            </td>
	        </tr>
	        <tr>
	            <td>
	                <label>データベースタイプ： </label>
	            </td>
	        </tr>
	         <tr>
	            <td>
	                <label>スキーマ名： </label>
	            </td>
	            <td>
	                <input type="text" id="txtSchemaName" name="schemaName" value="${workspaceForm.schemaName!""}" height="20px" width="150px" style="text-align: left" onkeyup="input()">  
	            </td>
	        </tr>
	         <tr>
	            <td>
	                <label>DBユーザ： </label>
	            </td>
	            <td>
	                <input type="text" id="txtDBuser" name="dbUser" value="${workspaceForm.dbUser!""}" height="20px" width="150px" style="text-align: left" readonly="readonly"> 
	            </td>
	        </tr>
	         <tr>
	            <td>
	                <label>DBユーザパスワード： </label>
	            </td>
	            <td>
	                <input type="password" id="txtDBpassword" name="dbPassword" value="${workspaceForm.dbPassword!""}" height="20px" width="150px" style="text-align: left"> 
	            </td>
	        </tr>
	         <tr>
	            <td>
	                <label>オーナー： </label>
	            </td>
	            <td>
	                <input type="text" id="txtOwner" name="owner" value="${workspaceForm.owner!""}" height="20px" width="150px" style="text-align: left"> 
	            </td>
	        </tr>
	         <tr>
	            <td>
	                <label>DBサーバ： </label>
	            </td>
	            <td>
	                <input type="text" id="txtDBserver" name="dbServer" value="${workspaceForm.dbServer!""}" readonly="readonly" height="20px" width="150px" style="text-align: left">
	            </td>
	        </tr>
         </#if>
    </table>
    <#if messages??> <#list messages as message>
    <div>
        <b style="color: red;">${message.getContent()}</b>
    </div>
    </#list> </#if>
</form>
</@standard.standardPage>
