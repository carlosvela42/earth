<@standard.standardPage title="MENU AUTHORITY">
<script type="text/javascript">
    function addParent() {
        var userAccessRightStr = "";
        $('#userAccessRightTable > tbody > tr').each(function () {
            userAccessRightStr += $(this).find("td").eq(1).html() + "|" + $(this).find("td").eq(2).html() + ",";
        });
        $('#userIdAccessRight').val(userAccessRightStr);

        var profileAccessRightStr = "";
        $('#profileAccessRightTable > tbody > tr').each(function () {
            profileAccessRightStr += $(this).find("td").eq(1).html() + "|" + $(this).find("td").eq(2).html() + ",";
        });

        $('#profileIdAccessRight').val(profileAccessRightStr);
        //$("#formId").submit()
    }

    function addChildRecord() {
        var choosenUserId = $("#userId").val();
        var choosenProfileId = $("#profileId").val();
        var choosenAccessRight = $("input[name='accessRight']:checked").data('name');
        if ((choosenUserId == null || choosenUserId == '') && (choosenProfileId == null || choosenProfileId == '')) {
            alert("Ban chua chon UserId hoac ProfileId");
        } else if (choosenUserId != '' && choosenProfileId != '') {
            alert("Ban chi duoc chon UserId hoac ProfileId");
        } else if (choosenUserId != '' || choosenProfileId != '') {
            if (choosenAccessRight != null) {
                if (choosenUserId != '') {
                    var userIdArr = new Array();
                    $('#userAccessRightTable > tbody > tr').each(function () {
                        userIdArr.push($(this).find("td").eq(1).html());
                    });

                    var newUserRow = "<tr><td><input type=\"checkbox\" name=\"userRight\"></td><td>" + choosenUserId + "</td><td>" + choosenAccessRight + "</td></tr>";

                    if (jQuery.inArray(choosenUserId, userIdArr) != -1) {
                        var index = jQuery.inArray(choosenUserId, userIdArr);
                        $('#userAccessRightTable > tbody > tr').eq(index).replaceWith(newUserRow);
                    } else {
                        $("#userAccessRightTable").append(newUserRow);
                    }
                    $("#userId").val('');
                    $("input[name='accessRight']:checked").prop('checked', false);
                } else if (choosenProfileId != '') {
                    var profileIdArr = new Array();
                    $('#profileAccessRightTable > tbody > tr').each(function () {
                        profileIdArr.push($(this).find("td").eq(1).html());
                    });

                    var newProfileRow = "<tr><td><input type=\"checkbox\" name=\"userRight\"></td><td>" + choosenProfileId + "</td><td>" + choosenAccessRight + "</td></tr>";

                    if (jQuery.inArray(choosenProfileId, profileIdArr) != -1) {
                        var index = jQuery.inArray(choosenProfileId, profileIdArr);
                        $('#profileAccessRightTable > tbody > tr').eq(index).replaceWith(newProfileRow);
                    } else {
                        $("#profileAccessRightTable").append(newProfileRow);
                    }

                    $("#profileId").val('');
                    $("input[name='accessRight']:checked").prop('checked', false);
                }
            } else {
                alert("Ban chua chon access right");
            }
        }
    }

    function deleteRecord() {
        $('input:checkbox:checked').each(function () {
            $(this).parent().parent().remove();
        });
    }
