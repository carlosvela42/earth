<span>ワークスペース</span>
<select id="workspaceSelection">
  <#if workspaces??>
    <#list workspaces as workspace>
        <#assign selected=(workspace.workspaceId == workspaceId)?then("selected","")>
        <option value="${workspace.workspaceId}" ${selected!""}>${workspace.workspaceName}</option>
    </#list>
  </#if>
</select>