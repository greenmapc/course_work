<#macro page namePage>
    <#include "security.ftl">
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>${namePage}</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
              integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO"
              crossorigin="anonymous">
        <link rel="stylesheet" href="${context.getContextPath()}/assets/style/style.css">
    </head>
    <body>
    <#nested>
    </body>
    </html>
</#macro>