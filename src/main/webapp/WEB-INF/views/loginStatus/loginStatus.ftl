<#assign searchForm>
    <@component.searchColumnFormPanel object="" formId=""></@component.searchColumnFormPanel>
</#assign>

<#assign addSearchPopupFrom>
    <@component.searchFormPopup></@component.searchFormPopup>
</#assign>

<#assign addFrom>
    <@component.workItemSearch></@component.workItemSearch>
</#assign>
<#assign addPopupFrom>
    <@component.searchPopup></@component.searchPopup>
</#assign>
<#assign addSearchFrom>
    <@component.searchColumnsForm></@component.searchColumnsForm>
</#assign>
<#assign loginFormPopup>
    <@component.loginFormPopup></@component.loginFormPopup>
</#assign>

<#assign script>
<script src="${rc.getContextPath()}/resources/js/loginStatus.js"></script>
</#assign>

<@standard.standardPage title=e.get('login.status') script=script imageLink="report">
<div class="board-wrapper board-full">
    <#include "../common/messages.ftl">
    ${addSearchPopupFrom}
    ${searchForm}
    <div class="clearfix" style="height: 15px;"></div>
    <table class="clientSearch table_list">
        <thead>
        <tr class="table_header rowSearch">
            <td>
                <button type="button" onclick="openSearchCondition(this, 0,'NCHAR','userId','${e.get('user.id')}','false');"
                         class="icon btn_filter"
                         data-target="#addFormSearchColumn"></button>
                ${e.get('user.id')}
            </td>
            <td>
                <button type="button" onclick="openSearchCondition(this, 1,'NCHAR','loginTime','${e.get('login.loginTime')}','false');"
                        class="icon btn_filter"
                        data-target="#addFormSearchColumn"></button>
                ${e.get('login.loginTime')}
            </td>
            <td>
                <button type="button" onclick="openSearchCondition(this, 2,'NCHAR','logoutTime','${e.get('login.logoutTime')}','false');"
                        class="icon btn_filter"
                        data-target="#addFormSearchColumn"></button>
                ${e.get('login.logoutTime')}
            </td>
            <td>
                <button type="button" onclick="openSearchCondition(this, 3,'NCHAR','sessionId','${e.get('login.sessionId')}','false');"
                        class="icon btn_filter"
                        data-target="#addFormSearchColumn"></button>
                ${e.get('login.sessionId')}
            </td>
        </tr>
        </thead>
        <tbody id="evidentTBody" class="table_body">
            <#if ctlLogins??>
                <#list ctlLogins as ctlLogin>
                <tr>
                    <td class="text">${ctlLogin.userId!""}</td>
                    <td class="text_time">${ctlLogin.loginTime!""}</td>
                    <td class="text">${ctlLogin.logoutTime!""}</td>
                    <td class="text">${ctlLogin.sessionId!""}</td>
                </tr>
                </#list>
            <#else>
                <#include "../common/noResult.ftl">
            </#if>
        </tbody>
    </table>

</div>
${addPopupFrom}
${addSearchFrom}
${addFrom}
${loginFormPopup}
</@standard.standardPage>