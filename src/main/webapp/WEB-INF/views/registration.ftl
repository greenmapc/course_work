<#import "parts/common.ftl" as c>
<#import "/spring.ftl" as spring/>

<@c.page "Registration">
<#--<body class="align">-->
    <div class="grid">


        <form method="post" class="form login" action="${context.getContextPath()}/registration">
            <@spring.bind "form"/>
            <header class="login__header">
                <span>Registration</span>
            </header>
            <div class="login__body">
                <div class="form__field">
                    <@spring.formInput "form.firstName" "placeholder='First name'"/>
                </div>
                <div class="form__field">
                    <@spring.formInput "form.lastName" "placeholder='Last name'"/>
                </div>
                <div class="form__field">
                    <@spring.formInput "form.username" "placeholder='Username'"/>
                </div>
                <div class="form__field">
                    <@spring.formPasswordInput "form.password" "placeholder='Password required'"/>
                </div>
                <div class="form__field">
                    <@spring.formPasswordInput "form.repeatPassword" "placeholder='Repeat password'"/>
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
