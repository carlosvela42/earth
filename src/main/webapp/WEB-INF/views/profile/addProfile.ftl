<#assign contentFooter>
    <@component.detailUpdatePanel object="profile" formId="profileForm"></@component.detailUpdatePanel>
</#assign>

<#assign script>
<script src="${rc.getContextPath()}/resources/js/profile.js"></script>
</#assign>

<@standard.standardPage title=e.get("profile.edit") imageLink="user" contentFooter=contentFooter script=script>
<br>
    <#assign isPersisted = (mgrProfile.lastUpdateTime??)>
    <#assign formAction = isPersisted?then('updateOne', 'insertOne')>
    <#assign readonly = (formAction=='updateOne')?then('readonly',"")>

<form  id="profileForm"  action="${rc.getContextPath()}/profile/${formAction}"  object="profileForm" method="post"
       class="">

    <#include "../common/messages.ftl">
    <div class="board-wrapper">
        <div class="board profile-scroll">
            <table class="table_form">
                <tr>
                    <td width="50%">${e.get('profile.id')}</td>
                    <td>
                        <input type="text" name="profileId" value="${mgrProfile.profileId!""}" ${readonly!""}>
                    </td>
                </tr>
                <tr>
                    <td width="50%">${e.get('profile.description')}</td>
                    <td>
                        <input type="text" name="description" value="${mgrProfile.description!""}">
                    </td>
                </tr>
                <tr>
                    <td width="50%">${e.get('profile.ldapIdentifier')}</td>
                    <td>
                        <input type="text" name="ldapIdentifier" value="${mgrProfile.ldapIdentifier!""}">
                    </td>
                </tr>
                <tr>
                    <td width="50%">${e.get('user.list')}</td>
                    <td>

                    </td>
                </tr>
            </table>
            <div><input type="hidden" name="lastUpdateTime" value="${mgrProfile.lastUpdateTime!""}"/></div>

            <table class="clientSearch table_list">
                <thead>
                <tr class="table_header profile-border">
                    <td class=""><input type="checkbox" class="deleteAllCheckBox"/></td>
                    <td class="text profile-width">${e.get('user.id')}</td>
                    <td class="text">${e.get('user.description')}</td>
                </tr>
                </thead>
                <tbody id="userTbody" class="table_body">
                    <#if mgrUsers??>
                        <#list mgrUsers as mgrUser>
                        <tr userId="${mgrUser.userId}">
                            <td><input type="checkbox" class="deleteCheckBox"
                            ${userIds?seq_contains(mgrUser.userId)?string("checked","")}
                            /></td>
                            <td class="text">${mgrUser.userId!""}</td>
                            <td class="text">${mgrUser.name!""}</td>
                        </tr>
                        </#list>
                    </#if>
                </tbody>
            </table>
        </div>
        <div class="board-split"></div>

    </div>
    <input type="hidden" id="userIds" name="userIds" value="${strUserId!""}">
</form>
</@standard.standardPage>