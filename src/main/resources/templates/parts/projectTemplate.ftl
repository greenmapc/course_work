<#macro projectTemplate title>
    <#import "common.ftl" as c>
    <#import "projectNavbar.ftl" as p>
    <@c.page title>
        <div class="container">
            <@p.projectNavbar/>
                <#nested>
        </div>
    </@c.page>
</#macro>