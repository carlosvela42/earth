<@standard.standardPage title="EDIT PROCESS">
<script>
	var baseUrl;
	var processDefinition;
	var processId;
	window.onload = function () {
		baseUrl = "${rc.getContextPath()}/process/";
		processDefinition = "${process.processDefinition}";
		processId = getUrlParameter('processId');
		$('.documentDataSavePath').click(function(){
			if($(this).val()== "database"){
				$('#fileArea').hide();
				$('#databaseArea').show();
				documentDataSavePath = $(this).val();
			} else if($(this).val()== "file"){
				$('#fileArea').show();
				$('#databaseArea').hide();
				documentDataSavePath = $(this).val();
			}
		});
		$('#cancel').click(function(){
			document.location.href = baseUrl+"showList";
		});
		$('#desision').click(function(){
			var processForm = {};
			var strageFile = {};
			var strageDb = {};
			var process = {};
			process.processId = processId;
			process.processDefinition = processDefinition;
			process.processName = $('#processNameTxt').val();
			process.processVersion = $('#processVersionTxt').text();
			process.description = $('#description').val();
			process.documentDataSavePath = $(".documentDataSavePath:checked").val();
			if($(".documentDataSavePath:checked").val() == 'file'){
				strageFile.siteId = $('#siteId').val();
				strageFile.siteManagementType = $('.siteManagementMethod:checked').val();
			}else if($(".documentDataSavePath:checked").val() == 'database'){
				strageDb.schemaName= $('#schemaName').val();
				strageDb.dbUser = $('#dbUser').val();
				strageDb.dbPassword = $('#dbPassword').val();
				strageDb.owner = $('#owner').val();
				strageDb.dbServer = $('#dbServer').val();
			}
			if($('#fileUpload').val()){
				processForm.fileExtention = getFileExtension($('#fileUpload').val());
			}else{
				processForm.fileExtention = "xml";
			}
			processForm.workspaceId = $('#workspaceSelection').val();
			processForm.process = process;
			processForm.strageFile = strageFile;
			processForm.strageDb = strageDb;
			$.ajax({
			   url: baseUrl+"updateOne",
			   contentType:'application/json',
			   data: JSON.stringify(processForm),
			   dataType: 'json',
			   type: 'POST',
			   success: function(data) {
			   	if(data.message){
			   		alert(data.message);
			   	}else if(data.success){
			   		document.location.href = baseUrl+"showList?workspaceId="+$('#workspaceSelection').val();
			   	}
			   }
			});
		});
		$('#fileUpload').change(function (){
        	var oFReader = new FileReader();
	       	oFReader.readAsText($(this).get(0).files[0]);
	       	oFReader.onload = function (oFREvent) {
		       	console.log(this.result);
		       	processDefinition = this.result;
      	 	}
      	});
      	$('#fileDownload').click(function(){
      		document.location.href = baseUrl+"downloadFile";
      	});
	};
	function getFileExtension(filename){
		return filename.substr(filename.lastIndexOf('.')+1)
	};
	function getUrlParameter(sParam) {
    	var sPageURL = decodeURIComponent(window.location.search.substring(1)), 
    		sURLVariables = sPageURL.split('&'), sParameterName, i;
    	for (i = 0; i < sURLVariables.length; i++) {
        	sParameterName = sURLVariables[i].split('=');
			if (sParameterName[0] === sParam) {
            	return sParameterName[1] === undefined ? true : sParameterName[1];
        	}
    	}
	}
</script>
<div>
	<span>ワークスペース</span>
	<select id="workspaceSelection">
	<#if workspaces??>
        <#list workspaces as workspace>
          <option value="${workspace.workspaceId}">${workspace.workspaceName}</option>
        </#list>
    </#if>
</div>
<br>
<div><span>プロセス編集画面</span></div>
<br>
<div><input type="button" id="desision" value="決定" /><input type="button" id="cancel" value="キャンセル" /></div>
<table>

<tr>
	<td>Process name</td>
	<td><input type="text" id="processNameTxt" value="<#if process.processName??>${process.processName}</#if>"/></td>
</tr>
<tr>
	<td>Process version</td>
	<td><div id="processVersionTxt"><#if process.processVersion??>${process.processVersion}</#if></div></td>
</tr>
<tr>
	<td>Description</td>
	<td><input type="text" id="description" value="<#if process.description??>${process.description}</#if>"/></td>
