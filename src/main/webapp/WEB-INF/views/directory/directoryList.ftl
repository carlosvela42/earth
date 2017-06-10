<#assign contentFooter>
<@component.removePanel></@component.removePanel>
</#assign>

<#assign script>
<script src="${rc.getContextPath()}/resources/js/directory.js"></script>
</#assign>

<@standard.standardPage title=e.get('directory.list') contentFooter=contentFooter script=script>
<div class="board-wrapper board-full">
  <#include "../common/messages.ftl">
    <table class="clientSearch table_list">
        <thead>
        <tr class="table_header">
            <td class=""><input type="checkbox" class="deleteAllCheckBox" /></td>
            <td class="text_center">
                <a id="addButton" class="icon icon_add" href="${rc.getContextPath()}/directory/addNew">
                </a>
            </td>
            <td>${e.get('directory.id')}</td>
            <td>${e.get('folder.path')}</td>
            <td>${e.get('create.new.file')}</td>
            <td>${e.get('secured.disk.space')}</td>
            <td>${e.get('disk.space')}</td>
        </tr>
        <tr class="condition">
            <td><img src="${rc.getContextPath()}/resources/images/search.png"/></td>
            <td colspan="2"><input id="tests" type="number" col="3" placeholder="search"/></td>
            <td><input id="tests2" type="text" col="4" placeholder="search"/></td>
            <td><input type="text" col="5" placeholder="search"/></td>
            <td><input type="text" col="6" placeholder="search"/></td>
            <td><input type="text" col="7" placeholder="search"/></td>
        </tr>
        </thead>
        <tbody id="directoryTbody" class="table_body">
          <#if directorys??>
            <#list directorys as directory>
            <tr directoryId="${directory.dataDirectoryId}">
                <td><input type="checkbox" class="deleteCheckBox" /></td>
                <td class="text_center"><a class="icon icon_edit" 
                href="${rc.getContextPath()}/directory/showDetail?dataDirectoryId=${directory.dataDirectoryId}"></a></td>
                <td class="number">${directory.dataDirectoryId}</td>
                <td class="text">${directory.folderPath!""}</td>
                <td class="text">
                	<#if directory.newCreateFile == 1>
                		Y
                	<#else>
                		N
                	</#if>
                </td>
                <td class="text">${directory.reservedDiskVolSize!""}</td>
                <td class="text">${directory.diskVolSize!""}</td>
            </tr>
            </#list>
          </#if>
        </tbody>
    </table>
</div>
</@standard.standardPage>