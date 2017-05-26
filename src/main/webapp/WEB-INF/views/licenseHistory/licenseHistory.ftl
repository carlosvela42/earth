<@standard.standardPage title="License History">

<form id="searchForm" method="post" object="strCals"
	action="searchLicenseHistory">
	<label>処理時刻From</label> <input type="text" name="fromDate"
		id="fromDateId" value="<#if fromDate??>${fromDate}</#if>"> <label>処理時刻To</label>
	<input type="text" name="toDate" id="toDateId"
		value="<#if toDate??>${toDate}</#if>"> <input type="submit"
		id="searchBtn" value="Search">
</form>
<#if messages??> <#list messages as message>
                        <div>
                            <b style="color: red;">${message.getContent()}</b>
                        </div> </#list> </#if>
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
	           <td colspan="5">Khong co du lieu nao</td>
	       </tr>
	   </#if>
	</tbody>
</table>
</@standard.standardPage>