<#macro removePanel>
<div class="content_foot">
    <p>
    <div class="center-block">
        <form id="deleteForm" action="deleteList" method="post">
        </form>
        <button class="btn btn_delete" id="deleteButton"><@spring.message code='button.delete'/></button>
        <a class="btn_confirm_delete"><input type="checkbox" class="ckb_confirm_delete" id="deleteConfirm"/><span><@spring.message code='button.confirmDelete'/></span></a>
    </div>
    </p>
</div>
</#macro>

<#macro unLockPanel>
<div class="content_foot">
    <p>
    <div class="center-block">
        <button class="btn btn_unlock" id="unlockButton"><@spring.message code='button.unLock'/></button>
        <form id="unlockForm" object="deleteListForm" method="post">

        </form>
    </div>
    </p>
</div>
</#macro>

<#macro detailUpdatePanel object="" formId="">
<div class="content_foot">
    <div class="center-block">
        <form action="${rc.getContextPath()}/${object}/cancel" class="form-cancel" method="post">
            <button class="btn btn_cancel" id="cancelButton"><@spring.message code='button.cancel'/></button>
        </form>
        <button class="btn btn_save" id="saveButton"
                data-form_id="${formId}"><@spring.message code='button.save'/></button>
    </div>
</div>
</#macro>

<#macro updateWorkItemPanel object="" formId="">
<div class="content_foot">
    <div class="center-block">
        <button class="btn btn_cancel" id="cancelButton" data-target="#closeEditWorkItemModal"><@spring.message code='button.back'/></button>
        <button class="btn btn_save" id="saveButton"
                data-form_id="${formId}"><@spring.message code='button.saveTemplate'/></button>
    </div>
</div>
</#macro>


<#macro searchFormPanel object="" formId="">

<div class="center-search">
    <form method="get" object="searchForm" action="${rc.getContextPath()}/${object}/"
          class="form-search">
        <#if searchForm??>
            <#assign limit=searchForm.limit!"">
            <#assign skip=searchForm.skip!"">
        </#if>
        <input type="number" class="radius textbox" placeholder="Limit" value="${limit!""}"
                autofocus>
        <input type="number" class="radius textbox" placeholder="Skip" value="${skip!""}"
               >
        <button class="btn searchBtn icon_search" id="searchButton"
                data-form_id="${formId}"><img src="${rc.getContextPath()
        }/resources/images/search.png"><@spring.message code='button.search'/></button>
    </form>
</div>

</#macro>

<#macro searchColumnFormPanel object="" formId="">

<div class="center-search-server">
        <input type="number" name="limit" class="radius textbox" id="limitRecord" placeholder="Limit"
               value="${searchByColumnsForm???then(searchByColumnsForm.limit!"","")}" autofocus
               onchange="changeText(this,'limit');">
        <input type="number" name="skip" class="radius textbox" id="skipRecord" placeholder="Skip" value="${searchByColumnsForm???then(searchByColumnsForm.skip!"","")}"
               onchange="changeText(this,'skip');">
        <button class="btn searchBtn icon_search" id="searchButton">
            <img src="${rc.getContextPath()}/resources/images/search.png">
            <@spring.message code='button.search'/>
        </button>
</div>

</#macro>

<#macro addFormPanel object="" formId="">
<script id="addRow" type="text/x-handlebars-template">
    <tr {{name}}Id="{{id}}">
        <td><input type="checkbox" class="deleteCheckBox"/></td>
        <td class="text_center">
            <span class="icon icon_edit" onclick="editRow('{{name}}','{{id}}');"></span>
        <td class="text">
            <span>{{id}}</span>
            <input type="hidden"
                   name="{{name}}AccessRights[{{index}}].{{name}}Id"
                   value="{{id}}"
            />
        </td>
        <td class="text">
            <span>{{accessRight}}<span>
            <input type="hidden"
                   name="{{name}}AccessRights[{{index}}].accessRight"
                   value="{{accessRight}}"
            />
        </td>
    </tr>
</script>

