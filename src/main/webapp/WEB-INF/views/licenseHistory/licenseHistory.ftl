<script type="text/javascript" src="../resources/js/jquery.min.js"></script>


<form id="searchForm" method="post" object="strCals" action="searchLicenseHistory">
	<label>蜃ｦ逅�譎ょ綾</label> <input type="text" name="fromDate" id="fromDateId" value="<#if fromDate??>${fromDate}</#if>">
	<label>蜃ｦ逅�譎ょ綾</label> <input type="text" name="toDate" id="toDateId" value="<#if toDate??>${toDate}</#if>">
	<input type="submit" id="searchBtn" value="Search">
</form>
<#if message??>${message}</#if>
<table border="1">
	<thead>
		<tr>
			<th>蛹ｺ蛻�</th>
			<th>蜃ｦ逅�譎ょ綾</th>
			<th>繝励Ο繝輔ぃ繧､繝ｫID</th>
			<th>菴ｿ逕ｨ蜿ｯ閭ｽ繝ｩ繧､繧ｻ繝ｳ繧ｹ謨ｰ</th>
			<th>菴ｿ逕ｨ繝ｩ繧､繧ｻ繝ｳ繧ｹ謨ｰ</th>
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