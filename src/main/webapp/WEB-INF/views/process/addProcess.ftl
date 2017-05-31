<#assign contentFooter>
    <@component.detailUpdatePanel object="process" formId="processForm"></@component.detailUpdatePanel>
</#assign>

<#assign script>
<script src="${rc.getContextPath()}/resources/js/process.js"></script>
</#assign>

<@standard.standardPage title=e.get("process.edit") contentFooter=contentFooter script=script>
<br>
    <#assign isPersisted = (process.processId??)>
    <#assign formAction = isPersisted?then('updateOne', 'insertOne')>

<form id="processForm" action="${rc.getContextPath()}/process/${formAction}" object="processForm" method="post" class="">
    <#include "../common/messages.ftl">
    <div class="board-wrapper">
        <div class="board board-half">
            <table class="table_form">
                <tr>
                    <td width="50%">${e.get('process.id')}</td>
                    <td>${process.processId!""}<input type="hidden" name="process.processId" value="${process.processId!""}"/></td>
                </tr>
                <tr>
                    <td width="50%">${e.get('process.name')}</td>
                    <td><input type="text" name="process.processName" value="${process.processName!""}"/></td>
                </tr>
                <tr>
                    <td>${e.get('process.version')}</td>
                    <td><input type="text" name="process.processVersion" value="${process.processVersion!""}"/></td>
                </tr>
                <tr>
                    <td>${e.get('process.description')}</td>
                    <td><input type="text" name="process.description" value="${process.description!""}"/></td>
                </tr>
                <tr>
                    <td>${e.get('process.definition')}</td>
                    <td><input type="file" id="fileUpload" value="upload"/><input type="button" id="fileDownload" value="download"></td>
                </tr>
            </table>
        </div>
        <div class="board-split"></div>
        <div class="board board-half">
            <#include "saveTarget.ftl">
        </div>
        <#--<div class="clearfix"></div>-->
    </div>
    <div><input type="hidden" name="workspaceId" value="${workspaceId!""}"/></div>

</form>
</@standard.standardPage>