<#import "parts/common.ftl" as c>
<@c.page "Profile">
    <div class="profile-main-block container">
        <div class="float-left profile-main-inf-l profile-main-inf col-3 min-width">
            <div class="profile-photo">
                <img src="https://pp.userapi.com/c851228/v851228331/c4598/wjAQ3oPDgs8.jpg">
            </div>
            <div class="profile-name">
                <span>${user.firstName} ${user.lastName}</span>
            </div>
            <form action="${context.getContextPath()}/profileSettings">
                <input type="submit" value="Edit" class="profile-edit-submit"/>
            </form>
        </div>
        <div class="float-left profile-main-inf-r profile-main-inf col-8">
            <div class="row">
                <nav class="navbar navbar-expand navbar-light pl-0">
                    <div class="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul class="navbar-nav mr-auto">
                            <li class="nav-item">
                                <a class="nav-link" href="#">Overview</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="#">Projects</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="#">Settings</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link add-link" href="${context.getContextPath()}/newProject">Add project</a>
                            </li>
                        </ul>
                    </div>
                </nav>
            </div>
            <#if projects??>
                <#list projects as project>
                    <#if project_index % 2 == 0>
                        <div class="row">
                    </#if>
                    <div class="profile-project-card col-5">
                        <a href="${context.getContextPath()}/project/${project.id}">
                            <h3 class="profile-project-name">
                                ${project.name}
                            </h3>
                        </a>
                        <span class="profile-project-about">
                                About this project
                            </span>
                    </div>
                    <div class="col-1"></div>
                    <#if project_index % 2 == 1>
                        </div>

                    </#if>
                </#list>
            <#else>
                <button><a class="nav-link add-link" href="${context.getContextPath()}/addProject">Add project</a>
                </button>
            </#if>
        <#--<div class="row">
            <div class="col-1">
            </div>
            <div class="col-5 profile-project-card">
                <a href="/">
                    <h3 class="profile-project-name">
                        Project1
                    </h3>
                </a>
                <span class="profile-project-about">
                About this project
            </span>
            </div>
            <div class="col-1">
            </div>
            <div class="col-5 profile-project-card">
                <a href="/">
                    <h3 class="profile-project-name">
                        Project2
                    </h3>
                </a>
                <span class="profile-project-about">
                About this project
            </span>
            </div>
        </div>
        <div class="row">
            <div class="col-1">
            </div>
            <div class="col-5 profile-project-card">
                <a href="/">
                    <h3 class="profile-project-name">
                        Project3
                    </h3>
                </a>
                <span class="profile-project-about">
                About this project
            </span>
            </div>
            <div class="col-1">
            </div>
            <div class="col-5 profile-project-card">
                <a href="/">
                    <h3 class="profile-project-name">
                        Project4
                    </h3>
                </a>
                <span class="profile-project-about">
                About this project
            </span>
            </div>
        </div>-->
        </div>
    </div>
</@c.page>