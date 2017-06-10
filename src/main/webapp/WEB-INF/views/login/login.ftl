<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta charset="UTF-8">
    <title>Earth</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" media="screen"
          href="${rc.getContextPath()}/resources/css/bootstrap.min.css"/>
    <link rel="stylesheet" media="screen"
          href="${rc.getContextPath()}/resources/css/style.css"/>
    <link rel="stylesheet" media="screen"
          href="${rc.getContextPath()}/resources/css/style_nev.css"/>
    <link rel="stylesheet" media="screen"
          href="${rc.getContextPath()}/resources/css/sass_compiled/components.css"/>
    <link rel="shortcut icon" href="${rc.getContextPath()}/resources/images/favicon.ico" type="image/x-icon">
    <script src="${rc.getContextPath()}/resources/js/libs/jquery-2.2.4.min.js"></script>
    <script src="${rc.getContextPath()}/resources/js/lib/bootstrap.min.js"></script>
    <script src="${rc.getContextPath()}/resources/js/lib/jquery.query-object.js"></script>
    <script src="${rc.getContextPath()}/resources/js/lib/handlebars-v4.0.10.js"></script>
    <script src="${rc.getContextPath()}/resources/js/common.js"></script>
</head>
<body>
<form action="${rc.getContextPath()}/login" object="loginForm" method="post">

    <div class="board-wrapper div_login" style="width: 350px;height:310px;margin:  auto;padding-top: 15%">
        <div class="board board-half" style="padding: 20px 30px 30px 30px;">
            <table class="table_form_login" >
                <tr>
                    <td colspan="2" style="text-align: center;">
                        <img src="${rc.getContextPath()}/resources/images/global/logoLogin.png"
                             alt="xStra SERIES" />
                    </td>
                </tr>
                <tr>
                    <td colspan="2" class="text">
                        <input type="text" style=" height: 33px; width: 270px;" id="txtUser" name="userId">
                    </td>
                </tr>
                <tr>
                    <td colspan="2" class="text">
                        <input type="password" style=" height: 33px; width: 270px;" id="txtPassWord" name="password">
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <img src="${rc.getContextPath()}/resources/images/line.png"/>
                    <#include "../common/messages.ftl">
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <button type="submit" class="btn btn_login"
                            <span>${e.get('login.login')}</span>
                        </button>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</form>
</body>
</html>