<#assign contentFooter>
    <@component.detailUpdatePanel object="user" formId="userForm"></@component.detailUpdatePanel>
</#assign>

<#assign script>
<script src="${rc.getContextPath()}/resources/js/user.js"></script>
</#assign>

<@standard.standardPage title=e.get("user.edit") imageLink="user" contentFooter=contentFooter script=script>
<br>
    <#assign isPersisted = (user.lastUpdateTime??)>
    <#assign formAction = isPersisted?then('updateOne', 'insertOne')>
    <#assign readonly = (formAction=='updateOne')?then('readonly',"")>
<form id="userForm" action="${rc.getContextPath()}/user/${formAction}" object="userForm" method="post" class="">
    <#include "../common/messages.ftl">
    <div class="board-wrapper">
        <div class="board board-half">
            <table class="table_form">
                <tr>
                    <td width="50%">${e.get('user.id')}</td>
                    <td>
                        <input type="text" name="userId" value="${user.userId!""}" ${readonly!""}>
                    </td>
                </tr>
                <tr>
                    <td width="50%">${e.get('user.name')}</td>
                    <td>
                        <input type="text" id="txtName" name="name" height="20px" width="150px" style="text-align: left"
                               value="${user.name!""}">
                    </td>
                </tr>
                <tr>
                    <td width="50%">${e.get('user.changePassword')}</td>
                    <td>
                        <input type="checkbox" name="changePassword">
                    </td>
                </tr>
                <tr>
                    <td width="50%">${e.get('user.password')}</td>
                    <td>
                        <input type="password" name="password" disabled="disabled"">
                    </td>
                </tr>
                <tr>
                    <td width="50%">${e.get('user.confirmPassword')}</td>
                    <td>
                        <input type="password" name="confirmPassword" disabled="disabled">
                    </td>
                </tr>
            </table>
            <div><input type="hidden" name="lastUpdateTime" value="${user.lastUpdateTime!""}"/></div>
        </div>
        <div class="board-split"></div>
        <div class="board board-half">
        <div>${e.get('profile')}</div>
        <br>
            <table class="clientSearch table_list">
                <thead>
                <tr class="table_header">
                    <td  colspan="2">${e.get('profile.id')}</td>
                    <td>${e.get('profile.description')}</td>
                </tr>
                <tr class="condition">
                    <td style="width: 5%;"><img src="${rc.getContextPath()}/resources/images/search.png"/></td>
                    <td ><input type="text" col="1" placeholder="Search for ID.."></td>
                    <td ><input type="text" col="2" placeholder="Search for name.."></td>
                </tr>
                </thead>
                <tbody id="userTbody" class="table_body">
                    <#if mgrProfiles??>
                        <#list mgrProfiles as mgrProfile>
                        <tr profileId="${mgrProfile.profileId}">
                            <td class="text" colspan="2">${mgrProfile.profileId!""}</td>
                            <td class="text">${mgrProfile.description!""}</td>
                        </tr>
                        </#list>
                    </#if>
                </tbody>
            </table>
        </div>
    </div>
</form>
</@standard.standardPage>