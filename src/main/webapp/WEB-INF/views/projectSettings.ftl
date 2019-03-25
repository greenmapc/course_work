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
                <h4>Update project:</h4>
                <form action="${context.getContextPath()}/project/settings/${project.id}" method="post">
                    <div class="form-group">
                        <label class="col-form-label">Name of project</label>
                        <div class="col-md-6">
                            <input type="text" name="name" class="form-control" value="${project.name}"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-form-label">Description</label>
                        <div class="col-md-6">
                            <textarea class="form-control" name="description">${project.description}</textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <button type="submit" class="btn btn-dark">Update</button>
                    </div>
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
    </div>
</@c.page>