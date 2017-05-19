<@standard.standardPage title="EDITTEMPLATE">
<form action="${rc.getContextPath()}/template/updateOne"
	object="mgrTemplate" method="post">
	<table>
		<tr>
			<td>テンプレートID</td>
			<td><input type="text" id="txtTemplate" name="templateId"
				value="${mgrTemplate.templateId}" height="20px" width="150px"
				style="text-align: left" readonly="readonly"></td>
		</tr>
		<tr>
			<td>テンプレート名</td>
			<td><input type="text" id="txtTemplateName" name="templateName"
				value="${mgrTemplate.templateName}" height="20px" width="150px"
				style="text-align: left"></td>
		</tr>
		<tr>
			<td>テンプレートテーブル名</td>
			<td><input type="text" id="txtTemplateTableName"
				name="templateTableName" value="${mgrTemplate.templateTableName}"
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
		<#if mgrTemplate ?? >
           <#list mgrTemplate.templateFields as field>
               <tr id="row${field?index}" >
                   <td><input type="checkbox" id="delRow${field?index}"
                      name="DeleteRow" disabled="disabled"></td>
                   <td><input type="text" id="delRow${field?index}"
                      name="templateFields[${field?index}].name" value="${field.name}" readonly="readonly"></td>
                   <td><input type="text" id="delRow${field?index}"
                      name="templateFields[${field?index}].description" value="${field.description}" readonly="readonly"></td>
                    <td><input type="text" id="delRow${field?index}"
                      name="templateFields[${field?index}].type" value="${field.type}" readonly="readonly"></td>
                   <td><input type="text" id="delRow${field?index}"
                      name="templateFields[${field?index}].size" value="${field.size}" readonly="readonly"></td>
                  <#if field.required>
                      <td><input type="checkbox" id="delRow${field?index}"
                        name="templateFields[${field?index}].required" checked disabled="disabled"></td>
                  <#else>
                       <td><input type="checkbox" id="delRow${field?index}"
                        name="templateFields[${field?index}].required" disabled="disabled"></td> 
                  </#if>
                  <#if field.encrypted>
	                   <td><input type="checkbox" id="delRow${field?index}"
	                      name="templateFields[${field?index}].encrypted" checked disabled="disabled"></td>
                  <#else>
                       <td><input type="checkbox" id="delRow${field?index}"
                        name="templateFields[${field?index}].encrypted" disabled="disabled"></td> 
                  </#if>
                </tr>
           </#list>
         </#if>
    </table>
	<br>
	<table style="width: 100%;">
		<tr>
			<td align="center" style="width: 50%;"><a
				href="${rc.getContextPath()}/template/showList" class="button">キャンセル</a></td>
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
