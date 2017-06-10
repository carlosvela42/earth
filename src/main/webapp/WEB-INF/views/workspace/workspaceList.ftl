<#assign contentFooter>
    <@component.removePanel></@component.removePanel>
</#assign>

<#assign script>
<script src="${rc.getContextPath()}/resources/js/workspace.js"></script>
</#assign>

<@standard.standardPage title=e.get('workspace.list') contentFooter=contentFooter script=script>
<div class="board-wrapper">
    <div class="board board-half">
        <#include "../common/messages.ftl">
        <table class="clientSearch table_list">
            <thead>
                <tr class="table_header">
                    <td class=""><input type="checkbox" class="deleteAllCheckBox"/></td>
                    <td class="text_center">
                        <a id="addButton" class="icon icon_add" href="${rc.getContextPath()}/workspace/addNew">
                        </a>
                    </td>
                    <td>${e.get('workspace.id')}</td>
                    <td>${e.get('workspace.id')}</td>
                </tr>
                <tr class="condition">
                    <td><img src="${rc.getContextPath()}/resources/images/search.png"/></td>
                    <td colspan="2"><input id="tests" type="text" col="3" placeholder="search"/></td>
                    <td><input id="tests2" type="text" col="4" placeholder="search"/></td>
                </tr>
            </thead>
            <tbody id="scheduleTbody" class="table_body">
                <#if mgrWorkspaces??>
                    <#list mgrWorkspaces as mgrWorkspace>
                    <tr workspaceId="${mgrWorkspace.workspaceId}">
                        <td><input type="checkbox" class="deleteCheckBox" /></td>
                        <td class="text_center">
                            <a class="icon icon_edit"  href="${rc.getContextPath()}/workspace/showDetail?workspaceId=${mgrWorkspace.workspaceId}"></a>
                        </td>
                        <td class="text">${mgrWorkspace.workspaceId!""}</td>
                        <td class="text">${mgrWorkspace.workspaceName!""}</td>
                    </tr>
                    </#list>
                </#if>
            </tbody>
        </table>
    </div>
    <div class="board-split"></div>
</div>
</@standard.standardPage>
