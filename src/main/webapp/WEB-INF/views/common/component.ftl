<#macro removePanel>
  <div class="content_foot">
      <p>
          <div class="center-block">
            <button class="btn" id="deleteButton"><@spring.message code='button.delete'/></button>
            <span class="btn">
                <input type="checkbox" id="deleteConfirm"/>
                <@spring.message code='button.confirmDelete'/>
            </span>
          </div>
          <#--<img src="${rc.getContextPath()}/resources/images/04/03.png" alt="03" width="1250" height="80" />-->
      </p>
  </div>
</#macro>

<#macro detailUpdatePanel object="" formId="">
  <div class="content_foot">
      <div class="center-block">
          <form action="${rc.getContextPath()}/${object}/cancel" class="form-cancel" method="post">
              <button class="btn" id="cancelButton"><@spring.message code='button.cancel'/></button>
          </form>
          <button class="btn" id="saveButton" data-form_id="${formId}"><@spring.message code='button.save'/></button>
          <#--<span class="btn">-->
                <#--<input type="checkbox" id="deleteConfirm"/>-->
            <#--<@spring.message code='button.confirmDelete'/>-->
            <#--</span>-->
      </div>
  </div>
</#macro>