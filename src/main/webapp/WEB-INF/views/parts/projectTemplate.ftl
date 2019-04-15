<#macro projectTemplate title>
    <#import "common.ftl" as c>
    <#import "projectNavbar.ftl" as p>
    <@c.page title>
        <div class="container">
            <div class="row justify-content-center">
                <span><i>${project.name}</i></span>
            </div>
            <div class="row justify-content-center">
                <@p.projectNavbar/>
            </div>
            <div class="row">
                <#nested>
            </div>
        </div>
    </@c.page>
</#macro>