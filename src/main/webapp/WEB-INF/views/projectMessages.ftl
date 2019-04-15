<#import "parts/common.ftl" as c>
<#import "parts/projectNavbar.ftl" as p>
<@c.page "Messages">
    <div class="container">
        <@p.projectNavbar/>
        <div class="row">
            <div class="container mt-5" align="center">
                <div class="jumbotron col-md-8" align="center">
                    <h1 class="display-4">Connect to Telegram</h1>
                    <p class="lead">Simple to use in your project</p>
                    <hr class="my-4">
                    <a class="btn btn-primary btn-lg" href="${context.getContextPath()}/telegram/connect" role="button">Connect to Telegram</a>
                </div>
            </div>
        </div>
    </div>
</@c.page>