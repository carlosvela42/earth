<#assign isDatabase=(process.documentDataSavePath == "database")/>

<div class="tab_head">
    <table class="table_form">
        <td width="40%">Document data save path</td>
        <td>
            <div class="row">
                <div class="col-md-6">
                    <input type="radio" name="process.documentDataSavePath" class="documentDataSavePath"
                           value="database" ${(isDatabase)?string('checked', '')}
                           >Database
                </div>
                <div class="col-md-6">
                    <input type="radio" name="process.documentDataSavePath" class="documentDataSavePath"
                           value="file" ${(isDatabase)?string('', 'checked')}
                    >file
                </div>
            </div>
        </td>
    </table>
</div>

<div class="tab_content_wrapper">
    <div id="databaseArea" style="display: ${(isDatabase)?string('block', 'none')}">
        <table class="table_form">
            <tr>
                <td width="40%">Schema name</td>
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
    </div>

    <div id="fileArea" style="display: ${(isDatabase)?string('none', 'block')}">
        <table class="table_form">
            <tr>
                <td width="40%">Site ID</td>
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

