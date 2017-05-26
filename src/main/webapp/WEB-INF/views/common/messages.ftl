<#if messages??>
  <#list messages as message>
    <div>
        <b style="color: red;">${message.getContent()}</b>
    </div>
  </#list>
</#if>