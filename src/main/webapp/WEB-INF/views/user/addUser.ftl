<@standard.standardPage title="ADD USER">
<script>
    window.onload = function () {
        $("input[name=changePassword]").change(function () {
            console.log(this.checked);
            $('input[type=password]').prop("disabled", !this.checked);
        });
    }
</script>
<form action="${rc.getContextPath()}/user/insertOne" object="mgrUser" method="post" class="form-narrow form-horizontal">
    <table style="text-align: left;">
        <tr style="height: 40px; text-align: center">
            <td><input type="submit" value="決定" class="button"></td>
            <td><a href="${rc.getContextPath()}/user/showList" class="button">キャンセル</a></td>
        </tr>
        <tr>
            <td colspan="2">
                <#if messages??>
                    <#list messages as message>
                        <div>
                            <b style="color: red;">${message.getContent()}</b>
                        </div>
                    </#list>
                </#if>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <#if messageError??>
                    <div>
                    <b style="color: red;">${rc.getMessage("${messageError}")}</b>
                    </div>
                </#if>
            </td>
        </tr>
        <#if mgrUser??>
            <tr>
                <td>
                    <label>ユーザID</label>
                </td>
                <td>
                    <input type="text" id="txtUserID" name="userId" height="20px" width="150px"
                           style="text-align: left" value="${mgrUser.userId}">
                </td>
            </tr>
            <tr>
                <td>
                    <label>名前</label>
                </td>
                <td>
                    <input type="text" id="txtName" name="name" height="20px" width="150px" style="text-align: left"
                           value="${mgrUser.name}">
                </td>
            </tr>
            <tr>
                <td>
                    <label>パスワード変更</label>
                </td>
                <td>
                    <input type="checkbox" name="changePassword">
                </td>
            </tr>
            <tr>
                <td>
                    <label>新しいパスワード</label>
                </td>
                <td>
                    <input type="password" id="txtPassword" name="password" height="20px" width="150px"
                           style="text-align: left" disabled="disabled"">
                </td>
            </tr>
            <tr>
                <td>
                    <label>新しいパスワード（確認用) </label>
                </td>
                <td>
                    <input type="password" id="txtComfirmpassword" name="confirmPassword" height="20px" width="150px"
                           style="text-align: left" disabled="disabled" >
                </td>
            </tr>
        <#else >
            <tr>
                <td>
                    <label>ユーザID</label>
                </td>
                <td>
                    <input type="text" id="txtUserID" name="userId" height="20px" width="150px"
                           style="text-align: left">
                </td>
            </tr>
            <tr>
                <td>
                    <label>名前</label>
                </td>
                <td>
                    <input type="text" id="txtName" name="name" height="20px" width="150px" style="text-align: left">
                </td>
            </tr>
            <tr>
                <td>
                    <label>パスワード変更</label>
                </td>
                <td>
                    <input type="checkbox" name="changePassword">
                </td>
            </tr>
            <tr>
                <td>
                    <label>新しいパスワード</label>
                </td>
                <td>
                    <input type="password" id="txtPassword" name="password" height="20px" width="150px"
                           style="text-align: left" disabled="disabled">
                </td>
            </tr>
            <tr>
                <td>
                    <label>新しいパスワード（確認用) </label>
                </td>
                <td>
                    <input type="password" id="txtComfirmpassword" name="confirmPassword" height="20px" width="150px"
                           style="text-align: left" disabled="disabled">
                </td>
            </tr>
        </#if>

    </table>
</form>
</@standard.standardPage>