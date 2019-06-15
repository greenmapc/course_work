<#import "parts/common.ftl" as c>
<@c.page "Projects">
    <div class="profile-main-block container">
        <div class="float-left profile-main-inf-l profile-main-inf col-3 min-width">
            <div class="profile-photo">
                <#if user??>
                    <#if user.getImg()??>
                        <img src="/img/${user.getImg().getFullName()}">
                    <#else>
                        <img src="https://www.cierpgaud.fr/wp-content/uploads/2018/07/avatar.jpg">
                    </#if>
                </#if>
            </div>
            <div class="profile-name">
                <span>${user.firstName} ${user.lastName}</span>
            </div>
            <#if isCurrentUser>
                <form action="/settings">
                    <input type="submit" value="Edit" class="profile-edit-submit"/>
                </form>
            </#if>
        </div>
        <div class="float-left profile-main-inf col-9">
            <div class="row">
                <div class="col-1"></div>
                <nav class="navbar navbar-expand-lg navbar-light">
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
                                <a class="nav-link add-link" href="#">Add project</a>
                            </li>
                        </ul>
                    </div>
                </nav>
            </div>
            <#if user.projects?size != 0>
                <#list user.projects as project>
                    <div class="row projects">
                        <div class="col-1"></div>
                        <div class="col-8 project-card-in">
                            <a href="/project/${project.id}">
                                <h3 class="profile-project-name">
                                    ${project.name}
                                </h3>
                            </a>
                            <span class="profile-project-about">
                                About this project
                            </span>
                        </div>
                    </div>
                </#list>
            <#else>
                <h3>No projects here</h3>
            </#if>
        </div>
    </div>
</@c.page>