<div class="nav">
    <div class="inner">
    </div>
    <div class="container navbar-container ">
        <nav class="navbar navbar-expand navbar-light pl-0 main-navbar">
            <a class="navbar-brand" href="#">TeamWorker</a>
            <ul class="navbar-nav mr-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${context.getContextPath()}/home">Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">Projects</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${context.getContextPath()}/profile">Profile</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">About</a>
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