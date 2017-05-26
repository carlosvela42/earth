<#assign contentFooter>
<@component.removePanel></@component.removePanel>
</#assign>

<@standard.standardPage title="PROCESS LIST" contentFooter=contentFooter displayWorkspace=true>
<script src="${rc.getContextPath()}/resources/js/process.js"></script>
<div><span><@spring.message code='process.list'/></span></div>
<div>
		<a id="addButton" href="${rc.getContextPath()}/process/addNew"><@spring.message code='button.new'/></a>
</div>
<form method="get" class="filter" action="">
		<input type="text" name="workspaceId" value="${workspaceId}">
</form>
<table>
	<thead>
		<tr>
			<td colspan="2"><@spring.message code='process.id'/></td>
			<td><@spring.message code='process.name'/></td>
			<td><@spring.message code='process.version'/></td>
			<td><@spring.message code='process.description'/></td>
			<td><@spring.message code='process.savingType'/></td>
		</tr>
		<tr>
			<td colspan="2"><input type="text" class="searchTxt" col="2" placeholder="Search process ID"/></td>
			<td><input type="text" class="searchTxt" col="3" placeholder="Search process name"/></td>															
			<td><input type="text" class="searchTxt" col="4" placeholder="Search process version"/></td>													
			<td><input type="text" class="searchTxt" col="5" placeholder="Search description"/></td>													
			<td><input type="text" class="searchTxt" col="6" placeholder="Search document data save path"/></td>					
		</tr>
	</thead>
	<tbody id="processTbody">
		<#if processes??>
			 <#list processes as process>
			 	<tr processId="${process.processId}">
				 	<td><input type="checkbox" class="deleteCheckBox" /></td>
					<td><a href="${rc.getContextPath()}/process/showDetail?processId=${process.processId}">${process
					.processId}</a> </td>
					<td><#if process.processName??>${process.processName}</#if></td>
					<td><#if process.processVersion??>${process.processVersion}</#if></td>
					<td><#if process.description??>${process.description}</#if></td>
					<td><#if process.documentDataSavePath??>${process.documentDataSavePath}</#if></td>
				</tr>
			 </#list>
		</#if>
	</tbody>
</table>
</@standard.standardPage>