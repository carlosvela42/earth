<@standard.standardPage title="EVIDENT LOG">

<script type="text/javascript" src="../resources/js/jquery.min.js"></script>
<script type="text/javascript"
        src="../resources/js/handlebars-v4.0.5.js"></script>

<form action="${rc.getContextPath()}/evidentLog/evidentLogScreen" object="mgrWorkspace" method="get" class="form-narrow
form-horizontal">
<#--// action="${rc.getContextPath()}/licenseHistory/evidentLogScreen"-->
    <label>証跡ログ</label>
    <#if mgrWorkspaces??>
        <select onchange="this.form.submit()" id="mgrWorkspaceList" name="workspaceId">
            <#list mgrWorkspaces as mgrWorkspace>
                <#if workspaceId??>
                    <#if mgrWorkspace.workspaceId == workspaceId>
                        <option value="${mgrWorkspace.workspaceId}" selected>${mgrWorkspace.workspaceName}</option>
                    <#else >
                        <option value="${mgrWorkspace.workspaceId}">${mgrWorkspace.workspaceName}</option>
                    </#if>
                <#else >
                    <option value="${mgrWorkspace.workspaceId}">${mgrWorkspace.workspaceName}</option>
                </#if>
            </#list>
        </select>
    </#if>
    <table border="1" style="text-align: left;">
        <tr>
            <th>イベントID</th>
            <th>処理日時</th>
            <th>ユーザID</th>
            <th>ワークアイテムID</th>
            <th>履歴NO</th>
            <th>プロセスID</th>
            <th>プロセスバージョン</th>
            <th>タスクID</th>
        </tr>
        <#if strLogAccesses??>
            <#list strLogAccesses as strLogAccess>
                <tr>
                    <td style="text-align: right;">${strLogAccess.eventId!""}</td>
                    <td style="text-align: left;">${strLogAccess.processTime!""}</td>
                    <td style="text-align: left;">${strLogAccess.userId!""}</td>
                    <td style="text-align: right;">${strLogAccess.workitemId!""}</td>
                    <td style="text-align: right;">${strLogAccess.historyNo!""}</td>
                    <td style="text-align: right;">${strLogAccess.processId!""}</td>
                    <td style="text-align: right;">${strLogAccess.processVersion!""}</td>
                    <td style="text-align: right;">${strLogAccess.taskId!""}</td>
                </tr>
            </#list>
        <#else>
            <tr>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
        </#if>
    </table>
    <script id="some-template" type="text/x-handlebars-template">
        <table>
            <thead>
            <th>Name</th>
            </thead>
            <tbody>
            {{#strLogAccesses}}
            <tr>
                <td>{{eventId}}</td>
            </tr>
            {{/strLogAccesses}}
            </tbody>
        </table>
    </script>
    <script>
        var source = $("#some-template").html();
        var template = Handlebars.compile(source);
        function searchText() {
            $.ajax({
                type: "POST",
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                headers: {
                    Accept: 'application/json'
                },
                url: "${rc.getContextPath()}/evidentLog/searchLicenseHistory",
                data: {workspaceId: "001"},
                success: function (strLogAccesses) {
//                    var context = {
//                        strLogAccesses: JSON.parse(strLogAccesses)
//                    }
                    console.log(strLogAccesses);
//                    $('body').append(template(context));
                }
            });
        }
    </script>
</form>
</@standard.standardPage>