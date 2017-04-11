<form action="${rc.getContextPath()}/templateAccessRight/chooseWorkspace" method="get" id="wspaceListForm" object="mgrWorkspace" >
    <div>
    <label>ワークスペース：</label> 
    
    
    <select onchange="this.form.submit()" id="mgrWorkspaceList" name="workspaceId"> 
       <option value="">--Select--</option>
	        <#if mgrWorkspaces??>
	       <#list mgrWorkspaces as mgrWorkspace>
	           <option value="${mgrWorkspace.workspaceId}" <#if workspaceId??><#if mgrWorkspace.workspaceId == workspaceId> selected </#if></#if>>${mgrWorkspace.workspaceName}</option>
	        </#list>
	        </#if>
    </select>
</div>
</form>
<div>
    <label>テンプレート一覧</label>
   
    <#if message??>
        <label>${message}</label>
    </#if>
    <table border="1">
        <tr>
            <td>テンプレートID</td>
            <td>テンプレート名</td>
        </tr>
        <#if mgrTemplates?? && workspaceId??>
        <#list mgrTemplates as mgrTemplate>
            <tr>
                <td>${mgrTemplate.templateId}</td>
                <td><a href="${rc.getContextPath()}/templateAccessRight/showDetail?templateId=${mgrTemplate.templateId}&workspaceId=${workspaceId}">${mgrTemplate.templateName}</a></td>
            </tr>
        </#list>
        <#else>
            <tr>
                <td></td>
                <td></td>
            </tr>
        </#if>
    </table>
</div>
