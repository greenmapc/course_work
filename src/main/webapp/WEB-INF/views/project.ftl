<#import "parts/common.ftl" as c>
<#import "parts/projectNavbar.ftl" as p>
<@c.page "Project">
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
            <div class="col-3">
            </div>
            <div class="col-9">

            </div>
        </div>
    </div>
</@c.page>