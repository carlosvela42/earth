<#assign searchForm>
    <@component.searchFormPanel object="licenseHistory" formId="evidentForm"></@component.searchFormPanel>
</#assign>

<@standard.standardPage title=e.get('license.title')>
<div class="board-wrapper board-full">
${searchForm}
<div style="clear: both;"></div>
<#if message??>
   <div>
       <b style="color: red;">${message}</b>
   </div>
</#if>

<table class="clientSearch table_list licenseTable">
	<thead class="table_header">
		<tr>
			<td>${e.get('license.division')}</td>
			<td>${e.get('license.processTime')}</td>
			<td>${e.get('license.profileId')}</td>
			<td>${e.get('license.numOfAvailableLicense')}</td>
			<td>${e.get('license.numOfUsedLicense')}</td>
		</tr>
	</thead>
	<tbody class="table_body">
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
</@standard.standardPage>