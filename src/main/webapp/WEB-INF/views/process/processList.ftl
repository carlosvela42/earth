<#assign contentFooter>
<@component.removePanel></@component.removePanel>
</#assign>

<#assign script>
  <script src="${rc.getContextPath()}/resources/js/process.js"></script>
</#assign>

<@standard.standardPage title=e.get('process.list') contentFooter=contentFooter displayWorkspace=true script=script>
<div class="board-wrapper board-full">
  <#include "../common/messages.ftl">
    <form method="get" id="filter" action="">
        <input type="hidden" name="workspaceId" value="${workspaceId}">
    </form>
    <table class="clientSearch table_list">
        <thead>
        <tr class="table_header">
            <td class=""><input type="checkbox" class="deleteAllCheckBox" /></td>
            <td>
                <a id="addButton" class="icon icon_add" href="${rc.getContextPath()}/process/addNew">
                </a>
            </td>
            <td>${e.get('process.id')}</td>
            <td>${e.get('process.name')}</td>
            <td>${e.get('process.version')}</td>
            <td>${e.get('process.description')}</td>
            <td>${e.get('process.savingType')}</td>
        </tr>
        <tr class="condition">
            <td><span class="icon icon_search"></span></td>
            <td colspan="2"><input id="tests" type="text" col="3" placeholder="Search process ID"/></td>
            <td><input id="tests2" type="text" col="4" placeholder="Search process name"/></td>
            <td><input type="text" col="5" placeholder="Search process version"/></td>
            <td><input type="text" col="6" placeholder="Search description"/></td>
            <td><input type="text" col="7" placeholder="Search document data save path"/></td>
        </tr>
        </thead>
        <tbody id="processTbody" class="table_body">
          <#if processes??>
            <#list processes as process>
            <tr processId="${process.processId}">
                <td><input type="checkbox" class="deleteCheckBox" /></td>
                <td class="text_center"><a class="icon icon_edit" href="${rc.getContextPath()
                }/process/showDetail?processId=${process
                .processId}"></a></td>
                <td class="number">${process.processId}</td>
                <td class="text"><#if process.processName??>${process.processName}</#if></td>
                <td class="number"><#if process.processVersion??>${process.processVersion}</#if></td>
                <td class="text"><#if process.description??>${process.description}</#if></td>
                <td class="text"><#if process.documentDataSavePath??>${process.documentDataSavePath}</#if></td>
            </tr>
            </#list>
          </#if>
        </tbody>
    </table>
</div>
</@standard.standardPage>