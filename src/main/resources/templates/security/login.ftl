<#import "../parts/common.ftl" as c>
<@c.page "Log in">
    <div class="align">
        <div class="grid">
            <form method="post" class="form login" action="/login">
                <header class="login__header">
                    <span>Sign In</span>
                    <a href="https://github.com/MerenovaAnastasiya">
                        <div class="git_img"><img
                                    src="https://github.githubassets.com/images/modules/logos_page/Octocat.png"/></div>
                    </a>
                </header>
                <div class="login__body">
                    <div class="form__field">
                        <input id="username" name="username" type="text" placeholder="Username" required>
                    </div>
                    <div class="form__field">
                        <input type="password" name="password" placeholder="Password" required>
                    </div>
                </div>
                <footer class="login__footer">
                    <input type="submit" value="Sign In">
                    <div class="ref_oth_pg">
                        <a href="/registration">Sign Up</a>
                    </div>
                </footer>
            </form>
        </div>
    </div>
</@c.page>