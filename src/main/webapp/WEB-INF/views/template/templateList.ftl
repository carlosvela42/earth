<@standard.standardPage title="WORKSPACELIST">
<script>
	window.onload = function() {
		var countChecked = function() {
			var str = ""
			$('input[name=DeleteRow]:checked').each(function() {
				str += $(this).attr('value') + ",";
			});

			if (str.length > 0) {
				str = str.substring(0, str.length - 1);
			}
			$("#templateIds").val(str);
		};

		var countDeleted = function() {
			var str = "0";
			$('input[name=deleteTemplate]:checked').each(function() {
				str = "1";
			});
			$("#deleted").val(str);
		};

		$("input[name=DeleteRow]").on("click", countChecked);
		$("input[name=deleteTemplate]").on("click", countDeleted);
	}
	function validate() {
		var deleted = parseInt($("#deleted").val());
		var str = $("#templateIds").val();
		console.log("templateIds: " + str);

		if (deleted > 0 && str.length > 0) {
			document.forms[0].submit();
		} else {
			if (deleted == 0 && str.length == 0) {
				$("#message").html("choose user and Confirm");
			} else if (deleted == 0 && str.length > 0) {
				$("#message").html("choose Confirm");
			} else if (deleted > 0 && str.length == 0) {
				$("#message").html("choose user");
			}
		}
	}
	function filter() {
		// Declare variables
		var workspaceIdInput, workspaceNameInput, filter, i;
		templateIdInput = document.getElementById('templateIdInput').value
				.toUpperCase();
		templateNameInput = document.getElementById('templateNameInput').value
				.toUpperCase();
		templateTableNameInput = document
				.getElementById('templateTableNameInput').value.toUpperCase();

		// Loop through all list items, and hide those who don't match the search query
		for (i = 0; i < $("#templateTable tr ").length - 2; i++) {

			if (($("#templateId" + i).html().toUpperCase().indexOf(
					templateIdInput) > -1)
					&& ($("#templateName" + i).html().toUpperCase().indexOf(
							templateNameInput) > -1)
					&& ($("#templateTableName" + i).html().toUpperCase()
							.indexOf(templateTableNameInput) > -1)) {
				$("#row" + i).show();

			} else {
				$("#row" + i).hide();
			}
		}
	}
	$(function() {
		$("#workspaces").on('change', function() {
			$("#workspaceId").val($('#workspaces option:selected').val());
			var workspaceId = $('#workspaces option:selected').text();
			if (workspaceId != '-- Please choose --') {

				/* $('#btAdd').prop( "disabled", false );
				$('#btEdit').prop( "disabled", false );
				$('#btDelete').prop( "disabled", false ); */

				$("#mgrTemplateForm").attr("action", "switchWorkspace");
				$("#mgrTemplateForm").submit();
			} else {
				$('#btAdd').prop("disabled", true);
				$('#btEdit').prop("disabled", true);
				$('#btDelete').prop("disabled", true);
			}

		});
	})

	function showDetail() {
		var str = $("#templateIds").val();
		if (str.length == 1) {
			$("#mgrTemplateForm").attr("action", "showDetail");
			$("#mgrTemplateForm").submit();
		} else {
			$("#message").html("choose one template, please!");
		}
	}
</script>

<form object="mgrTemplates" method="post" id="mgrTemplateForm"
	action="${rc.getContextPath()}/template/deleteList">
	<table id="button">
		<tr>
			<td><a href="${rc.getContextPath()}/template/addNew" id="btAdd"
				class="button">新規</a></td>
			<td><input type="button" class="button" value="編集" id="btEdit"
				onclick="showDetail()"></td>
			<td><input type="button" class="button" value="削除" id="btDelete"
				onclick="validate()"></td>
			<td><input type="checkbox" name="deleteTemplate"
				value="deleteTemplate">Confirm Delete</td>
			<td><select name="workspaces" id="workspaces">
					<option selected="selected">-- Please choose --</option>
					<#if mgrWorkspaces??>
                        <#list mgrWorkspaces as mgrWorkspace>
                          <#if workspaceId??>
					             <#assign selected=(mgrWorkspace.workspaceId == workspaceId)?then("selected","")>
					        </#if>
					        <option value="${mgrWorkspace.workspaceId}"  ${selected!""}>${mgrWorkspace.workspaceName}</option>
                        </#list>
                   </#if>
                </select></td>
		</tr>
	</table>
	<br> <input type="hidden" id="templateIds" name="templateIds"
		value=""> <input type="hidden" id="deleted" name="deleted"
		value="0"> <input type="hidden" id="workspaceId"
		name="workspaceId" value="0">
	<div>
		<b id="message" style="color: red;"></b>
	</div>
	<table border="1" id="templateTable">
		<tr>
			<th colspan="2">テンプレートID</th>
			<th>テンプレート名</th>
			<th>テンプレートテーブル名</th>
		</tr>
		<tr>
			<td colspan="2"><input type="text" id="templateIdInput"
				onkeyup="filter()" placeholder="Search for ID.."></td>
			<td><input type="text" id="templateNameInput" onkeyup="filter()"
				placeholder="Search for name.."></td>
			<td><input type="text" id="templateTableNameInput"
				onkeyup="filter()" placeholder="Search for table names.."></td>
		</tr>
		<#if mgrTemplates??>
                <#list mgrTemplates as mgrTemplate>
                <tr id="row${mgrTemplate?index}">   
                     <td><input type="checkbox" id="delRow${mgrTemplate?index}"
                       name="DeleteRow" value="${mgrTemplate.templateId}"></td>
                        <td id="templateId${mgrTemplate?index}">${mgrTemplate.templateId}</td>
                        <td id="templateName${mgrTemplate?index}">${mgrTemplate.templateName}</td>
                        <td id="templateTableName${mgrTemplate?index}">${mgrTemplate.templateTableName}</td>
                </tr>
                </#list>
            </#if>
        </table>
	<#if messages??> <#list messages as message>
    <div>
        <b style="color: red;">${message.getContent()}</b>
    </div>
    </#list> </#if>
</form>
</@standard.standardPage>
