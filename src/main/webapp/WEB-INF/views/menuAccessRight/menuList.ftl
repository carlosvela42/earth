<#assign script>
  <script src="${rc.getContextPath()}/resources/js/menuAuth.js"></script>
</#assign>
<@standard.standardPage title=e.get('mgrMenu.list') script=script imageLink="authority">
<div class="board-wrapper board-full">
    <#include "../common/messages.ftl">
    <form action="${rc.getContextPath()}/site/" id="siteSearchForm" object="searchForm" method="post">
    <table class="clientSearch table_list" id="listMenu">
        <thead>
        <tr class="table_header">
            <td colspan="2" style="width:25%">${e.get('mgrMenu.functionId')}</td>
            <td style="width:25%">${e.get('mgrMenu.functionName')}</td>
            <td style="width:25%">${e.get('mgrMenu.functionCategoryId')}</td>
            <td>${e.get('mgrMenu.functionCategoryName')}</td>
        </tr>
        <tr class="condition">
            <td colspan="2"><input type="text" col="2" placeholder="Search " class="searchInput" 
                   id="searchColumns[0]" name="searchColumns[0]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[0]!"",""),"")}"/></td>
            <td><input type="text" col="3" placeholder="Search " class="searchInput"
                   id="searchColumns[1]" name="searchColumns[1]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[1]!"",""),"")}"/></td>
            <td><input type="text" col="4" placeholder="Search " class="searchInput"
                   id="searchColumns[2]" name="searchColumns[2]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[2]!"",""),"")}"/></td>
            <td  style="border-left: transparent; border-right-style: solid; border-right-color: #B0AFB0;" ><input type="text" col="5" placeholder="Search "  class="searchInput"
                   id="searchColumns[3]" name="searchColumns[3]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[3]!"",""),"")}"/></td>
        </tr>
        </thead>
        <tbody id="menuTbody" class="table_body">
            <#if mgrMenus??>
                <#list mgrMenus as mgrMenu>
                <tr functionId="${mgrMenu.functionId}">
                    <td class="text_center" style="width: 40px;">
                        <a class="icon icon_edit editButton" href="#" data-id="${mgrMenu.functionId}">
                        </a>
                    </td>
                    <td class="number">${mgrMenu.functionId!""}</td>
                    <td class="text">${mgrMenu.functionName!""}</td>
                    <td class="number">${mgrMenu.functionCategoryId!""}</td>
                    <td class="text">${mgrMenu.functionCategoryName!""}</td>
                </tr>
                </#list>
            </#if>
        </tbody>
    </table>
    </form>
</div>
</@standard.standardPage>