<@standard.standardPage title="EVIDENT LOG">
<form action="${rc.getContextPath()}/evidentLog/evidentLogScreen" object="mgrWorkspace" method="get" class="form-narrow
form-horizontal">
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
                    <#if strLogAccess.eventId??>
                        <td style="text-align: right;">${strLogAccess.eventId}</td>
                    <#else >
                        <td></td>
                    </#if>
                    <#if strLogAccess.processTime??>
                        <td style="text-align: left;">${strLogAccess.processTime}</td>
                    <#else >
                        <td></td>
                    </#if>
                    <#if strLogAccess.userId??>
                        <td style="text-align: left;">${strLogAccess.userId}</td>
                    <#else >
                        <td></td>
                    </#if>
                    <#if strLogAccess.workitemId??>
                        <td style="text-align: right;">${strLogAccess.workitemId}</td>
                    <#else >
                        <td></td>
                    </#if>
                    <#if strLogAccess.historyNo??>
                        <td style="text-align: right;">${strLogAccess.historyNo}</td>
                    <#else >
                        <td></td>
                    </#if>
                    <#if strLogAccess.processId??>
                        <td style="text-align: right;">${strLogAccess.processId}</td>
                    <#else >
                        <td></td>
                    </#if>
                    <#if strLogAccess.processVersion??>
                        <td style="text-align: right;">${strLogAccess.processVersion}</td>
                    <#else >
                        <td></td>
                    </#if>
                    <#if strLogAccess.taskId??>
                        <td style="text-align: right;">${strLogAccess.taskId}</td>
                    <#else >
                        <td></td>
                    </#if>
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
</form>
</@standard.standardPage>