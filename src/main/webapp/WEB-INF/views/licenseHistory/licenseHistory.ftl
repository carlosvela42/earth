<script type="text/javascript" src="../resources/js/jquery.min.js"></script>
<script type="text/javascript"
	src="../resources/js/handlebars-v4.0.5.js"></script>


<form id="searchForm" method="post" object="strCals" action="searchLicenseHistory">
	<label>処理時刻</label> <input type="text" name="fromDate" id="fromDateId" value="<#if fromDate??>${fromDate}</#if>">
	<label>処理時刻</label> <input type="text" name="toDate" id="toDateId" value="<#if toDate??>${toDate}</#if>">
	<input type="submit" id="searchBtn" value="Search">
</form>
<#if message??>${message}</#if>
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
			       <td>
			         <#if strCal.division??>
			             ${strCal.division}
			         </#if>
			       </td>
			       <td><#if strCal.processTime??>${strCal.processTime}</#if></td>
			       <td><#if strCal.profileId??>${strCal.profileId}</#if></td>
			       <td><#if strCal.availableLicenseCount??>${strCal.availableLicenseCount}</#if></td>
			       <td><#if strCal.useLicenseCount??>${strCal.useLicenseCount}</#if></td>
			   </tr>
	       </#list>
	    <#else>
	       <tr>
	           <td colspan="5">Khong co du lieu nao</td>
	       </tr>
	   </#if>
	</tbody>
</table>