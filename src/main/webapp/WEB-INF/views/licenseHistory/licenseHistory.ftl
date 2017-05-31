<@standard.standardPage title="License History">

<form id="searchForm" method="post" object="strCals" action="searchLicenseHistory">
	<input type="number" name="limit" placeholder="Limit" value="<#if limit??>${limit}</#if>">
	<input type="number" name="skip" placeholder="Skip" value="<#if skip??>${skip}</#if>">
	<input type="submit" id="searchBtn" value="Search">
</form>
<#if message??>
   <div>
       <b style="color: red;">${message}</b>
   </div>
</#if>
<table border="1">
	<thead>
		<tr>
			<th>区分</th>
			<th>処理時刻</th>
			<th>プロファイルID</th>
			<th>使用可能ライセンス数</th>
			<th>使用ライセンス数</th>
		</tr>
	</thead>
	<tbody>
		<#if strCals??>
	       <#list strCals as strCal>
			   <tr>
			       <td>${strCal.division!""}</td>
			       <td>${strCal.processTime!""}</td>
			       <td>${strCal.profileId!""}</td>
			       <td>${strCal.availableLicenseCount!""}</td>
			       <td>${strCal.useLicenseCount!""}</td>
			   </tr>
	       </#list>
	    <#else>
	       <tr>
	           <td colspan="5">No result</td>
	       </tr>
	   </#if>
	</tbody>
</table>
</@standard.standardPage>