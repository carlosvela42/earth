<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>LOGIN</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" media="screen"
	href="${rc.getContextPath()}/resources/css/bootstrap.min.css" />
<link rel="stylesheet" media="screen"
	href="${rc.getContextPath()}/resources/css/core.css" />
<link rel="stylesheet" media="screen"
	href="${rc.getContextPath()}/resources/css/style.css" />
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="${rc.getContextPath()}/resources/js/bootstrap.min.js"></script>
</head>
<body>
	<form action="${rc.getContextPath()}/login" object="mgrUser"
		method="post">
		<div class="div_page">
			<table>
				<tr>

					<td colspan="2" style="text-align: center;"><label>ログイン</label>
					</td>
				</tr>
				<tr>
					<td><label>ユーザID：</label></td>
					<td><input type="text" id="txtUser" name="userId"
						height="20px" width="150px" style="text-align: left"></td>
				</tr>
				<tr>
					<td><label>パスワード： </label></td>
					<td><input type="password" id="txtPassWord" name="password"
						height="20px" width="150px" style="text-align: left"></td>
				</tr>
				<tr>
					<td colspan="2"><#if messages??> <#list messages as message>
						<div>
							<b style="color: red;">${message.getContent()}</b>
						</div> </#list> </#if>
					</td>
				</tr>
				<tr>
					<td></td>
					<td><input type="submit" value="ログイン"></td>
				</tr>
			</table>
		</div>
	</form>
</body>
</html>