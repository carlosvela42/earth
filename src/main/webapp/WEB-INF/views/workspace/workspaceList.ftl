<#assign contentFooter>
    <@component.removePanel></@component.removePanel>
</#assign>

<#assign script>
<script src="${rc.getContextPath()}/resources/js/workspace.js"></script>
</#assign>

<@standard.standardPage title=e.get('workspace.list') contentFooter=contentFooter script=script imageLink="process">
<div class="board-wrapper">
    <div class="board board-half">
        <#include "../common/messages.ftl">
        <form action="${rc.getContextPath()}/workspace/" id="siteSearchForm" object="searchForm" method="post">
        <table class="clientSearch table_list">
            <thead>
                <tr class="table_header">
                    <td class=""><input type="checkbox" class="deleteAllCheckBox"/></td>
                    <td class="text_center">
                        <a id="addButton" class="icon icon_add" href="#">
                        </a>
                    </td>
                    <td>${e.get('workspace.id')}</td>
                    <td>${e.get('workspace.name')}</td>
                </tr>
                <tr class="condition">
                    <td class="search_icon"><img src="${rc.getContextPath()}/resources/images/search.png"/></td>
                    <td colspan="2"><input id="searchColumns[0]" name="searchColumns[0]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[0]!"",""),"")}"
                         type="text" col="3" placeholder="search"/></td>
                    <td  style="border-left: transparent; border-right-style: solid; border-right-color: #B0AFB0;" ><input id="searchColumns[1]" name="searchColumns[1]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[1]!"",""),"")}"
                         type="text" col="4" placeholder="search"/></td>
                </tr>
            </thead>
            <tbody id="scheduleTbody" class="table_body">
                <#if mgrWorkspaces??>
                    <#list mgrWorkspaces as mgrWorkspace>
                    <tr workspaceId="${mgrWorkspace.workspaceId}">
                        <td><input type="checkbox" class="deleteCheckBox" /></td>
                        <td class="text_center">
                            <a class="icon icon_edit editButton"  href="#" data-id="${mgrWorkspace.workspaceId}"></a>
                        </td>
                        <td class="text">${mgrWorkspace.workspaceId!""}</td>
                        <td class="text">${mgrWorkspace.workspaceName!""}</td>
                    </tr>
                    </#list>
                </#if>
            </tbody>
        </table>
        </form>
    </div>
    <div class="board-split"></div>
</div>
</@standard.standardPage>
