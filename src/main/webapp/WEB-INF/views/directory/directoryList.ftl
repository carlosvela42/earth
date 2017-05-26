<@standard.standardPage title="DIRECTORYLIST">
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
            $("#dataDirectoryIds").val(str);
        };

        var countDeleted = function() {
            var str = "0";
            $('input[name=deleteDirectorys]:checked').each(function() {
                str = "1";
            });
            $("#deleted").val(str);
        };

        $("input[name=DeleteRow]").on("click", countChecked);
        $("input[name=deleteDirectorys]").on("click", countDeleted);
    }
    function validate() {
        var deleted = parseInt($("#deleted").val());
        var str = $("#dataDirectoryIds").val();
        console.log("dataDirectoryIds: " + str);

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
        var directoryIdInput, i;
        directoryIdInput = document.getElementById('directoryIdInput').value.toUpperCase();
        // Loop through all list items, and hide those who don't match the search query
        for (i = 0; i < $("#directorysTable tr ").length - 2; i++) {

            if (($("#ataDirectoryId" + i).html().toUpperCase().indexOf(siteIdInput) > -1)) {
                $("#row" + i).show();

            } else {
                $("#row" + i).hide();
            }
        }
      }
</script>
<form method="post" id="directoryForm"
    action="${rc.getContextPath()}/directory/deleteList">
    <input type="hidden" id="dataDirectoryIds" name="dataDirectoryIds" value=""> 
    <input type="hidden" id="deleted" name="deleted" value="0">
    <div>
        <b id="message" style="color: red;"></b>
    </div>
    <table border="1" id="directorysTable">
        <tr>
            <th><input type="checkbox" name="deleteDirectorys"
                value="deleteDirectorys"></th>
            <th><a href="${rc.getContextPath()}/directory/addNew" class="button">新規</a></th>
            <th>Directory_ID</th>
            <th>Folder path</th>
            <th>Create new file</th>
            <th>Secured disk space [MB]</th>
            <th>Disk space [MB]</th>
        </tr>
        <tr>
            <td colspan="3"><input type="text" id="directoryIdInput"
                onkeyup="filter()" placeholder="Search for ID.."></td>
        </tr>
        <#if directorys??>
                <#list directorys as directory>
                <tr id="row${directory?index}">
                     <td><input type="checkbox" id="delRow${directory?index}"name="DeleteRow" value="${directory.dataDirectoryId!""}"></td>
                     <td><a href="${rc.getContextPath()}/directory/showDetail?dataDirectoryId=${directory.dataDirectoryId}"  class="button">編集</a></td>
                     <td id="dataDirectoryId${directory?index}">${directory.dataDirectoryId}</td>
                     <td id="dataDirectoryId${directory?index}">${directory.folderPath}</td>
                     <td id="dataDirectoryId${directory?index}">${directory.newCreateFile}</td>
                     <td id="dataDirectoryId${directory?index}">${directory.reservedDiskVolSize}</td>
                     <td id="dataDirectoryId${directory?index}">${directory.diskVolSize}</td>
                </tr>
                </#list>
            </#if>
        </table>
    <br>
    <table id="button">
        <tr>
            <td><input type="button" class="button" value="削除"onclick="validate()"></td>
            <td><input type="checkbox" name="deleteDirectorys" value="deleteDirectorys">ConfirmDelete</td>
        </tr>
    </table>
</form>
</@standard.standardPage>