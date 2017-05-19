<@standard.standardPage title="EDIT PROFILE">
<script>
    window.onload = function () {
        var countChecked = function () {
            var str = ""
            $('input[class=DeleteRow]:checked').each(function () {
                str += $(this).attr('value') + ",";
            });
            if (str.length > 0) {
                str = str.substring(0, str.length - 1);
            }
            $("#userIds").val(str);
            console.log(str);
        };
        $("input[class=DeleteRow]").on("click", countChecked);
    }
</script>
<form action="${rc.getContextPath()}/profile/updateOne" object="mgrUser" method="post" class="form-narrow
form-horizontal">
    <input type="hidden" id="userIds" name="strUserId" value="${strUserId!""}">
    <table style="text-align: left;">
        <tr style="height: 40px; text-align: center">
            <td><input type="submit" value="決定" class="button"></td>
            <td><a href="${rc.getContextPath()}/profile/showList" class="button">キャンセル</a></td>
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

        <#if mgrProfile??>
            <tr>
                <td>
                    <label>プロファイルID</label>
                </td>
                <td>
                    <input type="text" id="txtProfileId" name="profileId" height="20px" width="150px"
                           style="text-align: left;background: #EBEBE4;" value="${mgrProfile.profileId}" readonly>
                </td>
            </tr>
            <tr>
                <td>
                    <label>説明</label>
                </td>
                <td>
                    <input type="text" id="txtDescription" name="description" height="20px" width="150px"
                           style="text-align: left"
                           value="<#if mgrProfile.description??>${mgrProfile.description}</#if>">
                </td>
            </tr>
            <tr>
                <td>
                    <label>LDAP識別子</label>
                </td>
                <td>
                    <input type="text" id="txtLDAP" name="ldapIdentifier" height="20px" width="150px"
                           style="text-align: left"
                           value="<#if mgrProfile.ldapIdentifier??>${mgrProfile.ldapIdentifier}</#if>">
                </td>
            </tr>
            <tr>
                <td>
                    <label>ユーザ一覧</label>
                </td>
                <td>
                    <table border="1px solid black">
                        <tr>
                            <th colspan="2">ユーザID</th>
                            <th>名前</th>
                        </tr>
                        <#if mgrUsers??>
                            <#list mgrUsers as mgrUser>
                                <tr>
                                    <td>
                                        <input type="checkbox" id="delRow${mgrUser?index}" name="${mgrUser.userId}"
                                               value="${mgrUser.userId}"
                                               class="DeleteRow" ${userIds?seq_contains(mgrUser.userId)?string("checked","")
                                        }>
                                    </td>
                                    <td><#if mgrUser.userId??>${mgrUser.userId}</#if></td>
                                    <td><#if mgrUser.name??>${mgrUser.name}</#if></td>
                                </tr>
                            </#list>
                        </#if>
                    </table>
                </td>
            </tr>
        </#if>
    </table>
</form>
</@standard.standardPage>