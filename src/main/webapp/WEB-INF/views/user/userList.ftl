<@standard.standardPage title="LIST USER">
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
            $("#userIds").val(str);
        };
        var countDeleted = function () {
            var str = "0";
            $('input[name=delete]:checked').each(function () {
                str = "1";
            });
            $("#deleted").val(str);
        };
        $("input[name=DeleteRow]").on("click", countChecked);
        $("input[name=delete]").on("click", countDeleted);
    }
    function validate() {
        var deleted = parseInt($("#deleted").val());
        var str = $("#userIds").val();
        console.log("userids: " + str);

        if (deleted > 0 && str.length > 0) {
            document.forms[0].submit();
        } else {
            if (deleted == 0 && str.length == 0) {
                $("#message").html("choose mgrUser and Confirm");
            } else if (deleted == 0 && str.length > 0) {
                $("#message").html("choose Confirm");
            } else if (deleted > 0 && str.length == 0) {
                $("#message").html("choose mgrUser");
            }
        }
    }
</script>
<form object="mgrUsers" method="post" style="text-align: left;" class="form-narrow form-horizontal"
      action="${rc.getContextPath()}/user/deleteList">
    <label>ユーザ一覧</label>
    <div id="count"></div>
    <table>
        <tr style="height: 40px;">
            <td style="padding-left: 20px;padding-right: 20px"><a href="${rc.getContextPath()}/user/addNew"
                                                                  class="button">新規</a></td>
            <td style="padding-left: 20px;padding-right: 20px">
                <input type="button" class="button" value="削除" onclick="validate()"></td>
            <td style="padding-left: 20px;padding-right: 20px"><input type="checkbox" name="delete" value="delete">Confirm
                Delete
            </td>
        </tr>
    </table>
    <div>
        <b id="message" style="color: red;"></b>
    </div>
    <input type="hidden" id="userIds" name="userIds" value="">
    <input type="hidden" id="deleted" name="deleted" value="0">
    <table border="1" style="text-align: left;">
        <tr>
            <th colspan="2">ユーザID</th>
            <th>名前</th>
        </tr>
        <tr style="height: 25px;">
            <td colspan="2"></td>
            <td></td>
        </tr>
        <#if mgrUsers??>
            <#list mgrUsers as mgrUser>
                <tr>
                    <td><input type="checkbox" id="delRow${mgrUser?index}" name="DeleteRow" value="${mgrUser.userId}"
                               class="DeleteRow"></td>
                    <td><a href="${rc.getContextPath()}/user/showDetail?userId=${mgrUser.userId}">${mgrUser.userId}</a></td>
                    <td>${mgrUser.name}</td>
                </tr>
            </#list>
        <#else>
            <tr>
                <td></td>
                <td></td>
                <td></td>
            </tr>
        </#if>

    </table>
</form>
</@standard.standardPage>