<#include "security.ftl">
<#macro page namePage>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport"
              content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>${namePage}</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
              integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO"
              crossorigin="anonymous">
        <link rel="stylesheet" href="${context.getContextPath()}/assets/style/style.css">

        <link id="contextPath" data-contextPath="${context.getContextPath()}"/>

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="${context.getContextPath()}/assets/js/telegramConnect.js"></script>
    </head>
    <body>
    <#include "mainNavbar.ftl">
    <#nested>
    </body>
    </html>
</#macro>