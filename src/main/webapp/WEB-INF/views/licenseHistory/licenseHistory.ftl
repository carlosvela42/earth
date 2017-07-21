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

<#assign script>
<script src="${rc.getContextPath()}/resources/js/licenseHistory.js"></script>
</#assign>
<@standard.standardPage title=e.get('license.title') script=script imageLink="report">
<script id="licenseHistoryRow" type="text/x-handlebars-template ">
    {{#each strCals}}
    <tr>
        <td class="text">{{this.division}}</td>
        <td class="text processTime">{{this.processTime}}</td>
        <td class="text">{{this.profileId}}</td>
        <td class="number">{{this.availableLicenseCount}}</td>
        <td class="number">{{this.useLicenseCount}}</td>
    </tr>
    {{/each}}
</script>

<div class="board-wrapper board-full">
    <div style="clear: both;"></div>
    <#if message??>
        <div>
            <b style="color: red;">${message}</b>
        </div>
    </#if>
    ${addSearchPopupFrom}
    ${searchForm}
        <div class="clearfix" style="height: 15px;"></div>
    <table class="clientSearch table_list licenseTable">
        <thead class="table_header">
        <tr class="rowSearch">
            <td>
                <button type="button"
                        onclick="openSearchCondition(this, 0,'NCHAR','division','${e.get('license.division')}',
                            'false');"
                        class="icon btn_filter"
                        data-target="#addFormSearchColumn"></button>
                ${e.get('license.division')}
            </td>
            <td>
                <button type="button"
                        onclick="openSearchCondition(this, 1,'NCHAR','processTime','${e.get('license.processTime')}',
                            'false');"
                        class="icon btn_filter"
                        data-target="#addFormSearchColumn"></button>
                ${e.get('license.processTime')}
            </td>
            <td>
                <button type="button"
                        onclick="openSearchCondition(this, 2,'NCHAR','profileId','${e.get('license.profileId')}',
                            'false');"
                        class="icon btn_filter"
                        data-target="#addFormSearchColumn"></button>
                ${e.get('license.profileId')}
            </td>
            <td>
                <button type="button"
                        onclick="openSearchCondition(this, 3,'NUMBER','availableLicenseCount','${e.get('license.numOfAvailableLicense')}','false');"
                        class="icon btn_filter"
                        data-target="#addFormSearchColumn"></button>
                ${e.get('license.numOfAvailableLicense')}
            </td>
            <td>
                <button type="button"
                        onclick="openSearchCondition(this, 4,'NUMBER','useLicenseCount','${e.get('license.numOfUsedLicense')}','false');"
                        class="icon btn_filter"
                        data-target="#addFormSearchColumn"></button>
                ${e.get('license.numOfUsedLicense')}
            </td>
        </tr>
        </thead>
        <tbody id="licenseHistoryTBody"  class="table_body">
            <#if strCals?? && strCals?has_content>
                <#list strCals as strCal>
                <tr>
                    <td class="text">${strCal.division!""}</td>
                    <td class="text processTime">${strCal.processTime!""}</td>
                    <td class="text">${strCal.profileId!""}</td>
                    <td class="number">${strCal.availableLicenseCount!""}</td>
                    <td class="number">${strCal.useLicenseCount!""}</td>
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
</@standard.standardPage>