<div class="nav">
    <div class="inner">
    </div>
    <div class="container navbar-container ">
        <nav class="navbar navbar-expand navbar-light pl-0">
            <a class="navbar-brand" href="#">Navbar</a>
            <ul class="navbar-nav mr-auto">
                <li class="nav-item">
                    <a class="nav-link" href="#">Link1</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">Link2</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">Link3</a>
                </li>
                <#if isAdmin>
                    <li class="nav-item">
                        <a class="nav-link" href="${context.getContextPath()}/user">User list</a>
                    </li>
                </#if>
            </ul>
            <form class="form-inline my-2 my-lg-0" method="post" action="${context.getContextPath()}/logout">
                <input type="submit" class="logOutSubmit" value="<#if user??>Log out<#else>Log in</#if>">
            </form>
        </nav>
    </div>
</div>