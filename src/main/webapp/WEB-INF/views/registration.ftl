<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Registration</title>
</head>
<body>
<h3>Регистрация</h3>
<div class="container">
    <#if message??><p>${message}</p></#if>
    <form action="/registration" method="post">
        <input id="firstName" name="firstName" type="text" placeholder="First name">
        <br>
        <input id="lastName" name="lastName" type="text" placeholder="Last name">
        <br>
        <input id="username" name="username" type="text" placeholder="Username">
        <br>
        <input id="password" name="password" type="password" placeholder="Password">
        <br>
        <button type="submit">Register</button>
    </form>
    <a href="/login">Sign in</a>
</div>
</body>
</html>
