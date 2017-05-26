<div id="databaseArea">
    <div><span>Database</span></div>
    <table>
        <tr>
            <td>Schema name</td>
            <td><input type="text" name="strageDb.schemaName" value="${(strageDb.schemaName!"")}"/></td>
        </tr>
        <tr>
            <td>DB user</td>
            <td><input type="text" name="strageDb.dbUser" value="${strageDb.dbUser!""}"/></td>
        </tr>
        <tr>
            <td>DB passsword</td>
            <td><input type="text" name="strageDb.dbPassword" value="${strageDb.dbPassword!""}"/></td>
        </tr>
        <tr>
            <td>Owner</td>
            <td><input type="text" name="strageDb.owner" value="${strageDb.owner!""}"/></td>
        </tr>
        <tr>
            <td>DB server</td>
            <td><input type="text" name="strageDb.dbServer" value="${strageDb.dbServer!""}"/></td>
        </tr>
    </table>

    <div id="fileArea">
        <div><span>File</span></div>
        <table>
            <tr>
                <td>Site ID</td>
                <td>
                    <select id="siteId" name="strageFile.siteId">
                      <#if siteIds??>
                        <#list siteIds as siteId>
                          <#if strageFile.siteId?? && siteId == strageFile.siteId>
                              <option value="${siteId}" selected>${siteId}</option>
                          <#else>
                              <option value="${siteId}">${siteId}</option>
                          </#if>
                        </#list>
                      </#if>
                    </select>
                </td>
            </tr>
            <tr>
                <td>Site management method</td>
                <td>
                <#if strageFile.siteManagementType?? && "fileUtilFull" == strageFile.siteManagementType>
                    <input type="radio" name="strageFile.siteManagementType" class="siteManagementMethod"
                           value="fileUtilFull" checked>Use until full
                    <input type="radio" name="strageFile.siteManagementType" class="siteManagementMethod"
                           value="fileRoundRobin">Round robin
                <#else>
                    <input type="radio" name="strageFile.siteManagementType" class="siteManagementMethod"
                           value="fileUtilFull">Use until full
                    <input type="radio" name="strageFile.siteManagementType" class="siteManagementMethod"
                           value="fileRoundRobin" checked>Round robin
                </#if>
                </td>
            </tr>
        </table>
    </div>
</div>