<#assign contentFooter>
    <@component.removePanel></@component.removePanel>
</#assign>

<#assign script>
<script src="${rc.getContextPath()}/resources/js/user.js"></script>
</#assign>

<@standard.standardPage title=e.get('user.list') imageLink="user" contentFooter=contentFooter script=script>
<div class="board-wrapper">
    <div class="board board-half">
        <#include "../common/messages.ftl">
        <form action="${rc.getContextPath()}/user/" id="siteSearchForm" object="searchForm" method="post">
        <table class="clientSearch table_list">
            <thead>
            <tr class="table_header">
                <td class=""><input type="checkbox" class="deleteAllCheckBox"/></td>
                <td class="text_center">
                    <a id="addButton" class="icon icon_add" href="#">
                    </a>
                </td>
                <td>${e.get('user.id')}</td>
                <td>${e.get('user.name')}</td>
            </tr>
            <tr class="condition">
                <td class="search_icon"><img src="${rc.getContextPath()}/resources/images/search.png"/></td>
                <td colspan="2"><input id="searchColumns[0]" name="searchColumns[0]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[0]!"",""),"")}"
                          type="text" col="2" placeholder="Search for ID.."></td>
                <td  style="border-left: transparent; border-right-style: solid; border-right-color: #B0AFB0;" ><input id="searchColumns[1]" name="searchColumns[1]" value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[1]!"",""),"")}"
                          type="text" col="3" placeholder="Search for name.."></td>
            </tr>
            </thead>
            <tbody id="userTbody" class="table_body">
                <#if mgrUsers??>
                    <#list mgrUsers as mgrUser>
                    <tr userId="${mgrUser.userId}">
                        <td><input type="checkbox" class="deleteCheckBox"/></td>
                        <td class="text_center text_icon"><a class="icon icon_edit editButton" href="#" data-id="${mgrUser.userId}"></a>
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
        </form>
    </div>
    <div class="board-split"></div>
</div>
</@standard.standardPage>