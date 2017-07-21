<#assign contentFooter>
<@component.removePanel></@component.removePanel>
</#assign>

<#assign script>
  <script src="${rc.getContextPath()}/resources/js/profile.js"></script>
</#assign>

<@standard.standardPage title=e.get('profile.list') imageLink="user" contentFooter=contentFooter displayWorkspace=false
script=script>
<div class="board-wrapper">
    <div class="board board-half">
     <#include "../common/messages.ftl">
    <form action="${rc.getContextPath()}/profile/" id="siteSearchForm" object="searchForm" method="post">
            <table class="clientSearch table_list" >
               <thead>               
                    <tr class="table_header">
                        <td class=""  width="40px" ><input type="checkbox" class="deleteAllCheckBox"/></td>
                        <td class="text_center"  width="40px">
                            <a href="#" id="addButton" class="icon icon_add"></a>
                        </td>
                        <td  width="45%">${e.get('profile.id')}</td>
                        <td width="45%">${e.get('profile.description')}</td>
                    </tr>
                    <tr class="condition" >
                        <td  colspan="3">
                            <input id="searchColumns[0]" name="searchColumns[0]"
                                   value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[0]!"",""),"")}"
                                 type="text" placeholder="search" class="searchInput" col="3"></td>
                        <td>
                            <input id="searchColumns[1]" name="searchColumns[1]"
                                   value="${searchForm???then(searchForm.searchColumns???then(searchForm.searchColumns[1]!"",""),"")}"
                                 type="text" placeholder="search" class="searchInput" col="4">
                        </td>
                    </tr>
                </thead>
                <tbody id="profileTbody" class="table_body">
                <#if mgrProfiles??>
                    <#list mgrProfiles as mgrProfile>
                        <tr profileId="${mgrProfile.profileId}">
                             <td><input type="checkbox" class="deleteCheckBox" /></td>
                             <td class="text_center text_icon">
                                <a class="icon icon_edit editButton" href="#" data-id="${mgrProfile.profileId}"></a>
                             </td>         
                            <td class="text">${mgrProfile.profileId!""}</a></td>
                            <td class="text"  colspan="2">${mgrProfile.description!""}</td>
                        </tr>
                    </#list>
                <#else>
                    <tr>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td  colspan="2"></td>
                    </tr>
                </#if>
              </tbody>
            </table>           
            </form>
    </div>
     <div class="board-split"></div>
</div>
  </@standard.standardPage>