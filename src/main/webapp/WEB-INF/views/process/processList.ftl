<@standard.standardPage title="PROCESS LIST">
<script>
	var baseUrl;
	window.onload = function () {
		baseUrl = "${rc.getContextPath()}/process/";
		$("#workspaceSelection").change(function(){
			document.location.href = baseUrl+"showList?workspaceId="+$(this).val();
		});
		$('#deleteButton').click(function(){
			var deleteProcessForm = {};
			var processIds = [];
			$('#processTbody > tr').each(function() {
				if($(this).find('.deleteCheckBox').prop('checked')){
					processIds.push(Number($(this).attr('processId')));
				}
			});
			deleteProcessForm.processIds = processIds;
			deleteProcessForm.confirmDelete = $('#deleteConfirm').prop('checked');
			deleteProcessForm.workspaceId = $('#workspaceSelection').val();
			$.ajax({
			   url: baseUrl+"deleteList",
			   contentType:'application/json',
			   data: JSON.stringify(deleteProcessForm),
			   dataType: 'json',
			   type: 'POST',
			   success: function(data) {
			   	if(data.message){
			   		alert(data.message);
			   	}else if(data.result){
			   		document.location.href = baseUrl+"showList?workspaceId="+$(this).val();
			   	}
			   }
			});
		});
		$('.searchTxt').keyup(function(){
			var array = [];
			$('.searchTxt').each(function(){
				if($(this).val()){
					var searchTxt = $(this).val();
					var col = $(this).attr('col');
					var count = 0;
					$('#processTbody > tr').each(function() {
						if(!searchTxt || $(this).find('td:nth-child('+col+')').text().indexOf(searchTxt)>=0){
							if(!array[count]){
								array[count] = 1;
								$(this).show();
							}else{
								if(array[count] === 1){
									$(this).show();
								}else{
									$(this).hide();
								}
							}	
						}else{
							array[count] = 2;
							$(this).hide();
						}
						count++;
					});
				}
			});
			if(array.length === 0){
				$('#processTbody > tr').each(function() {
					$(this).show();
				});
			}
			
		});
	};
</script>
<div>
	<span>ワークスペース</span>
	<select id="workspaceSelection">
	<#if workspaces??>
        <#list workspaces as workspace>
          <option value="${workspace.workspaceName}">${workspace.workspaceName}</option>
        </#list>
    </#if>
</div>
<div><span>プロセス一覧</span></div>
<div><input type="button" value="新規" /><input type="button" value="編集" /><input type="button" id="deleteButton" value="削除" /><input type="checkbox" id="deleteConfirm"/><span>削除確認</span>

<table>
	<thead>
		<tr>
			<td colspan="2">Process ID</td>
			<td>Process name</td>															
			<td>Process version	</td>													
			<td>Description</td>													
			<td>Document data save path</td>														
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
					<td>${process.processId}</td>
					<td>${process.processName}</td>
					<td>${process.processVersion}</td>
					<td>${process.description}</td>
					<td>${process.documentDataSavePath}</td>
				</tr>
			 </#list>
		</#if>
	</tbody>
</table>
</@standard.standardPage>