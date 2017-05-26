<@standard.standardPage title="SITELIST">
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
			$("#siteIds").val(str);
		};

		var countDeleted = function() {
			var str = "0";
			$('input[name=deleteSite]:checked').each(function() {
				str = "1";
			});
			$("#deleted").val(str);
		};

		$("input[name=DeleteRow]").on("click", countChecked);
		$("input[name=deleteSite]").on("click", countDeleted);
	}
	function validate() {
		var deleted = parseInt($("#deleted").val());
		var str = $("#siteIds").val();
		console.log("siteIds: " + str);

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
		var siteIdInput, i;
		siteIdInput = document.getElementById('siteIdInput').value
				.toUpperCase();
		// Loop through all list items, and hide those who don't match the search query
		for (i = 0; i < $("#sitesTable tr ").length - 2; i++) {

			if (($("#siteId" + i).html().toUpperCase().indexOf(siteIdInput) > -1)) {
				$("#row" + i).show();

			} else {
				$("#row" + i).hide();
			}
		}
	}
</script>
<form method="post" id="sitesForm"
	action="${rc.getContextPath()}/site/deleteList">
	<input type="hidden" id="siteIds" name="siteIds" value=""> <input
		type="hidden" id="deleted" name="deleted" value="0">
	<div>
		<b id="message" style="color: red;"></b>
	</div>
	<table border="1" id="sitesTable">
		<tr>
			<th><input type="checkbox" name="deleteSites"
				value="deleteSites"></th>
			<th><a href="${rc.getContextPath()}/site/addNew" class="button">新規</a></th>
			<th>Site_ID</th>
		</tr>
		<tr>
			<td colspan="3"><input type="text" id="siteIdInput"
				onkeyup="filter()" placeholder="Search for ID.."></td>
		</tr>
		<#if siteIds??>
                <#list siteIds as siteId>
                <tr id="row${siteId?index}">
                     <td><input type="checkbox" id="delRow${siteId?index}"name="DeleteRow" value="${siteId}"></td>
                     <td><a href="${rc.getContextPath()}/site/showDetail?siteId=${siteId}"  class="button">編集</a></td>
                     <td id="siteId${siteId?index}">${siteId}</td>
                </tr>
                </#list>
            </#if>
        </table>
	<br>
	<table id="button">
		<tr>
			<td><input type="button" class="button" value="削除"
				onclick="validate()"></td>
			<td><input type="checkbox" name="deleteSite" value="deleteSite">Confirm
				Delete</td>
		</tr>
	</table>
</form>
</@standard.standardPage>
