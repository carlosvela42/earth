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
            $("#workspaceIds").val(str);
        };
        countChecked();
        var countDeleted = function() {
            var str = "0";
            $('input[name=deleteWorkspace]:checked').each(function() {
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
    function filter() {
        // Declare variables
        var workspaceIdInput, workspaceNameInput, filter, i;
        workspaceIdInput = document.getElementById('workspaceIdInput').value
                .toUpperCase();
        workspaceNameInput = document.getElementById('workspaceNameInput').value
                .toUpperCase();

        // Loop through all list items, and hide those who don't match the search query
        for (i = 0; i < $("#workspaceList tr ").length - 2; i++) {

            if (($("#workspaceId" + i).html().toUpperCase()
                    .indexOf(workspaceIdInput) > -1)
                    && ($("#workspaceName" + i).html().toUpperCase().indexOf(
                            workspaceNameInput) > -1)) {
                $("#row" + i).show();

            } else {
                $("#row" + i).hide();
            }
        }
    }
</script>

<form object="mgrWorkspaces" method="post"
    action="${rc.getContextPath()}/workspace/deleteList">
    <div id="count"></div>
    <table id="button">
        <tr>
            <td><a href="${rc.getContextPath()}/workspace/addNew"
                class="button">新規</a></td>
            <td><input type="button" class="button" value="削除"
                onclick="validate()"></td>
            <td><input type="checkbox" name="deleteWorkspace"
                value="deleteWorkspace">Confirm Delete</td>
        </tr>
    </table>
    <div>
        <b id="message" style="color: red;"></b>
    </div>
    <input type="hidden" id="workspaceIds" name="workspaceIds" >
    <input type="hidden" id="deleted" name="deleted" value="0"> 
    <table border="1" id="workspaceList">
        <tr>
            <th></th>
            <th>ワークスペースID</th>
            <th>ワークスペース名</th>
        </tr>
        <tr>
            <td></td>
            <td><input
                type="text" id="workspaceIdInput" onkeyup="filter()"
                 placeholder="Search for ID.."> </td>
            <td><input type="text"id="workspaceNameInput" onkeyup="filter()" 
                placeholder="Search for names.."></td>
        </tr>
        <#if mgrWorkspaces??> 
            <#list mgrWorkspaces as mgrWorkspace>
		        <tr id="row${mgrWorkspace?index}">
		            <td><input type="checkbox" id="delRow${mgrWorkspace?index}"
		               name="DeleteRow" value="${mgrWorkspace.workspaceId}"></td>
		            <td><a id="workspaceId${mgrWorkspace?index}"
		                href="${rc.getContextPath()}/workspace/showDetail?workspaceId=${mgrWorkspace.workspaceId}">${mgrWorkspace.workspaceId}</a></td>
		            <td id="workspaceName${mgrWorkspace?index}">${mgrWorkspace.workspaceName}</td>
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
