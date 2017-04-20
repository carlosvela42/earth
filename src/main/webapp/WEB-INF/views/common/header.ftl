<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" media="screen"
    href="${rc.getContextPath()}/resources/css/bootstrap.min.css" />
<link rel="stylesheet" media="screen"
    href="${rc.getContextPath()}/resources/css/core.css" />
<link rel="stylesheet" media="screen"
    href="${rc.getContextPath()}/resources/css/style.css" />
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="${rc.getContextPath()}/resources/js/bootstrap.min.js"></script>
<script src="${rc.getContextPath()}/resources/js/handlebars-v4.0.5.js"></script>

</head>
<body>
    <div style="float: left;">
        <a href="${rc.getContextPath()}/" class="button">Main Menu</a>
    </div>
    <div style="float: right;">
        <#if Session.userInfo??>
            <#assign userInfo =Session.userInfo>
            <label>${userInfo.userId}/${userInfo.userName}</label>
            <a href="${rc.getContextPath()}/logout" class="button">ログアウト</a>
        </#if>
</div>
</body>
</html>