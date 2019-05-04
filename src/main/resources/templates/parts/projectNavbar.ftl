<#macro projectNavbar>
    <div class="row justify-content-center">
        <div class="container col-4" align="left">
            <h5 style="color: deepskyblue;margin-top: 13px">${project.name}</h5>
        </div>
        <div class="container col-8">
            <nav class="navbar navbar-expand navbar-light">
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav mr-auto">
                        <li class="nav-item">
                            <a class="nav-link" href="/project/${project.id}">Overview</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link"
                               href="/project/members/${project.id}">Members</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/project/tasks/${project.id}">Tasks</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link"
                               href="/project/messages/${project.id}">Messages</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/project/files/${project.id}">Files</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link"
                               href="/project/settings/${project.id}">Settings</a>
                        </li>
                    </ul>
                </div>
            </nav>
        </div>
    </div>
</#macro>