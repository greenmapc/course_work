<#import "../parts/common.ftl" as c>
<#import "/spring.ftl" as spring/>

<@c.page "Registration">
<div class="align">
    <div class="grid">
        <form method="post" class="form login" action="${context.getContextPath()}/registration">
            <@spring.bind "form"/>
            <header class="login__header">
                <span>Registration</span>
            </header>

            <div class="login__body">
                <div class="form__field">
                    <@spring.formInput "form.firstName" "placeholder='First name'"/>
                    <@spring.showErrors "form.firstName"/>
                </div>
                <div class="form__field">
                    <@spring.formInput "form.lastName" "placeholder='Last name'"/>
                    <@spring.showErrors "form.lastName"/>
                </div>
                <div class="form__field">
                    <@spring.formInput "form.username" "placeholder='Username'"/>
                    <@spring.showErrors "form.username"/>
                </div>
                <div class="form__field">
                    <@spring.formPasswordInput "form.password" "placeholder='Password required'"/>
                    <@spring.showErrors "form.password"/>
                </div>
                <div class="form__field">
                    <@spring.formPasswordInput "form.repeatPassword" "placeholder='Repeat password'"/>
                    <@spring.showErrors "form.repeatPassword"/>
                </div>
            </div>
            <footer class="login__footer">
                <input type="submit" value="Sign Up">
                <div class="ref_oth_pg"><a href="${context.getContextPath()}/login">Sign In</a></div>
            </footer>
        </form>
    </div>
</div>
</@c.page>
