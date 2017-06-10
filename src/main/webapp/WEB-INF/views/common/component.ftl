<#macro removePanel>
<div class="content_foot">
    <p>
    <div class="center-block">
        <form id="deleteForm" action="deleteList" method="post">
        </form>
        <button class="btn btn_delete" id="deleteButton"><@spring.message code='button.delete'/></button>
        <span class="btn">
                <input type="checkbox" id="deleteConfirm"/>
            <@spring.message code='button.confirmDelete'/>
            </span>
    </div>
    <!-- <img src="${rc.getContextPath()}/resources/images/04/03.png" alt="03" width="1250" height="80" /> -->
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


<#macro searchFormPanel object="" formId="">

<div class="center-search">
    <form method="get" object="searchForm" action="${rc.getContextPath()}/${object}/"
          class="form-search">
        <#if searchForm??>
            <#assign limit=searchForm.limit!"">
            <#assign skip=searchForm.skip!"">
        </#if>
        <input type="number" name="limit" class="radius textbox" placeholder="Limit" value="${limit!""}" autofocus>
        <input type="number" name="skip" class="radius textbox" placeholder="Skip" value="${skip!""}">
        <button class="btn searchBtn icon_search" id="searchButton"
                data-form_id="${formId}"><img src="${rc.getContextPath()
        }/resources/images/search.png"><@spring.message code='button.search'/></button>
    </form>
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
    <div class="modal-dialog">
    <#--<div class="overlay" id="addFormuser" role="dialog">-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" onclick="return cancel('user');">X</button>
                <span class="modal-title title_popup">
                ${e.get('mgrMenu.addAuthority')}
                    </span>
            </div>
            <input type="hidden" id="userEditItem" value="0">
            <input type="hidden" id="userIdOld" value="">
            <div class="modal-body">
                <table class="table_form">
                    <tr>
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
                <button type="button" class="btn btn-default btn_cancel_popup btn_popup" data-dismiss="modal">
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
    <div class="modal-dialog">
    <#--<div class="overlay" id="addFormprofile" role="dialog">-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" onclick="return cancel('profile');">
                    X
                </button>
                <span class="modal-title title_popup">
                ${e.get('mgrMenu.addAuthority')}
                    </span>
            </div>
            <input type="hidden" id="profileEditItem" value="0">
            <input type="hidden" id="profileIdOld" value="">
            <div class="modal-body">
                <table class="table_form">
                    <tr>
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
                <button type="button" class="btn btn-default btn_cancel_popup btn_popup" data-dismiss="modal">
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
            <span class="icon icon_edit" onclick="editRow('{{name}}','{{id}}','{{accessRight}}');"></span>
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
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" onclick="return cancel('user');">X</button>
                <span class="modal-title title_popup">
                ${e.get('mgrMenu.addAuthority')}
                    </span>
            </div>
            <input type="hidden" id="userEditItem" value="0">
            <input type="hidden" id="userIdOld" value="">
            <div class="modal-body">
                <table class="table_form accessRightTable">
                    <tr>
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
                    <tr>
                        <td width="50%">${e.get('accessRight.name')}</td>
                        <td><#if accessRights??>
                            <ul class="accessRights">
                                <#list accessRights as accessRight>
                                    <li>
                                        <input type="radio" name="accessRightuser" data-name="${accessRight}"
                                               value="${accessRight.value}"> <label
                                            class="permision">${accessRight}</label></br>
                                    </li>
                                </#list>
                            </ul>
                        </#if>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="modal-footer modal-footer-popup">
                <button type="button" class="btn btn-default btn_cancel_popup btn_popup" data-dismiss="modal">
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
    <div class="modal-dialog">
    <#--<div class="overlay" id="addFormprofile" role="dialog">-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" onclick="return cancel('profile');">X</button>
                <span class="modal-title title_popup">
                ${e.get('mgrMenu.addAuthority')}
                    </span>
            </div>
            <input type="hidden" id="profileEditItem" value="0">
            <input type="hidden" id="profileIdOld" value="">
            <div class="modal-body">
                <table class="table_form accessRightTable">
                    <tr>
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
                    <tr>
                        <td width="50%">${e.get('accessRight.name')}</td>
                        <td><#if accessRights??>
                            <ul class="accessRights">
                                <#list accessRights as accessRight>
                                    <li>
                                        <input type="radio" name="accessRightprofile" data-name="${accessRight}"
                                               value="${accessRight.value}"> <label
                                            class="permision">${accessRight}</label></br>
                                    </li>
                                </#list>
                            </ul>
                        </#if>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="modal-footer modal-footer-popup">
                <button type="button" class="btn btn-default btn_cancel_popup btn_popup" data-dismiss="modal">
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