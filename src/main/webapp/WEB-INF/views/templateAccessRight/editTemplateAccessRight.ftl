<script type="text/javascript" src="../resources/js/lib/jquery.min.js"></script>
<script type="text/javascript">

/* $("#addParent").click(function() {https://code.jquery.com/jquery-3.2.1.min.js
	alert("1");
	  var userAccessRightStr = "";
	  $('#userAccessRightTable > tbody > tr').each(function() {
		  userAccessRightStr += $(this).find("td").eq(1).html() + "|" + $(this).find("td").eq(2).html() + ",";  
	});
	  $('#userIdAccessRight').val(userAccessRightStr);
	  
	  var profileAccessRightStr = "";
	  $('#profileAccessRightTable > tbody > tr').each(function(){
		  profileAccessRightStr += $(this).find("td").eq(1).html() + "|" + $(this).find("td").eq(2).html() + ",";
	  });
	  
	  $('#profileIdAccessRight').val(profileAccessRightStr);
	}); */

function addParent(){
    var userAccessRightStr = "";
    $('#userAccessRightTable > tbody > tr').each(function() {
        userAccessRightStr += $(this).find("td").eq(1).html() + "|" + $(this).find("td").eq(2).html() + ",";  
  });
    $('#userIdAccessRight').val(userAccessRightStr);
    
    var profileAccessRightStr = "";
    $('#profileAccessRightTable > tbody > tr').each(function(){
        profileAccessRightStr += $(this).find("td").eq(1).html() + "|" + $(this).find("td").eq(2).html() + ",";
    });
    
    $('#profileIdAccessRight').val(profileAccessRightStr);
    //$("#formId").submit()
}

function addChildRecord(){
	var choosenUserId = $("#userId").val();
	var choosenProfileId = $("#profileId").val();
	var choosenAccessRight = $("input[name='accessRight']:checked").data('name');
	if((choosenUserId == null || choosenUserId == '') && (choosenProfileId == null || choosenProfileId == '')){
		alert("Ban chua chon UserId hoac ProfileId");
	} else if(choosenUserId != '' && choosenProfileId != ''){
		alert("Ban chi duoc chon UserId hoac ProfileId");
	} else if (choosenUserId != '' || choosenProfileId != ''){
		if(choosenAccessRight != null){
			if(choosenUserId != ''){
				var userIdArr = new Array();
				$('#userAccessRightTable > tbody > tr').each(function(){
					userIdArr.push($(this).find("td").eq(1).html());
				});
				
				var newUserRow = "<tr><td><input type=\"checkbox\" name=\"userRight\"></td><td>" + choosenUserId + "</td><td>" + choosenAccessRight + "</td></tr>";
				
				if(jQuery.inArray(choosenUserId, userIdArr) != -1){
				    var index = jQuery.inArray(choosenUserId, userIdArr);
				    $('#userAccessRightTable > tbody > tr').eq(index).replaceWith(newUserRow);
				} else{
					$("#userAccessRightTable").append(newUserRow);
				}
				$("#userId").val('');
				$("input[name='accessRight']:checked").prop('checked', false);
			}else if(choosenProfileId != ''){
				var profileIdArr = new Array();
				$('#profileAccessRightTable > tbody > tr').each(function(){
					profileIdArr.push($(this).find("td").eq(1).html());
                });
                
                var newProfileRow = "<tr><td><input type=\"checkbox\" name=\"userRight\"></td><td>" + choosenProfileId + "</td><td>" + choosenAccessRight + "</td></tr>";
                
                if(jQuery.inArray(choosenProfileId, profileIdArr) != -1){
                    var index = jQuery.inArray(choosenProfileId, profileIdArr);
                    $('#profileAccessRightTable > tbody > tr').eq(index).replaceWith(newProfileRow);
                } else{
                    $("#profileAccessRightTable").append(newProfileRow);
                }
				
				$("#profileId").val('');
				$("input[name='accessRight']:checked").prop('checked', false);
			}
		} else{
			alert("Ban chua chon access right");
		}
	}
}

function deleteRecord(){
	$('input:checkbox:checked').each(function(){
		$(this).parent().parent().remove();
	});
}
</script>
<label>テンプレート権限設定</label>
</br>
<form method="post" action="${rc.getContextPath()}/templateAccessRight/updateTemplateAccessRight" id="formId">
<input type="submit" onclick="addParent()" value="決定"> <button id="cancelParent">キャンセル</button>
<input type="hidden" id="templateId" name="templateId" value="${templateId}">
<input type="hidden" id="workspaceId" name="workspaceId" value="${workspaceId}">
<#if message??>
<label>${message}</label>
</#if>
<table>
    <tr>
        <td>テンプレートID</td>
        <td>
            <#if mgrTemplate??>
                ${mgrTemplate.templateId}
            </#if>
        </td>
    </tr>
    <tr>
        <td>テンプレート名</td>
        <td>
            <#if mgrTemplate??>
                ${mgrTemplate.templateName}
            </#if>
        </td>
    </tr>
    <tr>
        <td>ユーザID</td>
        <td>
            <button type="button" onclick="deleteRecord()">削除</button>
            <table border="1" id="userAccessRightTable" >
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
            <!-- <input type="hidden" id="userIdAccessRight" name="userIdAccessRight" value=""> -->
            <input type="hidden" id="userIdAccessRight" name="userIdAccessRight">
        </td>
        
        <tr>
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
            <!-- <input type="hidden" id="profileIdAccessRight" name="profileIdAccessRight" value=""> -->
            <input type="hidden" id="profileIdAccessRight" name="profileIdAccessRight">
        </td>
    </tr>
</table>
</form>
<button id="addChild" onclick="addChildRecord()">決定</button><button id="cancelChild">キャンセル</button>
<table>
    <tr>
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
    <tr>
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
    <tr>
        <td>アクセス権</td>
        <td>
            <#if accessRights??>
                <#list accessRights as accessRight>
                    <input type="radio" name="accessRight" data-name="${accessRight}" value="${accessRight.value}">${accessRight}</label></br>
                </#list>
            </#if>
        </td>
    </tr>
</table>
