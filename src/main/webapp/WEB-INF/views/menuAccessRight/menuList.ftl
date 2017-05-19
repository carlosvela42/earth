<@standard.standardPage title="LIST MENU">
<script>
    function filter() {
        // Declare variables
        var pIdInput, pNameInput, pParentIdInput, pParentNameInput, filter, i;
        pIdInput = document.getElementById('idInput').value
                .toUpperCase();
        pNameInput = document.getElementById('nameInput').value
                .toUpperCase();
        pParentIdInput = document.getElementById('parentIdInput').value
                .toUpperCase();
        pParentNameInput = document.getElementById('parentNameInput').value
                .toUpperCase();

        // Loop through all list items, and hide those who don't match the search query
        for (i = 0; i < $("#menuList tr ").length - 2; i++) {

            if (($("#functionId" + i).html().toUpperCase()
                            .indexOf(pIdInput) > -1)
                    && ($("#functionName" + i).html().toUpperCase().indexOf(
                            pNameInput) > -1)&& ($("#functionCategoryId" + i).html().toUpperCase().indexOf(
                            pParentIdInput) > -1)&& ($("#functionCategoryName" + i).html().toUpperCase().indexOf(
                            pParentNameInput) > -1)) {
                $("#row" + i).show();

            } else {
                $("#row" + i).hide();
            }
        }
    }
</script>
<form object="mgrMenus" method="post" style="text-align: left;" class="form-narrow form-horizontal">
    <label>メニュー一覧</label>
    <table border="1" style="text-align: left;" id="menuList">
        <tr>
            <th colspan="2">機能ID</th>
            <th>機能名</th>
            <th>機能分類ID</th>
            <th>機能分類名</th>
        </tr>
        <tr style="height: 25px; white-space: nowrap;">
            <td colspan="2"><input
                    type="text" id="idInput" onkeyup="filter()"
                    placeholder="Search for ID.."></td>
            <td><input type="text" id="nameInput" onkeyup="filter()"
                       placeholder="Search for name.."></td>
            <td><input type="text" id="parentIdInput" onkeyup="filter()"
                       placeholder="Search for ID.."></td>
            <td><input type="text" id="parentNameInput" onkeyup="filter()"
                       placeholder="Search for name.."></td>
        </tr>
        <#if mgrMenus??>
            <#list mgrMenus as mgrMenu>
                <tr id="row${mgrMenu?index}">
                    <td style="text-align: center;">
                        <a href="${rc.getContextPath()}/menuAccessRight/showDetail?functionId=${mgrMenu.functionId}">
                        <img src="${rc.getContextPath()}/resources/images/edit.png" width="20">
                    </a></td>
                    <td style="text-align: right;" id="functionId${mgrMenu?index}">${mgrMenu.functionId}</td>
                    <td style="text-align: left;" id="functionName${mgrMenu?index}">${mgrMenu.functionName!""}</td>
                    <td style="text-align: right;"
                        id="functionCategoryId${mgrMenu?index}">${mgrMenu.functionCategoryId!""}</td>
                    <td style="text-align: left;"
                        id="functionCategoryName${mgrMenu?index}">${mgrMenu.functionCategoryName!""}</td>
                </tr>
            </#list>
        <#else>
            <tr>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
        </#if>

    </table>
</form>
</@standard.standardPage>