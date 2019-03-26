<#import "parts/common.ftl" as c>
<#import "parts/projectNavbar.ftl" as p>
<@c.page "Settings">
    <div class="container">
        <div class="row">
            <@p.projectNavbar/>
            <hr>
        </div>
        <div class="row">
            <div class="container ml-3">
                <form action="${context.getContextPath()}/project/settings/${project.id}" method="post">
                    <h4>Update project:</h4>
                    <div class="form-group">
                        <div class="col-md-6">
                            <label for="name">Name of project</label>
                            <input type="text" name="name" id="name" class="form-control" value="${project.name}"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-6">
                            <label for="description">Description</label>
                            <textarea class="form-control" id="description"
                                      name="description">${project.description}</textarea>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-dark">Update</button>
                </form>
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="container" align="center">
                <div class="jumbotron col-md-8" align="center">
                    <h1 class="display-4">Connecting to GitHub</h1>
                    <p class="lead">This is a simple hero unit, a simple jumbotron-style component for calling extra
                        attention to featured content or information.</p>
                    <hr class="my-4">
                    <p>It uses utility classes for typography and spacing to space content out within the larger
                        container.</p>
                    <a class="btn btn-info btn-lg" href="#" role="button">Connect</a>
                </div>
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="container">
                <h6>Delete this project</h6>
                <form action="${context.getContextPath()}/project/delete/${project.id}" method="post">
                    <button class="btn btn-outline-danger" type="submit">Delete</button>
                </form>
            </div>
        </div>
    </div>
</@c.page>