<#assign contentFooter>
    <@component.removePanel></@component.removePanel>
</#assign>

<#assign script>
<script src="${rc.getContextPath()}/resources/js/site.js"></script>
</#assign>

<@standard.standardPage title=e.get('site.list') contentFooter=contentFooter displayWorkspace=false script=script>
    <#include "../common/messages.ftl">
<div class="board-wrapper">
    <div class="board board-half">
        <table class="clientSearch table_list">
            <thead>
            <tr class="table_header">
                <td class=""><input type="checkbox" class="deleteAllCheckBox"/></td>
                <td class="text_center"><a href="${rc.getContextPath()}/site/addNew" class="icon icon_add"></a></td>
                <td>${e.get('site.id')}</td>
            </tr>
            <tr class="condition">
                <td><img src="${rc.getContextPath()}/resources/images/search.png"/></td>
                <td colspan="3"><input type="text" col="3"
                                       onkeyup="filter()" placeholder="Search for ID.."></td>
            </tr>
            </thead>
            <tbody id="siteTbody" class="table_body">
                <#if siteIds??>
                    <#list siteIds as siteId>
                    <tr siteId="${siteId}">
                        <td><input type="checkbox" class="deleteCheckBox"/></td>
                        <td class="text_center">
                            <a class="icon icon_edit"
                               href="${rc.getContextPath()}/site/showDetail?siteId=${siteId}"></a>
                        </td>
                        <td class="number">${siteId}</td>
                    </tr>
                    </#list>
                </#if>
            </tbody>
        </table>
    </div>
    <div class="board-split"></div>

</div>
</@standard.standardPage>
