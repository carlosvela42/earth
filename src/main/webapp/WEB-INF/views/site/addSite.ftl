<#assign contentFooter>
    <@component.detailUpdatePanel object="site" formId="siteForm"></@component.detailUpdatePanel>
</#assign>

<#assign script>
<script src="${rc.getContextPath()}/resources/js/site.js"></script>
</#assign>

<@standard.standardPage title=e.get("site.edit") contentFooter=contentFooter script=script>
<br>    
    <#assign isPersisted = (siteForm.lastUpdateTime??)>
    <#assign formAction = isPersisted?then('updateOne', 'insertOne')>
    <#assign readonly = (formAction=='updateOne')?then('readonly',"")>
    
<form  id="siteForm" action="${rc.getContextPath()}/site/${formAction}" object="siteForm"
	method="post" class="">
	 <#include "../common/messages.ftl">  
	<input type="hidden" id="directoryIds" name="directoryIds">
	<div>
		<b id="message" style="color: red;"></b>
	</div>
 <div class="board-wrapper">
	 <div class="board board-half">
	   <table class="table_form">
		  <tr>		    
			<td width="100px"><label>${e.get("site.id")}</label></td>
			<td><input type="text" id="txtSiteId" name="siteId"
				value="${siteForm.siteId}" height="20px" width="150px"
				style="text-align: left" readonly="readonly"></td>
		  </tr>		
	   </table>
	<div><input type="hidden" name="lastUpdateTime" value="${siteForm.lastUpdateTime!""}"/></div>
	<br>
	   <table class="clientSearch table_list">
	   <thead>    
			<tr class="table_header">
			    <td class=""><input type="checkbox" class="deleteAllCheckBox" /></td>
				<td>${e.get("site.datadirectoryid")}</td>
				<td>${e.get("site.folderpath")}</td>
				<td>${e.get("site.createnew")}</td>
				<td>${e.get("site.secureddiskspace")}</td>
				<td>${e.get("site.diskcapacity")}</td>
			</tr>
		</thead>
		<tbody id="siteTbody" class="table_body">
		<#if siteForm??>
			<#if siteForm.directories??>
	                <#list siteForm.directories as directory>
	                <tr id="row${directory?index}">
	                     <td><input type="checkbox" class="deleteCheckBox" id="chooseRow${directory?index}" name="ChooseRow" value="${directory.dataDirectoryId}" ${directory.checked?string("checked","") } ></td>
	                     <td id="dataDirectoryId${directory?index}" value="${directory.dataDirectoryId}">${directory.dataDirectoryId}</td>
	                     <td id="folderPath${directory?index}" value="${directory.folderPath}">${directory.folderPath}</td>
	                     <td id="newCreateFile${directory?index}" value="${directory.newCreateFile}">${directory.newCreateFile}</td>
	                     <td id="reservedDiskVolSize${directory?index}" value="${directory.reservedDiskVolSize}">${directory.reservedDiskVolSize}</td>
	                     <td id="diskVolSize${directory?index}" value="${directory.diskVolSize}"->${directory.diskVolSize}</td>
	                </tr>
	                </#list>
	            </#if>
	      </#if>
	      </tbody>
        </table>		
    </div>
	</table>  
  </div>
</form>
</@standard.standardPage>
