<@standard.standardPage title="WORKSPACELIST">
<script>
    window.onload = function () {
        var countChecked = function () {
            var str = ""
            $('input[name=DeleteRow]:checked').each(function () {
                str += $(this).attr('value') + ",";
            });

            if (str.length > 0) {
                str = str.substring(0, str.length - 1);
            }
            $("#workspaceIds").val(str);
        };
        countChecked();
        var countDeleted = function () {
            var str = "0";
            $('input[name=deleteWorkspace]:checked').each(function () {
                str = "1";
            });
            $("#deleted").val(str);
        };

        $("input[name=DeleteRow]").on("click", countChecked);
        $("input[name=deleteWorkspace]").on("click", countDeleted);

    }
    function validate() {
        var deleted = parseInt($("#deleted").val());
        var str = $("#workspaceIds").val();
        console.log("workspaceIds: " + str);

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
</script>

<form object="mgrWorkspaces" method="post" action="${rc.getContextPath()}/workspace/deleteList">
 <div id="count"></div>
	<table >
		<tr>
			<td><a href="${rc.getContextPath()}/workspace/addNew" class="button">新規</a></td>
			<td><input type="submit" class="button" value="削除" onclick="validate()"></td>
			<td><input type="checkbox" name="deleteWorkspace" value="deleteWorkspace">Confirm Delete</td>
		</tr>
	</table>	
	 <div>
        <b id="message" style="color: red;"></b>
    </div>
	<input type="hidden" id="workspaceIds" name="workspaceIds" value="">
    <input type="hidden" id="deleted" name="deleted" value="0">
	    
	<table border="1">
		<tr>
			<th></th>
		    <th>ワークスペースID</th>
		    <th>ワークスペース名</th>
 	 	</tr>
 	 	<#if mgrWorkspaces??> 
			<#list mgrWorkspaces as mgrWorkspace>
			    <tr>
			    	<td><input type="checkbox" id="delRow${mgrWorkspace?index}" name="DeleteRow" value="${mgrWorkspace.workspaceId}"></td>
			        <td><a href="${rc.getContextPath()}/workspace/showDetail?workspaceId=${mgrWorkspace.workspaceId}">${mgrWorkspace.workspaceId}</a></td>
			        <td>${mgrWorkspace.workspaceName}</td>
			    </tr>
	    	</#list>
    	<#else> 
    		 <tr>
			    <td></td>
			    <td></td>
			  </tr>
		</#if>    
		
	</table>
</form>
</@standard.standardPage>