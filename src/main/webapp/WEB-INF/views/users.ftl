<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Users</title>
</head>
<body>
<h3>Users list:</h3>
<form action="/logout" method="post">
    <button type="submit">Log out</button>
</form>
<div class="container">
    <p>You are in the system</p>
    <#list users as user>
        <span>${user.username}</span>
    </#list>
</div>
</body>
</html>