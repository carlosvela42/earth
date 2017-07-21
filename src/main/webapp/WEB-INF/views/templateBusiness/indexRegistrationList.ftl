<link rel="stylesheet" media="screen"
      href="${rc.getContextPath()}/resources/css/templatebusiness.css" />

<@standard.standardPage title=e.get('templateBusiness.indexWaitSearchScreen') contentFooter=contentFooter script=script>

<div class="board-wrapper">
    <div class="board board-half">
     <#include "../common/messages.ftl"> 
     <span class="span_label">${e.get('templateBusiness.message')}</span>
     <div class="createline"></div>
     <p class="text_round_border" style="height: 100px; margin-bottom: 10px;">${e.get('templateBusiness.newDocument')
}</p>
     <div></div>    
     <span class="span_label" >${e.get('templateBusiness.seeTheCase')}</span> 
     <div class="span_label"></div>
     <table class="clientSearch table_list_template_business" >
     <thead>
            <tr class="table_header">               
                <td class="text_center">${e.get('templateBusiness.index')}</td>               
                <td class="text_center">${e.get('templateBusiness.caseManager')}</td>               
                <td class="text_center">${e.get('templateBusiness.scanDateAndTime')}</td>               
                <td style="width: 100px;" class="text_center">${e.get('templateBusiness.pageNumber')}</td>               
            </tr>
            <tr class="condition">               
               <td><input class="icon icon_search" type="text" onkeyup="filter()"  placeholder="Search..."></td>
               <td><input class="icon icon_search" type="text" onkeyup="filter()" placeholder="Search..."></td>
               <td><input class="icon icon_search" type="text" onkeyup="filter()" placeholder="Search..."></td>
               <td><input class="icon icon_search" type="text" onkeyup="filter()"  placeholder="Search..."></td>
            </tr>
            </thead>
            <tbody id="profileTbody" class="table_body">
                 <tr>
                        <td class="text_center">1</td>
                        <td class="text_center"><a href="indexRegistration" style="color: hotpink;"> 48G7-PC-02YBPM01</a></td>
                        <td class="text_center">2017/04/01 10:15:02</td>
                        <td class="text_center">10</td>
                 </tr>
                  <tr>
                        <td class="text_center">2</td>
                        <td class="text_center">48G7-PC-02YBPM01</td>
                        <td class="text_center">2017/04/01 10:15:02</td>
                        <td class="text_center">15</td>
                 </tr>
                  <tr>
                        <td class="text_center">3</td>
                        <td class="text_center">48G7-PC-02YBPM01</td>
                        <td class="text_center">2017/04/01 10:15:02</td>
                        <td class="text_center">8</td>
                 </tr>
                  <tr>
                        <td class="text_center">4</td>
                        <td class="text_center">48G7-PC-02YBPM01</td>
                        <td class="text_center">2017/04/01 10:15:02</td>
                        <td class="text_center">14</td>
                 </tr>
                 <tr>
                        <td class="text_center">5</td>
                        <td class="text_center">48G7-PC-02YBPM01</td>
                        <td class="text_center">2017/04/01 10:15:02</td>
                        <td class="text_center">19</td>
                 </tr>     
              </tbody>
     </table>
    </div>
</div>
</@standard.standardPage>