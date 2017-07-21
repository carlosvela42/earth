<#assign contentFooter>
    <@component.detailUpdatePanel object="directory" formId="directoryForm"></@component.detailUpdatePanel>
</#assign>
<#assign script>
<script src="${rc.getContextPath()}/resources/js/directory.js"></script>
</#assign>
<@standard.standardPage title=e.get("directory.add") contentFooter=contentFooter script=script imageLink="process">
<br>
    <#assign isPersisted = (directoryForm.lastUpdateTime??)>
    <#assign formAction = isPersisted?then('updateOne', 'insertOne')>

<form id="directoryForm" action="${rc.getContextPath()}/directory/${formAction}" object="directoryForm" method="post"
      class="" >
    <#include "../common/messages.ftl">
    <div class="board-wrapper">
        <div class="board board-half">
            <table class="table_form">
                <tr>
                    <td width="50%">${e.get('directory.id')}</td>
                    <td>${directoryForm.dataDirectoryId!""}<input type="hidden" name="dataDirectoryId"
                                                           value="${directoryForm.dataDirectoryId!""}"/></td>
                </tr>
                <tr>
                    <td width="50%">${e.get('create.new.file')}</td>
                    <td>
	                <#if directoryForm.newCreateFile??>
	                    <#if directoryForm.newCreateFile=="1">
	                        <#assign enable="checked">
	                    </#if>
	
	                    <#if directoryForm.newCreateFile=="0">
	                        <#assign disable="checked">
	                    </#if>
	                </#if>
	                <input type="radio" name="enable" id='enable' value="1" ${enable!""} > <label class="permision">${e.get('give.permission')}</label>
	                <br /><br />
	                <input type="radio" name="disable" id='disable' value="0" ${disable!""} > <label class="permision">${e.get('not.allow')}</label>
	                <input type="hidden" id="newCreateFile" name="newCreateFile" height="20px" width="150px"
	                       style="text-align: left" value="${directoryForm.newCreateFile!""}">
	            </td>
                </tr>
                <tr>
                    <td>${e.get('secured.disk.space')}</td>
                    <td><input type="text" name="reservedDiskVolSize" value="${directoryForm.reservedDiskVolSize!""}" /></td>
                </tr>
                <tr>
                    <td>${e.get('disk.space')}</td>
                    <td><input type="text" name="diskVolSize" value="${directoryForm.diskVolSize!""}" readonly="readonly" id="txtDiskVolSize" /></td>
                </tr>
                <tr>
                    <td>${e.get('folder.path')}</td>
                    <td><input type="text" name="folderPath" value="${directoryForm.folderPath!""}" id="txtFolderPath" /></td>
                </tr>
            </table>
            <input type="hidden" name="lastUpdateTime" value="${directoryForm.lastUpdateTime!""}"/>
        </div>
        <div class="board-split"></div>
    </div>
</form>
</@standard.standardPage>