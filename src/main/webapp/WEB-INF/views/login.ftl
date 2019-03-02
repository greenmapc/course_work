<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>
<h3>Вход на сайт</h3>
<div class="container">
    <form action="/login" method="post">
        <div><label> User Name: <input type="text"  name="username"/> </label></div>
        <div><label> Password: <input type="password"  name="password"/> </label></div>
        <div><input type="submit" value="Sign In"/></div>
    </form>
    <a href="/registration">Sign up</a>

</div>

<div class="auth link">
    <form style="width: 100%" action="/gitAuth">
        <button type="submit">
            <img alt="github" src="https://camo.githubusercontent.com/7710b43d0476b6f6d4b4b2865e35c108f69991f3/68747470733a2f2f7777772e69636f6e66696e6465722e636f6d2f646174612f69636f6e732f6f637469636f6e732f313032342f6d61726b2d6769746875622d3235362e706e67" style="width: 100%">
        </button>
    </form>
</div>
<#if email??><p>${email}</p></#if>
<#if username??><p>${username}</p></#if>
</body>
</html>
