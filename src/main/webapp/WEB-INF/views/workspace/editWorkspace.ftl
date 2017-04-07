<@standard.standardPage title="ADDNEWWORKSPACE">
<form action="${rc.getContextPath()}/workspace/updateOne"
	object="mgrWorkspaceConnect" method="post">
	<table>
		<tr>
			<td><input type="submit" value="決定" class="button"></td>
			<td><a href="${rc.getContextPath()}/workspace/list"
				class="button">キャンセル</a></td>
		</tr>
		<#if mgrWorkspaceConnect??>
	        <tr>
	            <td>
	                <label>ワークスペースID：</label>
	            </td>
	            <td>
	                <input type="text" id="txtWorkspace" name="workspaceId" value="${mgrWorkspaceConnect.workspaceId}" readonly height="20px" width="150px" style="text-align: left">
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
	                <input type="text" id="txtSchemaName" name="schemaName" value="${mgrWorkspaceConnect.schemaName}" height="20px" width="150px" style="text-align: left">  
	            </td>
	        </tr>
	         <tr>
	            <td>
	                <label>DBユーザ： </label>
	            </td>
	            <td>
	                <input type="text" id="txtDBuser" name="dbUser" value="${mgrWorkspaceConnect.dbUser}" height="20px" width="150px" style="text-align: left"> 
	            </td>
	        </tr>
	         <tr>
	            <td>
	                <label>DBユーザパスワード： </label>
	            </td>
	            <td>
	                <input type="password" id="txtDBpassword" name="dbPassword" value="${mgrWorkspaceConnect.dbPassword}" height="20px" width="150px" style="text-align: left"> 
	            </td>
	        </tr>
	         <tr>
	            <td>
	                <label>オーナー： </label>
	            </td>
	            <td>
	                <input type="text" id="txtOwner" name="owner" value="${mgrWorkspaceConnect.owner}" height="20px" width="150px" style="text-align: left"> 
	            </td>
	        </tr>
	         <tr>
	            <td>
	                <label>DBサーバ： </label>
	            </td>
	            <td>
	                <input type="text" id="txtDBserver" name="dbServer" value="${mgrWorkspaceConnect.dbServer}" readonly height="20px" width="150px" style="text-align: left">
	            </td>
	        </tr>
         </#if>
    </table>
</form>
</@standard.standardPage>
