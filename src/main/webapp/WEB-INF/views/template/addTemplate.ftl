<@standard.standardPage title="ADDNEWTEMPLATE">

<script type="text/javascript">
	$(document)
			.ready(
					function() {
						var i = 0;
						$('#addRow')
								.click(
										function() {
											var name = $('#name').val();
											var description = $('#description')
													.val();
											var typeDisplay = $('#fieldType option:selected').text();
											var typeValue = $('#fieldType option:selected').val();
											var size = $('#size').val();
											var required = $('#required').is(
													':checked');
											var encrypted = $('#encrypted').is(
													':checked');

											$('#fieldList tbody')
													.append(
															"<tr>"
																	+ "<td><input type='checkbox' id='delRow["
																	+ i
																			.toString()
																	+ "]'name='DeleteRow'></td>"
																	+ "<td><input  type='text' name='templateFields["
																	+ i
																			.toString()
																	+ "].name' value='"
																	+ name
																	+ "'/></td>"
																	+ "<td><input  type='text'name='templateFields["
																	+ i
																			.toString()
																	+ "].description' value='"
																	+ description
																	+ "'/></td>"
																	+ "<td><input type='text' value='"
																	+ typeDisplay
																	+ "'/><input type='hidden'name='templateFields["
                                                                    + i
                                                                    .toString()
                                                                        + "].type' value='"
                                                                        + typeValue
                                                                        + "'/></td>"
																	+ "<td><input  type='text'name='templateFields["
																	+ i
																			.toString()
																	+ "].size' value='"
																	+ size
																	+ "'/></td>"
																	+ "<td><input  type='text'name='templateFields["
																	+ i
																			.toString()
																	+ "].required' value='"
																	+ required
																	+ "'/></td>"
																	+ "<td><input  type='text'name='templateFields["
																	+ i
																			.toString()
																	+ "].encrypted' value='"
																	+ encrypted
																	+ "'/></td></tr>");
											i += 1;
											$('#name').val("");
											$('#description').val("");
											$('#type').val("");
											$('#size').val("");
											$('#required').prop("checked",
													false);
											$('#encrypted').prop("checked",
													false);
										});

						$('#clearRow').click(function() {
							$('#name').val("");
							$('#description').val("");
							$('#type').val("");
							$('#size').val("");
							$('#required').prop("checked", false);
							$('#encrypted').prop("checked", false);
						})
					});
</script>
<form action="${rc.getContextPath()}/template/insertOne"
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
				height="20px" width="150px" style="text-align: left"></td>
		</tr>
		<tr>
			<td>テンプレートテーブル名</td>
			<td><input type="text" id="txtTemplateTableName"
				value="${mgrTemplate.templateTableName}" name="templateTableName"
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
               <tr id="row${field?index}">
                   <td><input type="checkbox" id="delRow${field?index}"
                      name="DeleteRow" ></td>
                   <td><input type="text" id="delRow${field?index}"
                      name="templateFields[${field?index}].name" value="${field.name}"></td>
                   <td><input type="text" id="delRow${field?index}"
                      name="templateFields[${field?index}].description" value="${field.description}"></td>
                    <td><input type="text" id="delRow${field?index}"
                      name="templateFields[${field?index}].type" value="${field.type}"></td>
                   <td><input type="text" id="delRow${field?index}"
                      name="templateFields[${field?index}].size" value="${field.size}"></td>
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
	<div>
		<input type="button" value="削除" disabled="disabled">
	</div>
	<br>
	<table border="1" style="width: 100%;">
		<tr>
			<th>名前</th>
			<th>説明</th>
			<th>型</th>
			<th>サイズ</th>
			<th>必須</th>
			<th>暗号化</th>
		</tr>
		<tr>
			<td><input type="text" id="name" name="name" height="20px"
				width="150px" style="text-align: left"></td>
			<td><input type="text" id="description" name="description"
				height="20px" width="150px" style="text-align: left"></td>
			<td><select name="fieldType" id="fieldType">
                <#if fieldTypes??>
                    <#list fieldTypes as fieldType>
                        <option value="${fieldType.value}" selected>${fieldType}</option>
                    </#list>
               </#if>
                   <!--  <option value="1">INT</option>
                    <option value="2">NCHAR</option>
                    <option value="3">NVACHAR2</option>
                    <option value="4">LONG</option>
                    <option value="5">FLOAT</option>
                    <option value="6">DECIMAL</option>
                    <option value="7">NUMBER</option> -->
               </select>
            </td>
			<td><input type="text" id="size" name="size" height="20px"
				width="150px" style="text-align: left"></td>
			<td><input type="checkbox" id="required" name="required"
				height="20px" width="150px" style="text-align: left"></td>
			<td><input type="checkbox" id="encrypted" name="encrypted"
				height="20px" width="150px" style="text-align: left"></td>
		</tr>
		<tr>
			<td align="center" style="width: 50%;" colspan="3"><input
				type="button" value="キャンセル" id="clearRow"></td>
			<td align="center" colspan="3"><input type="button" value="決定"
				id="addRow"></td>
		</tr>
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