</tr>
<tr>
	<td>Process defination</td>
	<td><input type="file" id="fileUpload" value="upload"/><input type="button" id="fileDownload" value="download"></td>
</tr>
<tr>
	<td>Document data save path</td>
	<td>
		<#if process.documentDataSavePath == "database">
			<input type="radio" name="documentDataSavePath" class="documentDataSavePath" value="database" checked>Database
  			<input type="radio" name="documentDataSavePath" class="documentDataSavePath" value="file">file
  		<#else>
  			<input type="radio" name="documentDataSavePath" class="documentDataSavePath" value="database">Database
  			<input type="radio" name="documentDataSavePath" class="documentDataSavePath" value="file" checked>file
  		</#if>
  	</td>
</tr>
</table>
<#if strageDb??>
<div id="databaseArea">
<div><span>Database</span></div>
<table>
<tr>
	<td>Schema name</td>
	<td><input type="text" id="schemaName" value="<#if strageDb.schemaName??>${strageDb.schemaName}</#if>"/></td>
</tr>
<tr>
	<td>DB user</td>
	<td><input type="text" id="dbUser" value="<#if strageDb.dbUser??>${strageDb.dbUser}</#if>"/></td>
</tr>
<tr>
	<td>DB passsword</td>
	<td><input type="text" id="dbPassword" value="<#if strageDb.dbPassword??>${strageDb.dbPassword}</#if>"/></td>
</tr>
<tr>
	<td>Owner</td>
	<td><input type="text" id="owner" value="<#if strageDb.owner??>${strageDb.owner}</#if>"/></td>
</tr>
<tr>
	<td>DB server</td>
	<td><input type="text" id="dbServer" value="<#if strageDb.dbServer??>${strageDb.dbServer}</#if>"/></td>
</tr>
</table>
</div>
<#else>
<div id="databaseArea"  style="display: none;">
<div><span>Database</span></div>
<table>
<tr>
	<td>Schema name</td>
	<td><input type="text" id="schemaName" /></td>
</tr>
<tr>
	<td>DB user</td>
	<td><input type="text" id="dbUser" /></td>
</tr>
<tr>
	<td>DB passsword</td>
	<td><input type="text" id="dbPassword" /></td>
</tr>
<tr>
	<td>Owner</td>
	<td><input type="text" id="owner" /></td>
</tr>
<tr>
	<td>DB server</td>
	<td><input type="text" id="dbServer" /></td>
</tr>
</table>
</div>
</#if>

<#if strageFile??>
<div id="fileArea">
<div><span>File</span></div>
<table>
<tr>
	<td>Site ID</td>
	<td>
		<select id="siteId">
			<#if siteIds??>
				<#list siteIds as siteId>
					<#if siteId == strageFile.siteId>
						<option value="${siteId}" selected>${siteId}</option>
					<#else>
						<option value="${siteId}">${siteId}</option>
					</#if>
				</#list>
			</#if>
		</select>
	</td>
</tr>
<tr>
	<td>Site management method</td>
	<td>
		<#if "fileUtilFull" == strageFile.siteManagementType>
			<input type="radio" name="siteManagementMethod" class="siteManagementMethod" value="fileUtilFull" checked>Use until full
  			<input type="radio" name="siteManagementMethod" class="siteManagementMethod" value="fileRoundRobin">Round robin
  		<#else>
  			<input type="radio" name="siteManagementMethod" class="siteManagementMethod" value="fileUtilFull">Use until full
  			<input type="radio" name="siteManagementMethod" class="siteManagementMethod" value="fileRoundRobin" checked>Round robin
  		</#if>
  	</td>
</tr>
</table>
</div>
<#else>
<div id="fileArea" style="display: none;">
<div><span>File</span></div>
<table>
<tr>
	<td>Site ID</td>
	<td>
		<select id="siteId">
			<#if siteIds??>
				<#list siteIds as siteId>
					<option value="${siteId}">${siteId}</option>
				</#list>
			</#if>
		</select>
	</td>

</tr>
<tr>
	<td>Site management method</td>
	<td>
		<input type="radio" name="siteManagementMethod" class="siteManagementMethod" value="fileUtilFull" checked>Use until full
  		<input type="radio" name="siteManagementMethod" class="siteManagementMethod" value="fileRoundRobin">Round robin
  	</td>
</tr>
</table>
</div>
</#if>

</@standard.standardPage>