<script id="message-template" type="text/x-handlebars-template">
    <div class="entry">
        <b data-code="{{code}}" style="color: red;">{{message}}</b>
    </div>
</script>

<div id="messages">
  <#if messages??>
    <#list messages as message>
        <div>
            <br>
            <b data-code="${(message.getCode()!"")}" style="color: red;">${message.getContent()}</b>
        </div>
    </#list>
  </#if>
</div>
