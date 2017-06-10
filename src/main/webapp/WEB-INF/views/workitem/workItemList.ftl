<@standard.standardPage title="WORKITEMLIST">

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

<script id="searchList" type="text/x-handlebars-template">
       <table border="1" id="search">
            {{#searchForms}}
            <tr>
               <th style="width:200px"><div onclick="$('#ColumnNameSearch').text($(this).find('a b').text());" data-toggle="modal" data-target="#myModal"><img src="${rc.getContextPath()}/resources/images/filter.png" class="fieldFilter icon icon-filter"><a class="sortHdr"><b>WorkItem_ID</b></a></div></th>
                    {{#each this.columns}}
                        <th style="width:200px"><div onclick="$('#ColumnNameSearch').text($(this).find('a b').text());" data-toggle="modal" data-target="#myModal"><img src="${rc.getContextPath()}/resources/images/filter.png" class="fieldFilter icon icon-filter"><a class="sortHdr"><b>{{this.name}}</b></a></div></th>
                    {{/each}}
            </tr>
                    {{#each this.rows}}
                        <tr>
                                <td style="width:200px">{{this.workitemId}}</td>
                           {{#each this.columns}}
                                <td style="width:200px">{{this.value}}</td>
                           {{/each}}
                       </tr>
                   {{/each}}
            {{/searchForms}}
        </table>
</script>

<script type="text/javascript">

var i = 1;
var j=0;
function search() {  
    if($('#img'+i).css('visibility')=="hidden"){
        var className = ".num"+i;
        $(className).html(i-j);
        $('#img'+i).css('visibility','visible');
        $('#val'+i).removeAttr("onkeypress");
        i++;
        $('#conditionSearch').append('<tr id="'+i+'"><td class="num'+i+'">'+i+'</td><td>'+$('#ColumnNameSearch').text()+'</td><td><select id="operator'+i+'" onchange="hide('+i+');"><option value="0">=</option><option value="1">!=</option>'
        		   +'option value="2">&gt;</option><option value="3">&gt;=</option><option value="4">&lt;</option><option value="5">&lt;=</option>'
        		   +'<option value="8">is null</option><option value="9">is not null</option><option value="10">is empty</option><option value="11">is not empty</option></select></td>'
                   +'<td><div class="value"><input type="text" name="value" value="" data-description="Condition" class="valid item" id="val'+i+'" onkeypress="search()"></div></td>'
                   +'<td><img id="img'+i+'" src="${rc.getContextPath()}/resources/images/cross-button.png" style="visibility:hidden" onclick="deletee('+i+');"></td></tr>');
    }           
}

function save(){
    var string =""; 
    for(var k =1; k<=i;k++){
        if($('#operator'+ k +' :selected').val() < 6  ){
            if($('#val' + k).val() != ""&& $('#'+k).length){
            	string =string +  $('#ColumnNameSearch').text() + $('#operator'+ k +' :selected').text() + " " + $('#val'+k).val() +" " + $('#valid :selected').val();
            }
        }else{
        	   string =string +  " status "+ $('#operator'+ k +' :selected').text() + " " + $('#valid :selected').val();
        }    
   }
}
	window.onload = function() {
		var countChecked = function() {
			var str = ""
			$('input[name=DeleteRow]:checked').each(function() {
				str += $(this).attr('value') + ",";
			});

			if (str.length > 0) {
				str = str.substring(0, str.length - 1);
			}
			$("#workitemId").val(str);
		};

		$("input[name=DeleteRow]").on("click", countChecked);

		$("#workspaceId").val($('#workspaces option:selected').val());
		var workspaceId = $('#workspaces option:selected').text();
		if (workspaceId == '-- Please choose --') {
			$('#btSearch').attr("style", 'pointer-events: none;');
			$('.icon.icon_edit').attr("style", 'pointer-events: none;');
			$('#btUnlock').prop("disabled", true);
		} else {
			$('#btSearch').attr("style", '');
			$('.icon.icon_edit').attr("style", '');
			$('#btUnlock').prop("disabled", false);
		}
	    
		function unlock() {
			var str = $("#workitemId").val();
			var workspaceId = $('#workspaces option:selected').text();
			console.log("workitemId: " + str);

			if (str.length > 0) {
				$("#workItemForm").attr("action", "unlock");
				$("#workItemForm").submit();
			} else {
				$("#message").html("choose workItem !");
			}
		}
		$(function() {
			$("#workspaces").on('change', function() {
				$("#workspaceId").val($('#workspaces option:selected').val());
				var workspaceId = $('#workspaces option:selected').text();
				if (workspaceId == '-- Please choose --') {
					$('#btSearch').attr("style", 'pointer-events: none;');
					$('.icon.icon_edit').attr("style", 'pointer-events: none;');
					$('#btUnlock').prop("disabled", true);
				} else {
					$('#btSearch').attr("style", '');
					$('.icon.icon_edit').attr("style", '');
					$('#btUnlock').prop("disabled", false);
					$("#workItemForm").attr("action", "switchWorkspace");
					$("#workItemForm").submit();

				}
			});
		})

		$('#btSearchList').on("click", function() {
			var source = $("#searchList").html();
			var template = Handlebars.compile(source);
			var baseUrl = "${rc.getContextPath()}/workItem/";
			var searchForm = {};
			searchForm.templateId = $('#templateId option:selected').val();
			searchForm.templateType = $('#templateType option:selected').val();
			$.ajax({
				url : baseUrl + "search",
				data : searchForm,
				 contentType:'application/json',
				dataType : 'json',
				type : 'GET',
				success : function(data) {
					console.log(data);
					var context = {
						searchForms : data
					}
					console.log(context);
					console.log(context.searchForms.columns);
					console.log(template(context));
					$('#searchListDiv').html(template(context));
				},
				error : function(data) {
					alert("error");
				}
			});
		});
	}
	
</script>
<div class="overlay" id="addFormSearch" role="dialog">
	<form method="get" id="searchForm" object="searchForm"
		action="${rc.getContextPath()}/workItem/search">
		<div class="modal-content popup">
			<table>
				<tr>
					<td><label>TemplateName</label></td>
					<#if searchForms??>
	                   <td><select name="templateId" id="templateId">
	                       <option selected="selected">-- Please choose --</option>
	                           <#list searchForms as searchForm>
	                                 <option value="${searchForm.templateId}-${searchForm.templateTableName}"  ${selected!""}>${searchForm.templateName}</option>
	                           </#list>
	                       </select></td>
	                   <td><label>Template_Type</label></td>
	                   <td><select name="templateType" id="templateType">
	                       <option selected="selected">-- Please choose --</option>
	                           <#list templateTypes as templateType>
	                               <option value="${templateType}"  ${selected!""}>${templateType}</option>
	                           </#list>
	                       </select></td>
	                   </#if>
	            </tr>
			</table>
			<br>
			<div id="searchListDiv">
				<table>
					<#if searchForm??>
		                <#if searchForm.columns?exists>
		                   <tr>
		                       <#list searchForm.columns as column>
		                           <th><b>${column.name?upper_case}</b></th>
		                       </#list>
		                   </tr>
		                      <#if searchForm.rows?exists>
		                        <#list  searchForm.rows as row>
		                          <tr>
		                              <#list row.columns as column>
		                                  <td>${column.value}</td>
		                              </#list>
		                          </tr>
		                       </#list>
		                     </#if>
		               </#if>
		            </#if>
	            </table>
			</div>
			<table id="button">
				<tr>
					<td><input type="button" class="button" value="SEARCH"
						id="btSearchList"></td>
					<td><a href="#" class="button">Close</a></td>
				</tr>
			</table>
		</div>
	</form>
</div>
<br>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">Search Filter</h4>
                </div>
                <div class="modal-body">
                    <label>Show Result when:</label>
                    <br/>
                    <select id="valid">
                        <option value="and" selected="selected">All of the conditions below are met</option>
                        <option value="or">Any of the conditions below are met</option>
                    </select>
                    <table id="conditionSearch">
                        <colgroup>
                        <col style="width:2em">
                        <col style="width:16em">
                        <col style="width:10em">
                        <col>
                        <col style="width:3em">
                    </colgroup>
                    <tr>
                        <td>#</td>
                        <td>Column</td>
                        <td>Operator</td>
                        <td>Value</td>
                        <td></td>
                    </tr>
                    <tr id="1">
                        <td class="num1"></td>
                        <td id="ColumnNameSearch"></td>
                        <td>
                            <select id="operator1" onchange="hide(1);">
                                <option value="0">=</option>
                                <option value="1">!=</option>
                                <option value="2">&gt;</option>
                                <option value="3">&gt;=</option>
                                <option value="4">&lt;</option>
                                <option value="5">&lt;=</option>
                                <option value="8">is null</option>
                                <option value="9">is not null</option>
                                <option value="10">is empty</option>
                                <option value="11">is not empty</option>
                            </select>
                        </td>
                        <td>
                            <div class="value">
                                <input type="text" name="value" value="" data-description="Condition" class="valid item" id="val1" onkeypress="search()">
                            </div>
                        </td>
                        <td>
                            <img id="img1" src="${rc.getContextPath()}/resources/images/cross-button.png" style="visibility:hidden" onclick="deletee(1);">
                        </td>
                    </tr>        
                </table>            
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary pull-left">Clear</button>
                <button type="button" class="btn btn-primary" id="save" onclick="save();">Save</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                
            </div>
        </div>
    </div>
</div>
<br>
<form method="post" id="workItemForm" object="workItems"
	action="${rc.getContextPath()}/workItem/">
	<input type="hidden" id="workitemId" name="workitemId" value="0">
	<input type="hidden" id="workspaceId" name="workspaceId">
	<div>
		<b id="message" style="color: red;"></b>
	</div>
	<table id="button">
		<tr>
			<td><a href="#addFormSearch" id="btSearch">SEARCH</td>
			<td><select name="workspaces" id="workspaces">
					<option selected="selected">-- Please choose --</option>
					<#if  mgrWorkspaces??>
                        <#list mgrWorkspaces as mgrWorkspace>
                          <#if workspaceId??>
                                 <#assign selected=(mgrWorkspace.workspaceId == workspaceId)?then("selected","")>
                            </#if>
                            <option value="${mgrWorkspace.workspaceId}"  ${selected!""}>${mgrWorkspace.workspaceName}</option>
                        </#list>
                   </#if>
                </select></td>
		</tr>
	</table>
	<br>
	<table border="1" id="directorysTable">
		<tr align="left">
			<th colspan="3" >WorkItem_ID</th>
			<th>Lock_Status</th>
			<th>Task</th>
			<th>Template_ID</th>
			<th>Template_Name</th>
		</tr>
		<#if workItems??>
                <#list workItems as workItem>
                <tr id="row${workItem?index}">
                     <td><input type="checkbox" id="delRow${workItem?index}"name="DeleteRow" value="${workItem.workitemId!""}"></td>
                     <td class="text_center">
                           <a id="editWorkItem" class="icon icon_edit" href="${rc.getContextPath()}/workItem/showDetail?workitemId=${workItem.workitemId}"></a>
                       </td>
                     <td id="dataDirectoryId${workItem?index}">${workItem.workitemId}</td>
                     <td id="dataDirectoryId${workItem?index}">${workItem.statusLock}</td>
                     <td id="dataDirectoryId${workItem?index}">${workItem.taskName}</td>
                     <td id="dataDirectoryId${workItem?index}">${workItem.templateId}</td>
                     <td id="dataDirectoryId${workItem?index}">${workItem.templateName}</td>
                </tr>
                </#list>
            </#if>
        </table>
	<br>
	<table id="button">
		<tr>
			<td><input id="btUnlock" type="button" class="button"
				value="UNLOCK" onclick="unlock()"></td>
		</tr>
	</table>
</form>
</@standard.standardPage>