<div class="modal fade" id="addFormuser" role="dialog">
    <select id="userSelectOrigin" style="display: none;">
        <#list mgrUsers as mgrUser>
            <option id="${mgrUser.userId}"
                    value="${mgrUser.userId}">${mgrUser.userId!""}</option>
        </#list>
    </select>
    <div class="modal-dialog">
    <#--<div class="overlay" id="addFormuser" role="dialog">-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" onclick="return cancel('user');">X</button>
                <span class="modal-title title_popup">
                    <@spring.message code='mgrMenu.addAuthority'/>
                </span>
            </div>
            <input type="hidden" id="userEditItem" value="0">
            <input type="hidden" id="userIdOld" value="">
            <div class="modal-body">
                <table class="table_form">
                    <tr style="border-bottom: 0px">
                        <td width="50%">${e.get('user.id')}</td>
                        <td>
                            <select id="userSelect">
                                <#list mgrUsers as mgrUser>
                                    <option id="${mgrUser.userId}"
                                            value="${mgrUser.userId}">${mgrUser.userId!""}</option>
                                </#list>
                            </select>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="modal-footer modal-footer-popup">
                <button type="button" class="btn btn-default btn_cancel_popup btn_popup" data-dismiss="modal" onclick="return cancel('user');">
                ${e.get('button.cancel')}
                </button>
                <button align="center" colspan="3" class="btn btn-default btn_save_popup btn_popup" id="addRowUser"
                        onclick="editRowNew('user')">
                ${e.get('button.save')}
                </button>
            </div>
        </div>
    </div>
</div>


<#--Profile-->
<div class="modal fade" id="addFormprofile" role="dialog">
    <select id="profileSelectOrigin" style="display: none;">
        <#list mgrProfiles as mgrProfile>
            <option id="${mgrProfile.profileId}"
                    value="${mgrProfile.profileId}">${mgrProfile.profileId!""}</option>
        </#list>
    </select>
    <div class="modal-dialog">
    <#--<div class="overlay" id="addFormprofile" role="dialog">-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" onclick="return cancel('profile');">
                    X
                </button>
                <span class="modal-title title_popup">
                     <@spring.message code='mgrMenu.addAuthority'/>
                    </span>
            </div>
            <input type="hidden" id="profileEditItem" value="0">
            <input type="hidden" id="profileIdOld" value="">
            <div class="modal-body">
                <table class="table_form">
                    <tr style="border-bottom: 0px">
                        <td width="50%">${e.get('profile.id')}</td>
                        <td>
                            <select id="profileSelect">
                                <#list mgrProfiles as mgrProfile>
                                    <option id="${mgrProfile.profileId}"
                                            value="${mgrProfile.profileId}">${mgrProfile.profileId!""}</option>
                                </#list>
                            </select>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="modal-footer modal-footer-popup">
                <button type="button" class="btn btn-default btn_cancel_popup btn_popup" data-dismiss="modal" onclick="return cancel('profile');">
                ${e.get('button.cancel')}
                </button>
                <button align="center" colspan="3" class="btn btn-default btn_save_popup btn_popup" id="addRowProfile"
                        onclick="editRowNew('profile')">
                ${e.get('button.save')}
                </button>
            </div>
        </div>
    </div>
</div>
</#macro>

<#macro addFormTemplatePanel object="" formId="">
<script id="addRow" type="text/x-handlebars-template">
    <tr {{name}}Id="{{id}}">
        <td><input type="checkbox" class="deleteCheckBox"/></td>
        <td class="text_center">
            <span class="icon icon_edit"
                  onclick="editRow('{{name}}','{{id}}','{{accessRight}}','{{screenType}}');"></span>
        </td>
        <td class="text">
            <span>{{id}}</span>
            <input type="hidden"
                   name="{{name}}AccessRights[{{index}}].{{name}}Id"
                   value="{{id}}"
            />
        </td>
        <td class="text">
            <span>{{accessRight}}<span>
            <input type="hidden"
                   name="{{name}}AccessRights[{{index}}].accessRight"
                   value="{{accessRight}}"
            />
        </td>
    </tr>
</script>


