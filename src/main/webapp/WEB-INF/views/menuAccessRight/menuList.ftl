<@standard.standardPage title=e.get('mgrMenu.list') script=script>
<div class="board-wrapper board-full">
    <#include "../common/messages.ftl">
    <table class="clientSearch table_list">
        <thead>
        <tr class="table_header">
            <td colspan="2" style="width:17%">${e.get('mgrMenu.functionId')}</td>
            <td style="width:30%">${e.get('mgrMenu.functionName')}</td>
            <td style="width:20%">${e.get('mgrMenu.functionCategoryId')}</td>
            <td>${e.get('mgrMenu.functionCategoryName')}</td>
        </tr>
        <tr class="condition">
            <td class="text_center"><img src="${rc.getContextPath()}/resources/images/search.png"/></td>
            <td><input type="text" col="2" placeholder="Search "/></td>
            <td><input type="text" col="3" placeholder="Search "/></td>
            <td><input type="text" col="4" placeholder="Search "/></td>
            <td><input type="text" col="5" placeholder="Search "/></td>
        </tr>
        </thead>
        <tbody id="menuTbody" class="table_body">
            <#if mgrMenus??>
                <#list mgrMenus as mgrMenu>
                <tr functionId="${mgrMenu.functionId}">
                    <td class="text_center">
                        <a class="icon icon_edit"
                           href="${rc.getContextPath()}/menuAccessRight/showDetail?functionId=${mgrMenu.functionId}">
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
</div>
</@standard.standardPage>