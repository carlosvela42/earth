<!DOCTYPE html>
<html >
<head>
    <title>Signup</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link href="../../../resources/css/bootstrap.min.css" rel="stylesheet" media="screen" href="@{/resources/css/bootstrap.min.css}"/>
    <link href="../../../resources/css/core.css" rel="stylesheet" media="screen" href="@{/resources/css/core.css}" />
    <script src="http://code.jquery.com/jquery-latest.js"></script>
    <script src="../../../resources/js/bootstrap.min.js" src="@{/resources/js/bootstrap.min.js}"></script>
</head>
<body>
<form class="form-narrow form-horizontal" method="post" action="/${rc.getContextPath()}/signup" object="$signupForm">
    <!--/* Show general error message when form contains errors */-->

        <legend>Please Sign Up</legend>
        <div class="form-group" >
            <label for="email" class="col-lg-2 control-label">Email</label>
            <div class="col-lg-10">
                <input type="text" class="form-control" id="email" placeholder="Email address" field="*{email}" />
            </div>
        </div>
        <div class="form-group" >
            <label for="password" class="col-lg-2 control-label">Password</label>
            <div class="col-lg-10">
                <input type="password" class="form-control" id="password" placeholder="Password" field="*{password}"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-lg-offset-2 col-lg-10">
                <button type="submit" class="btn btn-default">Sign up</button>
            </div>
        </div>
        <div class="form-group">
            <div class="col-lg-offset-2 col-lg-10">
                <p>Already have an account? <a href="signin" href="/signin">Sign In</a></p>
            </div>
        </div>
    </fieldset>
</form>
</body>
</html>