<div class="modal fade" id="addFormuser" role="dialog">
    <select id="userSelectOrigin" style="display: none;">
        <#list mgrUsers as mgrUser>
            <option id="${mgrUser.userId}"
                    value="${mgrUser.userId}">${mgrUser.userId!""}</option>
        </#list>
    </select>
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" onclick="return cancel('user');">X</button>
                <span class="modal-title title_popup">
                    <@spring.message code='mgrMenu.addAuthority'/>
                    </span>
            </div>
            <input type="hidden" id="userEditItem" value="0">
            <input type="hidden" id="userIdOld" value="">
            <div class="modal-body">
                <table class="table_form accessRightTable" id="accessRightTableuser">
                    <tr>
                        <td width="50%">${e.get('user.id')}</td>
                        <td>
                            <select id="userSelect">
                                <#list mgrUsers as mgrUser>
                                    <option id="${mgrUser.userId}"
                                            value="${mgrUser.userId}">${mgrUser.userId!""}</option>
                                </#list>
                            </select>
                            <label id="textuser"></label>
                        </td>
                    </tr>
                    <tr>
                        <td width="50%">${e.get('accessRight.name')}</td>
                        <td><#if accessRights??>
                            <ul class="accessRights">
                                <#list accessRights as accessRight>
                                    <li>
                                        <input type="radio" name="accessRightuser" data-name="${accessRight}"
                                               value="${accessRight.value}"> <label
                                        class="permision">${accessRight.title}</label></br>
                                    </li>
                                </#list>
                            </ul>
                        </#if>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="modal-footer modal-footer-popup">
                <button type="button" class="btn btn-default btn_cancel_popup btn_popup" data-dismiss="modal" onclick="return cancel('user');">
                ${e.get('button.cancel')}
                </button>
                <button align="center" colspan="3" class="btn btn-default btn_save_popup btn_popup" id="addRowUser"
                        onclick="editRowNew('user')">
                ${e.get('button.save')}
                </button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="addFormprofile" role="dialog">
    <select id="profileSelectOrigin" style="display: none;">
        <#list mgrProfiles as mgrProfile>
            <option id="${mgrProfile.profileId}"
                    value="${mgrProfile.profileId}">${mgrProfile.profileId!""}</option>
        </#list>
    </select>
    <div class="modal-dialog">
    <#--<div class="overlay" id="addFormprofile" role="dialog">-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" onclick="return cancel('profile');">X</button>
                <span class="modal-title title_popup">
                    <@spring.message code='mgrMenu.addAuthority'/>
                    </span>
            </div>
            <input type="hidden" id="profileEditItem" value="0">
            <input type="hidden" id="profileIdOld" value="">
            <div class="modal-body">
                <table class="table_form accessRightTable" id="accessRightTableprofile">
                    <tr>
                        <td width="50%">${e.get('profile.id')}</td>
                        <td>
                            <select id="profileSelect">
                                <#list mgrProfiles as mgrProfile>
                                    <option id="${mgrProfile.profileId}"
                                            value="${mgrProfile.profileId}">${mgrProfile.profileId!""}</option>
                                </#list>
                            </select>
                            <label id="textprofile"></label>
                        </td>

                    </tr>
                    <tr>
                        <td width="50%">${e.get('accessRight.name')}</td>
                        <td><#if accessRights??>
                            <ul class="accessRights">
                                <#list accessRights as accessRight>
                                    <li>
                                        <input type="radio" name="accessRightprofile" data-name="${accessRight}"
                                               value="${accessRight.value}"> <label
                                        class="permision">${accessRight.title}</label></br>
                                    </li>
                                </#list>
                            </ul>
                        </#if>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="modal-footer modal-footer-popup">
                <button type="button" class="btn btn-default btn_cancel_popup btn_popup" data-dismiss="modal" onclick="return cancel('profile');">
                ${e.get('button.cancel')}
                </button>
                <button align="center" colspan="3" class="btn btn-default btn_save_popup btn_popup" id="addRowUser"
                        onclick="editRowNew('profile')">
                ${e.get('button.save')}
                </button>
            </div>
        </div>
    </div>
</div>
</#macro>

<#macro workItemSearch>

