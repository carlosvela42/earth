<#assign isDatabase=(process.documentDataSavePath == "database")/>

<div class="tab_head">
    <table class="table_form">
        <td width="40%">${e.get('process.DocumentPath')}</td>
        <td>
            <div class="row">
                <div class="col-md-6">
                    <input type="radio" name="process.documentDataSavePath" class="documentDataSavePath"
                           value="database" ${(isDatabase)?string('checked', '')}
                           >${e.get('process.Database')}
                </div>
                <div class="col-md-6">
                    <input type="radio" name="process.documentDataSavePath" class="documentDataSavePath"
                           value="file" ${(isDatabase)?string('', 'checked')}
                    >${e.get('process.File')}
                </div>
            </div>
        </td>
    </table>
</div>

<div class="tab_content_wrapper">
    <div id="databaseArea" style="display: ${(isDatabase)?string('block', 'none')}">
        <table class="table_form">
            <tr>
                <td width="40%">${e.get('process.SchemaName')}</td>
                <td><input type="text" name="strageDb.schemaName" value="${(strageDb.schemaName!"")}"/></td>
            </tr>
            <tr>
                <td>${e.get('process.DBuser')}</td>
                <td><input type="text" name="strageDb.dbUser" value="${strageDb.dbUser!""}"/></td>
            </tr>
            <tr>
                <td>${e.get('process.DBpasssword')}</td>
                <td><input type="text" name="strageDb.dbPassword" value="${strageDb.dbPassword!""}"/></td>
            </tr>
            <tr>
                <td>${e.get('process.Owner')}</td>
                <td><input type="text" name="strageDb.owner" value="${strageDb.owner!""}"/></td>
            </tr>
            <tr>
                <td>${e.get('process.DBserver')}</td>
                <td><input type="text" name="strageDb.dbServer" value="${strageDb.dbServer!""}"/></td>
            </tr>
        </table>
    </div>

    <div id="fileArea" style="display: ${(isDatabase)?string('none', 'block')}">
        <table class="table_form">
            <tr>
                <td width="40%">${e.get('process.SiteID')}</td>
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
                <td>${e.get('process.SiteManagement')}</td>
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

