<div id="workspaceSelection" class="btn-group clearfix pull-right workspaceSelectBox">
    <button type="button" class="name btn btn-default">${workspaceName}</button>
    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
        <span class="caret"></span>
        <span class="sr-only"></span>
    </button>
    <ul class="dropdown-menu">
      <#list workspaces as workspace>
        <#assign selected=(workspace.workspaceId == workspaceId)?then("selected","")>
          <li ${selected!""}><a data-value="${workspace.workspaceId}" data-name="${workspace.workspaceName}" href="#">
            ${workspace.workspaceName}
          </a></li>
      </#list>
    </ul>
</div>