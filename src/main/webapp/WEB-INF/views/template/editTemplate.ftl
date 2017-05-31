<@standard.standardPage title="EDITTEMPLATE">
<form action="${rc.getContextPath()}/template/updateOne"
    object="templateForm" method="post">
    <table>
        <tr>
            <td>テンプレートID</td>
            <td><input type="text" id="txtTemplate" name="templateId"
                value="${templateForm.templateId!""}" height="20px" width="150px"
                style="text-align: left" readonly="readonly"></td>
        </tr>
        <tr>
            <td>テンプレート名</td>
            <td><input type="text" id="txtTemplateName" name="templateName"
                value="${templateForm.templateName!""}" height="20px" width="150px"
                style="text-align: left"></td>
        </tr>
        <tr>
            <td>テンプレートテーブル名</td>
            <td><input type="text" id="txtTemplateTableName"
                name="templateTableName" value="${templateForm.templateTableName!""}"
                height="20px" width="150px" style="text-align: left"
                readonly="readonly"></td>
        </tr>
    </table>
    <div align="left">フィールド定義</div>
    <table border="1" style="width: 100%;" id="fieldList">
        <tr>
            <th colspan="2">名前</th>
            <th>説明</th>
            <th>型</th>
            <th>サイズ</th>
            <th>必須</th>
            <th>暗号化</th>
        </tr>
        <#if templateForm ?? >
           <#list templateForm.templateFields as field>
               <tr id="row${field?index}" >
                   <td><input type="checkbox" id="delRow${field?index}"
                      name="DeleteRow" disabled="disabled"></td>
                   <td><input type="text" id="delRow${field?index}"
                      name="templateFields[${field?index}].name" value="${field.name!""}" readonly="readonly"></td>
                   <td><input type="text" id="delRow${field?index}"
                      name="templateFields[${field?index}].description" value="${field.description!""}" readonly="readonly"></td>
                    <td><input type="text" id="delRow${field?index}"
                      name="templateFields[${field?index}].type" value="${field.type!""}" readonly="readonly"></td>
                       <td><input type="text" id="delRow${field?index}"
                          name="templateFields[${field?index}].size" value="${field.size!""}" readonly="readonly"></td>
	                  <#if field.required>
	                    <td><input type="checkbox" id="delRow${field?index}"
                           name="templateFields[${field?index}].required" checked disabled="disabled">
                        <input  type="hidden" name="templateFields[${field?index}].required" value='true'/></td>
                      <#else>
                        <td><input type="checkbox" id="delRow${field?index}"
                            name="templateFields[${field?index}].required" disabled="disabled">
                        <input  type="hidden" name="templateFields[${field?index}].required" value='false'/></td></#if>
                    <#if field.encrypted>
		              <td><input type="checkbox" id="delRow${field?index}"
		                name="templateFields[${field?index}].encrypted" checked disabled="disabled">
		                <input  type="hidden" name="templateFields[${field?index}].encrypted" value='true'/></td>
		            <#else>
            <td><input type="checkbox" id="delRow${field?index}"
                name="templateFields[${field?index}].encrypted" disabled="disabled">
                <input  type="hidden" name="templateFields[${field?index}].encrypted" value='false'/></td></#if>
                </tr>
           </#list>
         </#if>
    </table>
    <br>
    <table style="width: 100%;">
        <tr>
            <td align="center" style="width: 50%;"><a
                href="${rc.getContextPath()}/template" class="button">キャンセル</a></td>
            <td align="center"><input type="submit" value="決定"
                class="button"></td>
        </tr>
    </table>
    <#if messages??> <#list messages as message>
        <div>
            <b style="color: red;">${message.getContent()}</b>
        </div>
        </#list> </#if>
</form>
</@standard.standardPage>
