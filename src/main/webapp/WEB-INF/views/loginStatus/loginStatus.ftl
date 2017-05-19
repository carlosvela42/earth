<@standard.standardPage title="LOGIN STATUS">
<form action="${rc.getContextPath()}/loginViewScreen" object="ctlLogins" method="get" class="form-narrow
form-horizontal">
    <label>証跡ログ</label>
    <table border="1" style="text-align: left;">
        <tr style="white-space: nowrap; text-align: center;">
            <th>ユーザID</th>
            <th>ログイン日時</th>
            <th>ログアウト日時</th>
            <th>セッションID</th>
        </tr>
        <#if ctlLogins??>
            <#list ctlLogins as ctlLogin>
                <tr style="white-space: nowrap; text-align: left;">
                    <td style="text-align: left;">${ctlLogin.userId!""}</td>
                    <td style="text-align: left;">${ctlLogin.loginTime!""}</td>
                    <td style="text-align: left;">${ctlLogin.logoutTime!""}</td>
                    <td style="text-align: left;">${ctlLogin.sessionId!""}</td>
                </tr>
            </#list>
        <#else>
            <tr>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
        </#if>
        <tr style="white-space: nowrap; text-align: center;">
            <td colspan="4"><input type="submit" value="検索"></td>
        </tr>
    </table>
</form>
</@standard.standardPage>