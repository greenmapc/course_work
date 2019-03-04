<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>OauthLogin</title>
</head>
<body>
<h3>Login with:</h3>
<#list urls as url>
<p>
    <a title="${url.key}" href="${url.value}">Client</a>
</p>
</#list>
</body>
</html>