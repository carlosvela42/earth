<#ftl encoding='UTF-8'>
<#macro standardPage title="">
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Earth</title>
</head>
<body style="text-align: center;width: 100%;;height:100%;">
<div class="div_page">
    <table>
        <tr style="text-align: center">
            <td colspan="2">
                <#include "header.ftl">
            </td>
        </tr>
        <tr style="text-align: center">
            <td style="width: 30%">
                <#include "*/menu/menu.ftl">
            </td>
            <td style="width: 70%;padding-left: 20px;padding-right: 20px;">
                <#nested/>
            </td>
        </tr>
        <tr style="text-align: center">
            <td colspan="2">
                <#include "footer.ftl">
            </td>
        </tr>
    </table>
</div>
</body>
</body>
</html>
</#macro>