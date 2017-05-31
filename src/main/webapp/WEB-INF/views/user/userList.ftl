<#assign contentFooter>
    <@component.removePanel></@component.removePanel>
</#assign>

<#assign script>
<script src="${rc.getContextPath()}/resources/js/user.js"></script>
</#assign>

<@standard.standardPage title=e.get('user.list') contentFooter=contentFooter script=script>
<div class="board-wrapper">
    <div class="board board-half">
        <#include "../common/messages.ftl">
        <table class="clientSearch table_list">
            <thead>
            <tr class="table_header">
                <td class=""><input type="checkbox" class="deleteAllCheckBox"/></td>
                <td>
                    <a id="addButton" class="icon icon_add" href="${rc.getContextPath()}/user/addNew">
                    </a>
                </td>
                <td>${e.get('user.id')}</td>
                <td>${e.get('user.name')}</td>
            </tr>
            <tr class="condition">
                <td><span class="icon icon_search"></span></td>
                <td colspan="2"><input type="text" col="3" placeholder="Search for ID.."></td>
                <td><input type="text" col="4" placeholder="Search for name.."></td>
            </tr>
            </thead>
            <tbody id="userTbody" class="table_body">
                <#if mgrUsers??>
                    <#list mgrUsers as mgrUser>
                    <tr userId="${mgrUser.userId}">
                        <td><input type="checkbox" class="deleteCheckBox"/></td>
                        <td class="text_center"><a class="icon icon_edit"
                                                   href="${rc.getContextPath()}/user/showDetail?userId=${mgrUser.userId}"></a>
                        </td>
                        <td class="text">${mgrUser.userId!""}</td>
                        <td class="text">${mgrUser.name!""}</td>
                    </tr>
                    </#list>
                <#else>
                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                </#if>
            </tbody>
        </table>
    </div>
    <div class="board-split"></div>
</div>
</@standard.standardPage>