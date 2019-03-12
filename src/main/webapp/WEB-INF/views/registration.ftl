<#import "parts/common.ftl" as c>

<@c.page "Registration">
<#--<body class="align">-->
    <div class="grid">
        <form method="post" class="form login" action="${context.getContextPath()}/registration">
            <header class="login__header">
                <span>Registration</span>
            </header>
            <div class="login__body">
                <div class="form__field">
                    <input id="firstName" name="firstName" type="text" placeholder="First name"
                           value="<#if user??>${user.firstName}</#if>" required>
                </div>
                <div class="form__field">
                    <input id="lastName" name="lastName" type="text" placeholder="Last name"
                           value="<#if user??>${user.lastName}</#if>" required>
                </div>
                <div class="form__field">
                    <input id="username" name="username" type="text" placeholder="Username"
                           value="<#if user??>${user.username}</#if>" required>
                </div>
                <div class="form__field">
                    <input type="password" name="password" placeholder="Password" required>
                </div>
                <div class="form__field">
                    <input type="password" name="password2" placeholder="Repeat password" required>
                </div>
            </div>
            <footer class="login__footer">
                <input type="submit" value="Sign Up">
                <div class="ref_oth_pg"><a href="${context.getContextPath()}/login">Sign In</a></div>
            </footer>
        </form>
    </div>
<#--</body>-->
</@c.page>
