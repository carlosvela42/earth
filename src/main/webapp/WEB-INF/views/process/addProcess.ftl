<#assign contentFooter>
    <@component.detailUpdatePanel object="process" formId="processForm"></@component.detailUpdatePanel>
</#assign>

<@standard.standardPage title="EDIT PROCESS" contentFooter=contentFooter>
<div><span>プロセス編集画面</span></div>
<br>
    <#assign isPersisted = (process.processId??)>
    <#assign formAction = isPersisted?then('updateOne', 'insertOne')>
<form id="processForm" action="${rc.getContextPath()}/process/${formAction}" object="processForm" method="post" class="">
    <#include "../common/messages.ftl">

    <table>
        <tr>
            <td>Process name</td>
            <td><input type="text" name="process.processName" value="${process.processName!""}"/></td>
        </tr>
        <tr>
            <td>Process version</td>
            <td><input type="text" name="process.processVersion" value="${process.processVersion!""}"/></td>
        </tr>
        <tr>
            <td>Description</td>
            <td><input type="text" name="process.description" value="${process.description!""}"/></td>
        </tr>
        <tr>
            <td>Process defination</td>
            <td><input type="file" id="fileUpload" value="upload"/><input type="button" id="fileDownload" value="download"></td>
        </tr>
        <tr>
            <td>Document data save path</td>
            <td>
                <#if process.documentDataSavePath == "database">
                    <input type="radio" name="process.documentDataSavePath" class="documentDataSavePath"
                           value="database" checked>Database
                    <input type="radio" name="process.documentDataSavePath" class="documentDataSavePath" value="file">file
                <#else>
                    <input type="radio" name="process.documentDataSavePath" class="documentDataSavePath"
                           value="database">Database
                    <input type="radio" name="process.documentDataSavePath" class="documentDataSavePath" value="file"
                           checked>file
                </#if>
            </td>
        </tr>
    </table>
    <td><input type="hidden" name="process.processId" value="${process.processId!""}"/></td>
    <td><input type="hidden" name="workspaceId" value="${workspaceId!""}"/></td>
    <#include "saveTarget.ftl">

</form>
</@standard.standardPage>