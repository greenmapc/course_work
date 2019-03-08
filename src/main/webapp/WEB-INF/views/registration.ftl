<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Registration</title>
    <link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet">
    <link rel="stylesheet" href="${context.getContextPath()}/assets/style/style.css">
</head>

<body class="align">
<div class="grid">
    <form method="post" class="form login" action="${context.getContextPath()}/registration">
        <header class="login__header">
            <span>Registration</span>
        </header>
        <div class="login__body">
            <div class="form__field">
                <input id="firstName" name="firstName" type="text" placeholder="First name" required>
            </div>
            <div class="form__field">
                <input id="lastName" name="lastName" type="text" placeholder="Last name" required>
            </div>
            <div class="form__field">
                <input id="username" name="username" type="text" placeholder="Username" required>
            </div>
            <div class="form__field">
                <input type="password" name="password" placeholder="Password" required>
            </div>
        </div>
        <footer class="login__footer">
            <input type="submit" value="Sign Up">
            <div class="ref_oth_pg"><a href="${context.getContextPath()}/login">Sign In</a></div>
        </footer>
    </form>
</div>
</body>
</html>
