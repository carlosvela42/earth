<#assign searchForm>
    <@component.searchFormPanel object="loginView" formId="evidentForm"></@component.searchFormPanel>
</#assign>


<@standard.standardPage title=e.get('login.status') script=script>
<div class="board-wrapper board-full">
    <#include "../common/messages.ftl">

    ${searchForm}

    <table class="clientSearch table_list">
        <thead>
        <tr class="table_header" style="white-space: nowrap;">
            <td>${e.get('user.id')}</td>
            <td>${e.get('login.loginTime')}</td>
            <td>${e.get('login.logoutTime')}</td>
            <td>${e.get('login.sessionId')}</td>
        </tr>
        </thead>
        <tbody id="evidentTbody" class="table_body">
            <#if ctlLogins??>
                <#list ctlLogins as ctlLogin>
                <tr>
                    <td class="text">${ctlLogin.userId!""}</td>
                    <td class="text">${ctlLogin.loginTime!""}</td>
                    <td class="text">${ctlLogin.logoutTime!""}</td>
                    <td class="text">${ctlLogin.sessionId!""}</td>
                </tr>
                </#list>
            </#if>
        </tbody>
    </table>
</div>
</@standard.standardPage>