<#assign contentFooter>
<@component.removePanel></@component.removePanel>
</#assign>

<#assign script>
  <script src="${rc.getContextPath()}/resources/js/profile.js"></script>
</#assign>

<@standard.standardPage title=e.get('profile.list') contentFooter=contentFooter displayWorkspace=false script=script>
 <#include "../common/messages.ftl">
    <div class="board board-half">
            <table class="clientSearch table_list" >
               <thead>               
                    <tr class="table_header">
                        <td class=""><input type="checkbox" class="deleteAllCheckBox" /></td>
                        <td class="text_center">
                            <a href="${rc.getContextPath()}/profile/addNew" class="icon icon_add"></a>
                        </td>             
                        <td>${e.get('profile.id')}</td>
                        <td>${e.get('profile.description')}</td>
                    </tr>
                    <tr class="condition" >
                      <td><img src="${rc.getContextPath()}/resources/images/search.png"/></td>
                        <td colspan="2"><input type="text" col="3" onkeyup="filter()" placeholder="Search for ID.."></td>
                        <td><input type="text" col="4" onkeyup="filter()" placeholder="Search for description.."></td>
                    </tr>
                </thead>
                <tbody id="profileTbody" class="table_body">
                <#if mgrProfiles??>
                    <#list mgrProfiles as mgrProfile>
                        <tr profileId="${mgrProfile.profileId}">
                             <td><input type="checkbox" class="deleteCheckBox" /></td>
                             <td class="text_center text_icon">
                                <a class="icon icon_edit" href="${rc.getContextPath()}/profile/showDetail?profileId=${mgrProfile.profileId}"></a>
                             </td>         
                            <td class="text">${mgrProfile.profileId!""}</a></td>
                            <td class="text">${mgrProfile.description!""}</td>
                        </tr>
                    </#list>
                <#else>
                    <tr>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                    </tr>
                </#if>
              </tbody>
            </table>           
    </div>
  </@standard.standardPage>