<script id="searchList" type="text/x-handlebars-template">
    <table class="clientSearch table_list search" id="search">

        {{#searchForms}}
        <thead>
        <tr class="table_header">

            <td style="text-align: left; width: 15%; vertical-align: middle;" rowspan="2">
                <button type="button" onclick="openSearchCondition(this, {{this.columns.length}},'NUMBER','workItemId','<@spring.message code='workItem.id'/>','false');"
                        class="icon btn_filter"
                        data-target="#addFormSearchColumn"></button>
                <b><@spring.message code='workItem.id'/></b>
            </td>

            <td  colspan="{{this.columns.length}}">
                <b><@spring.message code='workItem.listField'/></b>
            </td>
        </tr>
        <tr class="table_header">
            {{#each this.columns}}
            <td style="text-align: left">
                <button type="button" onclick="openSearchCondition(this, {{@index}},'{{this.type}}','{{this.name}}','{{this.description}}','{{this.encrypted}}');"
                        class="icon btn_filter"
                        data-target="#addFormSearchColumn"></button>
                <b>{{this.description}}</b>
            </td>
            {{/each}}
        </tr>
        </thead>
        <tbody id="templateDataTbody" class="table_body">
        {{#each this.rows}}
        <tr class="rows">
            <td class="number">{{this.workitemId}}</td>
            {{#each this.columns}}
            <td class="text">{{this.value}}</td>
            {{/each}}
        </tr>
        {{/each}}
        </tbody>
        {{/searchForms}}
    </table>
</script>

<script id="searchWorkItemRows" type="text/x-handlebars-template">
        {{#each workItems}}
            <tr workItem="{{this.workitemId}}">
                <td><input type="checkbox" class="deleteCheckBox"/></td>
                <td class="text_center">
                    <a class="icon icon_edit"
                       href="${rc.getContextPath()}/workItem/showDetail?workItemId={{this.workitemId}}">
                    </a>
                </td>
                <td class="number">{{this.workitemId}}</td>

                <td class="number"> {{#if this.statusLock}}
                    1
                    {{else}}
                    0
                    {{/if}}</td>
                <td class="text">{{this.taskName}}</td>
                <td class="text">{{this.templateId}}</td>
                <td class="text">{{this.templateName}}</td>
            </tr>
        {{/each}}
</script>

<script id="unlockRows" type="text/x-handlebars-template">
    <input type="hidden" name="listIds[{{id}}]" value="{{value}}">
</script>

<script id="selectTemplateTypeOption" type="text/x-handlebars-template">
    <select id="templateId">
        <option selected="selected">-- Please choose --</option>
    {{#each mgrTemplates}}
        <option value="{{this.templateId}}">{{this.templateName}}</option>
    {{/each}}
    </select>
</script>

</#macro>

<#macro searchPopup>
<script id="searchCondition" type="text/x-handlebars-template">
    <tr id="{{index}}_{{idx}}" >
        <td id="num{{index}}_{{idx}}"></td>
        <td class="text" style="white-space: nowrap;">
            {{label}}
            <input type="hidden"
                   value="{{label}}"
                   name="searchByColumnForms[{{idx}}].columnSearchs[{{index}}].label"
                   readonly="readonly">
            <input type="hidden" id="columnNameSearch{{index}}"
                                                      value="{{columnName}}"
                                              name="searchByColumnForms[{{idx}}].columnSearchs[{{index}}].name"
                                              readonly="readonly">
        </td>
        <td id="columnOperator" index="{{index}}" idx="{{idx}}">
            <select id="operator{{index}}_{{idx}}" name="operator_{{idx}}"
                    onchange="changeOperator({{index}},{{idx}});">
                <#list searchOperators as item>
                    <option value="${item}">${item.value!""}</option>
                </#list>
            </select>
            <input type="hidden" name="searchByColumnForms[{{idx}}].columnSearchs[{{index}}].operator" value="EQUAL"
                   id="changeOperator{{index}}_{{idx}}">

        </td>
        <td class="text" id="columnValue" index="{{index}}" idx="{{idx}}">
            <input type="text" onkeyup="addValues(this,{{index}},{{idx}},'{{type}}','{{label}}',{{encrypted}})"
                   onchange="changeValue(this,{{index}},{{idx}},'{{type}}')"
                   id="value_{{index}}_{{idx}}">
            <input type="hidden" name="searchByColumnForms[{{idx}}].columnSearchs[{{index}}].value"
                   id="values_{{index}}_{{idx}}" value="" class="valueInput">
            <input type="hidden" name="searchByColumnForms[{{idx}}].columnSearchs[{{index}}].encrypted"
                   id="encrypted_{{index}}_{{idx}}" value="{{encrypted}}" class="valueInput">
        </td>
        <td id="columnImg{{index}}_{{idx}}">

        </td>
    </tr>
</script>

<script id="addImage" type="text/x-handlebars-template">
    <img id="img{{index}}_{{idx}}"
         src="${rc.getContextPath()}/resources/images/cross-button.png"
         onclick="deleteRowSearch({{index}},{{idx}});">
</script>

<script id="selectValidChild" type="text/x-handlebars-template">
    <select id="valid" onchange="changeValid(this,{{index}});">
        <option value="AND" selected>All of the conditions below are met
        </option>
        <option value="OR">Any of the conditions below are met</option>
    </select>
    <input type="hidden" name="searchByColumnForms[{{index}}].valid" value="AND" id="valid{{index}}">
    <input type="hidden" name="searchByColumnForms[{{index}}].type" value="{{type}}" id="type{{index}}">
</script>

<script id="valueChild" type="text/x-handlebars-template">
    <div id="searchByColumnForm{{idx}}">


    </div>
</script>

<script id="clearChild" type="text/x-handlebars-template">
    <button type="button" class="btn btn-primary pull-left"
            onclick="return clearCondition({{idx}},'{{columnName}}','{{type}}','{{label}}',{{encrypted}});">Clear</button>
</script>

</#macro>
<#macro loginFormPopup>
<script id="loginStatusRow" type="text/x-handlebars-template">
    {{#each ctlLogins}}
    <tr>
        <td class="text">{{this.userId}}</td>
        <td class="text">{{this.loginTime}}</td>
        <td class="text">{{this.logoutTime}}</td>
        <td class="text">{{this.sessionId}}</td>
    </tr>
    {{/each}}
</script>
</#macro>


<#macro searchFormPopup>
<div class="modal fade" id="addFormSearchColumn" role="dialog">
    <div class="modal-dialog content_main">
        <div class="modal-content board-wrapper board-full popup" style="width: 50%;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">X</button>
                <span class="modal-title title_popup">
                    <@spring.message code='mgrMenu.detailSearch'/>
                </span>
            </div>
            <form method="post" id="searchByColumnaaaa">
                <div class="modal-body">
                    <input type="hidden" value="" id="searchByColumnForms">
                    <label>Show Result when:</label> <br/>
                    <div id="selectValid">

                    </div>

                    <table id="conditionSearch" class="clientSearch table_list">
                        <thead>
                        <tr class="table_header">
                            <td>No</td>
                            <td>Column</td>
                            <td>Operator</td>
                            <td>Value</td>
                            <td></td>
                        </tr>
                        </thead>
                        <tbody id="conditionTBody">

                        </tbody>

                    </table>
                </div>
            </form>
            <div class="modal-footer modal-footer-popup">
                <div id="clearButtonFooter">

                </div>
                <button type="button" class="btn btn-default btn_cancel_popup btn_popup" data-dismiss="modal">
                ${e.get('button.cancel')}
                </button>
                <button align="center" colspan="3" class="btn btn-default btn_popup btn btn_save_popup"
                        id="save">
                ${e.get('button.save')}
                </button>

            </div>
        </div>
    </div>
</div>

</#macro>
<#function avg x y>
    <#return (x + y)>
</#function>
<#macro searchColumnsForm>

<form method="post" id="searchByColumnForm" object="searchByColumnForm" style="visibility: hidden;">

    <#if Session.searchByColumnsForm??>
        <#assign searchByColumnsForm = Session.searchByColumnsForm>
    </#if>
    <input type="hidden" name="limit" id="limitCondition" value="${searchByColumnsForm???then(searchByColumnsForm.limit!"","")}">
    <input type="hidden" name="skip" id="skipCondition" value="${searchByColumnsForm???then(searchByColumnsForm.skip!"","")}">
    <input type="hidden" name="templateId" id="templateIdCondition" value="${searchByColumnsForm???then(searchByColumnsForm.templateId!"","")}">
    <input type="hidden" name="templateType" id="templateTypeCondition" value="${searchByColumnsForm???then(searchByColumnsForm.templateType!"","")}">
    <input type="hidden" name="valid" id="validCondition" value="${searchByColumnsForm???then(searchByColumnsForm.valid!"","AND")}">
    <#if searchByColumnsForm??>
        <#if searchByColumnsForm.searchByColumnForms??>
            <#list searchByColumnsForm.searchByColumnForms as searchByColumnForm>
                <div id="searchByColumnForm${searchByColumnForm?index}">
                    <input type="hidden" name="searchByColumnForms[${searchByColumnForm?index}].valid" value="AND" id="valid${searchByColumnForm?index}">
                    <input type="hidden" name="searchByColumnForms[${searchByColumnForm?index}].type" value="NUMBER" id="type${searchByColumnForm?index}">
                    <#if searchByColumnForm??>
                    <table>
                        <tbody id="tBody${searchByColumnForm?index}">
                            <#if searchByColumnForm.columnSearchs??>
                                <#list searchByColumnForm.columnSearchs as columnSearch>

                                <tr id="${columnSearch?index}_${searchByColumnForm?index}">
                                    <td id="num${columnSearch?index}_${searchByColumnForm?index}">
                                    <#assign index =columnSearch?index>
                                    <#assign idx =searchByColumnForm?index>
                                        <#if (columnSearch_has_next)>
                                            ${avg(index, 1)}
                                        <#else>
                                               <#assign str="addValues(this,${index},${idx},'${searchByColumnForm.type}','${columnSearch.label}');">
                                        </#if>
                                    </td>
                                    <td class="text" style="white-space: nowrap;">
                                    ${columnSearch.label!""}
                                    <input type="hidden" id="columnNameSearch ${columnSearch?index}"
                                    value="${columnSearch.label!""}"
                                    name="searchByColumnForms[${searchByColumnForm?index}].columnSearchs[${columnSearch?index}].label" readonly="readonly">
                                    <input type="hidden" id="columnNameSearch ${columnSearch?index}"
                                    value="${columnSearch.name!""}" name="searchByColumnForms[${searchByColumnForm?index}].columnSearchs[${columnSearch?index}].name" readonly="readonly">
                                    </td>
                                    <td id="columnOperator" index="${columnSearch?index}" idx="${searchByColumnForm?index}">
                                    <select id="operator${columnSearch?index}_${searchByColumnForm?index}" name="operator${columnSearch?index}_${searchByColumnForm?index}" onchange="changeOperator(${columnSearch?index},${searchByColumnForm?index});">
                                    <option value="Equal">=</option>
                                    <option value="NotEqual">!=</option>
                                    <option value="Over">&gt;</option>
                                    <option value="EqualOver">&gt;=</option>
                                    <option value="Under">&lt;</option>
                                    <option value="EqualUnder">&lt;=</option>
                                    <option value="Like">like</option>
                                    <option value="NotLike">not like</option>
                                    <option value="IsNull">is null</option>
                                    <option value="IsNotNull">is not null</option>
                                    <option value="IsEmpty">is empty</option>
                                    <option value="IsNotEmpty">is not empty</option>
                                    </select>
                                    <input type="hidden" name="searchByColumnForms[${searchByColumnForm?index}].columnSearchs[${columnSearch?index}].operator" value="${columnSearch.operator!""}" id="changeOperator${columnSearch?index}_${searchByColumnForm?index}">
                                    </td>
                                    <td class="text" id="columnValue" index="${columnSearch?index}" idx="${searchByColumnForm?index}">
                                    <input type="text" onchange="changeValue(this,${columnSearch?index},${searchByColumnForm?index},'${searchByColumnForm.type !""}')"
                                    onkeyup="${str!""}"
                                    id="value_${columnSearch?index}_${searchByColumnForm?index}">
                                    <input type="hidden" name="searchByColumnForms[${searchByColumnForm?index}].columnSearchs[${columnSearch?index}].value" id="values_${columnSearch?index}_${searchByColumnForm?index}" value="${columnSearch.value!""}" class="valueInput">
                                    </td>
                                    <td id="columnImg${columnSearch?index}_${searchByColumnForm?index}">
                                    <#if (columnSearch_has_next)>
                                        <img id="img${columnSearch?index}_${searchByColumnForm?index}" src="/earth/resources/images/cross-button.png" onclick="deleteRowSearch(${columnSearch?index},${searchByColumnForm?index});">
                                    </#if>
                                    </td>
                                </tr>
                                </#list>
                            </#if>

                        </tbody>
                    </table>

                    </#if>
                </div>
            </#list>
        </#if>
    </#if>


</form>

</#macro>

<#macro saveTemplate>
<div class="content_foot">
    <p>
    <div class="center-block">
        <form id="deleteForm" action="deleteList" method="post">
        </form>
        <button class="btn btn_delete" id="deleteButton"><@spring.message code='button.delete'/></button>
         <a class="btn_confirm_delete"><input type="checkbox" class="ckb_confirm_delete" id="deleteConfirm"/><span><@spring.message code='button.confirmDelete'/></span></a>
    </div>
    </p>
</div>
</#macro>