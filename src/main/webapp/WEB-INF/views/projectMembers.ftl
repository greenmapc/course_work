<#import "parts/common.ftl" as c>
<#import "parts/projectNavbar.ftl" as p>
<@c.page "Members">
    <div class="nav">
        <div class="inner">
        </div>
        <div class="container navbar-container ">
            <nav class="navbar navbar-expand-lg navbar-light main-navbar">
                <!--Основное меню для всего сайта-->
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
                </ul>
            </nav>
        </div>
    </div>
    <div class="container">
        <div class="row">
            <div class="col-1"></div>
            <@p.projectNavbar/>
        </div>
        <div class="row">
            <table class="table">
                <thead class="thead-light">
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Username</th>
                    <th scope="col">Role</th>
                    <th scope="col">#</th>
                </tr>
                </thead>
                <tbody>
                <#list members as member>
                    <tr>
                        <th scope="row">${member_index + 1}</th>
                        <td><a href="#">${member.username}</a></td>
                        <td><#list member.roles as role>${role}<#sep>, </#list></td>
                        <td><a href="/user/project/edit/${member.id}" role="button"></a></td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>
</@c.page>