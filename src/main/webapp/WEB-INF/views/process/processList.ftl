<#assign contentFooter>
<@component.removePanel></@component.removePanel>
</#assign>

<#assign script>
  <script src="${rc.getContextPath()}/resources/js/process.js"></script>
</#assign>

<@standard.standardPage title=e.get('process.list') contentFooter=contentFooter displayWorkspace=true script=script imageLink="process">
<div class="board-wrapper board-full">
  <#include "../common/messages.ftl">
    <form method="get" id="filter" action="">
        <input type="hidden" id="workspaceId" name="workspaceId" value="${workspaceId}">
    </form>
    <form action="${rc.getContextPath()}/process/" id="siteSearchForm" object="searchForm" method="post">
    <table class="clientSearch table_list">
        <thead>
        <tr class="table_header">
            <td class=""><input type="checkbox" class="deleteAllCheckBox" /></td>
            <td class="text_center">
                <a id="addButton" class="icon icon_add" href="#">
                </a>
            </td>
            <td>${e.get('process.id')}</td>
            <td>${e.get('process.name')}</td>
            <td>${e.get('process.version')}</td>
            <td>${e.get('process.description')}</td>
            <td>${e.get('process.savingType')}</td>
        </tr>
        <tr class="condition">
            <td class="search_icon"><img src="${rc.getContextPath()}/resources/images/search.png"/></td>
            <td colspan="2"><input id="searchColumns[0]" name="searchColumns[0]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[0]!"",""),"")}"
                    type="text" col="3" placeholder="Search"/></td>
            <td><input id="searchColumns[1]" name="searchColumns[1]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[1]!"",""),"")}"
                    type="text" col="4" placeholder="Search"/></td>
            <td><input id="searchColumns[2]" name="searchColumns[2]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[2]!"",""),"")}"
                    type="text" col="5" placeholder="Search"/></td>
            <td><input id="searchColumns[3]" name="searchColumns[3]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[3]!"",""),"")}"
                    type="text" col="6" placeholder="Search"/></td>
            <td  style="border-left: transparent; border-right-style: solid; border-right-color: #B0AFB0;" ><input id="searchColumns[4]" name="searchColumns[4]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[4]!"",""),"")}"
                    type="text" col="7" placeholder="Search"/></td>
        </tr>
        </thead>
        <tbody id="processTbody" class="table_body">
          <#if processes??>
            <#list processes as process>
            <tr processId="${process.processId}">
                <td><input type="checkbox" class="deleteCheckBox" /></td>
                <td class="text_center"><a class="icon icon_edit editButton" href="#" data-id="${process.processId}"></a></td>
                <td class="number">${process.processId}</td>
                <td class="text wrap"><#if process.processName??>${process.processName}</#if></td>
                <td class="number"><#if process.processVersion??>${process.processVersion}</#if></td>
                <td class="text wrap"><#if process.description??>${process.description}</#if></td>
                <td class="text"><#if process.documentDataSavePath??>${process.documentDataSavePath}</#if></td>
            </tr>
            </#list>
          </#if>
        </tbody>
    </table>
    </form>
</div>
</@standard.standardPage>