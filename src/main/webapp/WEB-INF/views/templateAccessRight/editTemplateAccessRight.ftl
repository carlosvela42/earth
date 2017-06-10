<#assign contentFooter>
    <@component.detailUpdatePanel object="templateAccessRight" formId="templateAuthorityForm"></@component.detailUpdatePanel>
</#assign>
<#assign addForm>
    <@component.addFormTemplatePanel object="" formId=""></@component.addFormTemplatePanel>
</#assign>
<#assign script>
<script src="${rc.getContextPath()}/resources/js/template.js"></script>
</#assign>
<@standard.standardPage title=e.get("template.authority.setting") contentFooter=contentFooter script=script>

<form method="post" action="${rc.getContextPath()}/templateAccessRight/updateOne" id="templateAuthorityForm" object="templateAuthorityForm">
    <#include "../common/messages.ftl">
    <div class="board-wrapper">
        <div class="board board-half">
            <table class="table_form">
                <tr>
                    <td width="50%">${e.get('template.id')}</td>
                    <td>
                        <input type="text" name="templateId"
                               value="${templateAuthorityForm.templateId!""}" readonly>
                    </td>
                </tr>
                <tr>
                    <td width="50%">${e.get('template.name')}</td>
                    <td>
                        <input type="text" name="templateName"
                               value="${templateAuthorityForm.templateName!""}" readonly>
                    </td>
                </tr>
            </table>
        </div>
        <div class="board-split"></div>
        <div class="board board-half">
            <div id="tabs" class="container">
                <div class="panel with-nav-tabs panel-default">
                    <div class="panel-heading">
                        <ul class="nav nav-tabs">
                            <li class="active"><a data-toggle="tab" href="#tabs-user">${e.get('user.id')}</a></li>
                            <li><a data-toggle="tab" href="#tabs-profile">${e.get('profile.id')}</a></li>
                        </ul>
                    </div>
                    <div class="panel-body">
                        <div class="tab-content">
                            <div id="tabs-user" class="tab-pane fade in active">
                                <button type="button" class="btn btn_remove" id="deleteButton"
                                        onclick="return delRow('user');">
                                    <@spring.message code='button.delete'/></button>
                                <div style="height: 10px;"></div>
                                <table class="clientSearch table_list userAccessRightTable">
                                    <thead>
                                    <tr class="table_header">
                                        <td><input type="checkbox" name="userRightTop" class="deleteAllCheckBox"></td>
                                        <td class="text_center">
                                            <button type="button" class="icon btn_add" id="addUser"
                                                    data-target="#addFormuser"></button>
                                        </td>
                                        <td style="width: 40%">${e.get('user.id')}</td>
                                        <td>${e.get('accessRight.name')}</td>
                                    </tr>
                                    <tr class="condition">
                                        <td><img src="${rc.getContextPath()}/resources/images/search.png"/></td>
                                        <td colspan="2"><input type="text" col="3" placeholder="Search">
                                        </td>
                                        <td><input type="text" col="4" placeholder="Search"></td>
                                    </tr>
                                    </thead>
                                    <#assign index=(templateAuthorityForm.userAccessRights??)?then
                                    (templateAuthorityForm.userAccessRights?size,0)>
                                    <tbody id="userTbody" class="table_body" index="${index}">
                                        <#if templateAuthorityForm.userAccessRights??>
                                            <#list templateAuthorityForm.userAccessRights as userAccessRight>
                                            <tr userId="${userAccessRight.userId}">
                                                <td><input type="checkbox" class="deleteCheckBox"/></td>
                                                <td class="text_center">
                                                     <span class="icon icon_edit"
                                                           onclick="editRow('user','${userAccessRight.userId!""}', '${userAccessRight.accessRight!""}');">
                                                     </span>
                                                </td>
                                                <td class="text">
                                                    <span> ${userAccessRight.userId!""}</span>
                                                        <input type="hidden"
                                                               name="userAccessRights[${userAccessRight?index}].userId"
                                                               value="${userAccessRight.userId!""}"
                                                        />
                                                </td>
                                                <td class="text" >
                                                    <span> ${userAccessRight.accessRight!""}</span>
                                                        <input type="hidden"
                                                               name="userAccessRights[${userAccessRight?index}].accessRight"
                                                               value="${userAccessRight.accessRight!""}"
                                                        />
                                                </td>
                                            </tr>
                                            </#list>
                                        </#if>
                                    </tbody>
                                </table>
                            </div>
                            <div id="tabs-profile" class="tab-pane fade">
                                <button type="button" class="btn btn_remove"
                                        id="deleteButton"  onclick="return delRow('profile');">
                                    <@spring.message code='button.delete'/></button>
                                <div style="height: 10px;"></div>
                                <table class="clientSearch table_list profileAccessRightTable">
                                    <thead>
                                    <tr class="table_header">
                                        <td><input type="checkbox" name="userRightTop" class="deleteAllCheckBox"></td>
                                        <td>
                                            <button type="button" class="icon btn_add" id="addProfile"
                                                    data-target="#addFormprofile"></button>
                                        </td>
                                        <td style="width: 40%">${e.get('profile.id')}</td>
                                        <td>${e.get('accessRight.name')}</td>
                                    </tr>
                                    <tr class="condition">
                                        <td><img src="${rc.getContextPath()}/resources/images/search.png"/></td>
                                        <td colspan="2"><input type="text" col="3" placeholder="Search ">
                                        </td>
                                        <td><input type="text" col="4" placeholder="Search "></td>
                                    </tr>
                                    </thead>
                                    <#assign index1=(templateAuthorityForm.profileAccessRights??)?then(templateAuthorityForm.profileAccessRights?size,0)>
                                    <tbody id="profileTbody" class="table_body" index="${index1}">

                                        <#if templateAuthorityForm.profileAccessRights??>
                                            <#list templateAuthorityForm.profileAccessRights as profileAccessRight>
                                            <tr profileId="${profileAccessRight.profileId}">
                                                <td><input type="checkbox" name="profileRight" class="deleteCheckBox">
                                                </td>
                                                <td class="text_center">
                                                     <span class="icon icon_edit"
                                                           onclick="editRow('profile','${profileAccessRight.profileId!""}', '${profileAccessRight.accessRight!""}');">
                                                    </span>
                                                    <#--<a class="icon icon_edit"-->
                                                       <#--onclick="editRow('profile','${profileAccessRight.profileId!""}', '${profileAccessRight.accessRight!""}');"-->
                                                       <#--href="#addFormprofileTemplate">-->
                                                    <#--</a>-->
                                                </td>
                                                <td type="text">
                                                <span>${profileAccessRight.profileId!""}</span><input type="hidden"
                                                        name="profileAccessRights[${profileAccessRight?index}].profileId"
                                                                       value="${profileAccessRight.profileId!""}"/>
                                                </td>
                                                <td type="text">
                                                <span>${profileAccessRight.accessRight!""}</span><input type="hidden"
                                                                                                           name="profileAccessRights[${profileAccessRight?index}].accessRight" value="${profileAccessRight.accessRight!""}"
                                                        readonly/></td>
                                            </tr>
                                            </#list>
                                        </#if>
                                    </tbody>
                                </table>
                            </div>

                        </div>
                    </div>

                </div>

            </div>
        </div>
    </div>

</form>
${addForm}
</@standard.standardPage>