<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/XSL/Transform">
<head>
    <title text="{view.index.title}">Welcome!</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" media="screen" href="${rc.getContextPath()}/resources/css/bootstrap.min.css"/>
    <link rel="stylesheet" media="screen" href="${rc.getContextPath()}/resources/css/core.css"/>
    <script src="http://code.jquery.com/jquery-latest.js"></script>
    <script src="${rc.getContextPath()}/resources/js/bootstrap.min.js"></script>

</head>

<body>
<form class="form-narrow form-horizontal" method="get" action="${rc.getContextPath()}/index">
    <div>
        <p align="center" style="font-size: large;">BPM PROJECT</p>
    </div>

    <ul id="ul-System-managed" class="nav nav-second-level">
        <li>
            <a id="a-system-managed-TCKT" href="${rc.getContextPath()}/loadListWorkspace">WorkSpace<span
                    class="fa arrow"></span></a>
        </li>
        <li>
            <a id="a-system-managed-TCKT" href="${rc.getContextPath()}/imageviewer">Image Viewer New<span
                    class="fa arrow"></span></a>
        </li>
    </ul>
    </div>
    </div>
</form>
</body>
</html>