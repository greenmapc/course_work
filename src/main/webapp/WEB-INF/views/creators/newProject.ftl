<#import "../parts/common.ftl" as c>
<#import "/spring.ftl" as spring/>

<@c.page "Project">
    <div class="nav">
        <div class="inner">
        </div>
        <div class="container navbar-container ">
            <nav class="navbar navbar-expand navbar-light main-navbar">
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
    <div class="profile-main-block container">
        <div class="float-left profile-main-inf-l profile-main-inf col-3 min-width">
            <div class="profile-photo">
                <img src="https://pp.userapi.com/c851228/v851228331/c4598/wjAQ3oPDgs8.jpg">
            </div>
            <div class="profile-name">
                <span>Kuzmenko Anna</span>
            </div>
            <form action="${context.getContextPath()}/logout">
                <input type="submit" value="Edit" class="profile-edit-submit"/>
            </form>
        </div>
        <div class="float-left profile-main-inf profile-add-project">
            <div class="row">
                <!--<div class="col-1"></div>-->
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
                                <a class="nav-link add-link" href="#">Add project</a>
                            </li>
                        </ul>
                    </div>
                </nav>
            </div>
            <div class="profile-add-project-container">
                <div class="row mb-4">
                    <div class="text-left col-12 pl-0"><h1>Create a new project</h1>
                    </div>
                </div>
                <div class="row add-project-form">
                    <form class="add-project-form" action="${context.getContextPath()}/newProject" method="post">
                        <@spring.bind "form"/>
                        <label for="nameField">
                            <span>Project name</span>
                            <@spring.formInput "form.name" "id = 'nameField'"/>
                        </label><br>
                        <label for="descriptionField">
                            <span>Description</span>
                            <@spring.formTextarea "form.description" "id = 'descriptionField'"/>
                        </label><br>
                        <input type="submit" value="Create" class="add-project-submit"/>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@c.page>