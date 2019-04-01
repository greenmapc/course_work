<#import "parts/projectTemplate.ftl" as p>
<@p.projectTemplate "Members">
    <div class="container">
        <div class="card-deck">
            <div class="card my-3" style="width: 200px;">
                <img src="https://www.cierpgaud.fr/wp-content/uploads/2018/07/avatar.jpg"
                     class="card-img-top p-2" style="border-radius: 50%"
                     alt="...">
                <div class="card-body" align="center">
                    <h5 class="card-title">Full-stack developer</h5>
                    <p class="card-text">Java, Java and one more Java</p>
                    <a href="#" class="btn btn-outline-info">Info</a>
                </div>
            </div>
            <div class="card my-3" style="width: 200px;">
                <img src="https://www.cierpgaud.fr/wp-content/uploads/2018/07/avatar.jpg"
                     class="card-img-top p-2" style="border-radius: 50%"
                     alt="...">
                <div class="card-body" align="center">
                    <h5 class="card-title">Full-stack developer</h5>
                    <p class="card-text">Java, Java and one more Java</p>
                    <a href="#" class="btn btn-outline-info">Info</a>
                </div>
            </div>
            <div class="card my-3" style="width: 200px;">
                <img src="https://www.cierpgaud.fr/wp-content/uploads/2018/07/avatar.jpg"
                     class="card-img-top p-2" style="border-radius: 50%"
                     alt="...">
                <div class="card-body" align="center">
                    <h5 class="card-title">Full-stack developer</h5>
                    <p class="card-text">Java, Java and one more Java</p>
                    <a href="#" class="btn btn-outline-info">Info</a>
                </div>
            </div>
            <div class="card my-3" style="width: 200px;">
                <img src="https://www.cierpgaud.fr/wp-content/uploads/2018/07/avatar.jpg"
                     class="card-img-top p-2" style="border-radius: 50%"
                     alt="...">
                <div class="card-body" align="center">
                    <h5 class="card-title">Full-stack developer</h5>
                    <p class="card-text">Java, Java and one more Java</p>
                    <a href="#" class="btn btn-outline-info">Info</a>
                </div>
            </div>
        </div>
        <form method="post" action="${context.getContextPath()}/project/${project.getId()}/settings/addMember">
            <input type="text" name="username" id="add_member_name" style="
    border: 2px solid;
">
            <input type="submit" value="Add member" >
        </form>
        <#if error??>
            <p style="color: mediumvioletred"> ${error} </p>
        </#if>
    </div>
</@p.projectTemplate>