</script>
<label>メニュー権限設定</label>
</br>
<form method="post" action="${rc.getContextPath()}/menuAccessRight/updateMenuAccessRight" id="formId" object="mgrMenu">
    <input type="submit" onclick="addParent()" value="決定">
    <a id="cancelParent" href="${rc.getContextPath()}/menuAccessRight/menuList" class="button">キャンセル</a>
    <#if message??>
        <label>${message}</label>
    </#if>
    <table>
        <tr style="text-align: left;">
            <td>機能ID</td>
            <td>
                <input type="label" id="txtFunctionId" name="functionId" height="20px" width="150px"
                       style="text-align: left;background: #EBEBE4;" value="${mgrMenu.functionId!""}" readonly>
            </td>
        </tr>
        <tr style="text-align: left;">
            <td>機能名</td>
            <td>
                <input type="label" id="txtFunctionId" name="functionName" height="20px" width="150px"
                       style="text-align: left;background: #EBEBE4;" value="${mgrMenu.functionName!""}" readonly>
            </td>
        </tr>
        <tr style="text-align: left;">
            <td>機能分類ID</td>
            <td>
                <input type="label" id="txtFunctionId" name="functionCategoryId" height="20px" width="150px"
                       style="text-align: left;background: #EBEBE4;" value="${mgrMenu.functionCategoryId!""}" readonly>
            </td>
        </tr>
        <tr style="text-align: left;">
            <td>機能分類名</td>
            <td>
                <input type="label" id="txtFunctionId" name="functionCategoryName" height="20px" width="150px"
                       style="text-align: left;background: #EBEBE4;" value="${mgrMenu.functionCategoryName!""}" readonly>
            </td>
        </tr>
        <tr style="text-align: left;">
            <td>ユーザID</td>
            <td>
                <button type="button" onclick="deleteRecord()">削除</button>
                <table border="1" id="userAccessRightTable">
                    <thead>
                    <tr>
                        <th></th>
                        <th>ユーザID</th>
                        <th>アクセス権</th>
                    </tr>
                    </thead>
                    <tbody>
                        <#if userAccessRights??>
                            <#list userAccessRights as userAccessRight>
                            <tr id="${userAccessRight.userId}">
                                <td><input type="checkbox" name="userRight"></td>
                                <td>${userAccessRight.userId}</td>
                                <td>${userAccessRight.accessRight}</td>
                            </tr>
                            </#list>
                        </#if>
                    </tbody>
                </table>
                <input type="hidden" id="userIdAccessRight" name="userIdAccessRight">
            </td>
        </tr>
        <tr style="text-align: left;">
            <td>プロファイルID</td>
            <td>
                <button type="button" onclick="deleteRecord()">削除</button>
                <table border="1" id="profileAccessRightTable">
                    <thead>
                    <tr>
                        <th></th>
                        <th>プロファイルID</th>
                        <th>アクセス権</th>
                    </tr>
                    </thead>
                    <tbody>
                        <#if profileAccessRights??>
                            <#list profileAccessRights as profileAccessRight>
                            <tr>
                                <td><input type="checkbox" name="profileRight"></td>
                                <td>${profileAccessRight.profileId}</td>
                                <td>${profileAccessRight.accessRight}</td>
                            </tr>
                            </#list>
                        </#if>
                    </tbody>
                </table>
                <input type="hidden" id="profileIdAccessRight" name="profileIdAccessRight">
            </td>
        </tr>
    </table>
</form>
<div style="height: 20px;"></div>
<button id="addChild" onclick="addChildRecord()">決定</button>
<button id="cancelChild">キャンセル</button>
<table>
    <tr style="text-align: left;">
        <td>ユーザID</td>
        <td>
            <select id="userId">
                <option value="">--Select--</option>
                <#if mgrUsers??>
                    <#list mgrUsers as mgrUser>
                        <option value="${mgrUser.userId}">${mgrUser.userId}</option>
                    </#list>
                </#if>
            </select>
        </td>
    </tr>
    <tr style="text-align: left;">
        <td>プロファイルID</td>
        <td>
            <select id="profileId">
                <option value="">--Select--</option>
                <#if mgrProfiles??>
                    <#list mgrProfiles as mgrProfile>
                        <option value="${mgrProfile.profileId}">${mgrProfile.profileId}</option>
                    </#list>
                </#if>
            </select>
        </td>
    </tr>
    <tr style="text-align: left;">
        <td>アクセス権</td>
        <td>
            <#if accessRights??>
                <#list accessRights as accessRight>
                    <input type="radio" name="accessRight" data-name="${accessRight}"
                           value="${accessRight.value}">${accessRight}</label></br>
                </#list>
            </#if>
        </td>
    </tr>
</table>
</@standard.standardPage>