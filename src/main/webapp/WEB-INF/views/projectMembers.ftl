<#import "parts/projectTemplate.ftl" as p>
<@p.projectTemplate "Members">

    <div class="container">
        <#--<div class="card-deck">-->
        <#--<div class="card my-3" style="width: 200px;">-->
        <#--<img src="https://www.cierpgaud.fr/wp-content/uploads/2018/07/avatar.jpg"-->
        <#--class="card-img-top p-2" style="border-radius: 50%"-->
        <#--alt="...">-->
        <#--<div class="card-body" align="center">-->
        <#--<h5 class="card-title">Full-stack developer</h5>-->
        <#--<p class="card-text">Java, Java and one more Java</p>-->
        <#--<a href="#" class="btn btn-outline-info">Info</a>-->
        <#--</div>-->
        <#--</div>-->
        <#--<div class="card my-3" style="width: 200px;">-->
        <#--<img src="https://www.cierpgaud.fr/wp-content/uploads/2018/07/avatar.jpg"-->
        <#--class="card-img-top p-2" style="border-radius: 50%"-->
        <#--alt="...">-->
        <#--<div class="card-body" align="center">-->
        <#--<h5 class="card-title">Full-stack developer</h5>-->
        <#--<p class="card-text">Java, Java and one more Java</p>-->
        <#--<a href="#" class="btn btn-outline-info">Info</a>-->
        <#--</div>-->
        <#--</div>-->
        <#--<div class="card my-3" style="width: 200px;">-->
        <#--<img src="https://www.cierpgaud.fr/wp-content/uploads/2018/07/avatar.jpg"-->
        <#--class="card-img-top p-2" style="border-radius: 50%"-->
        <#--alt="...">-->
        <#--<div class="card-body" align="center">-->
        <#--<h5 class="card-title">Full-stack developer</h5>-->
        <#--<p class="card-text">Java, Java and one more Java</p>-->
        <#--<a href="#" class="btn btn-outline-info">Info</a>-->
        <#--</div>-->
        <#--</div>-->
        <#--<div class="card my-3" style="width: 200px;">-->
        <#--<img src="https://www.cierpgaud.fr/wp-content/uploads/2018/07/avatar.jpg"-->
        <#--class="card-img-top p-2" style="border-radius: 50%"-->
        <#--alt="...">-->
        <#--<div class="card-body" align="center">-->
        <#--<h5 class="card-title">Full-stack developer</h5>-->
        <#--<p class="card-text">Java, Java and one more Java</p>-->
        <#--<a href="#" class="btn btn-outline-info">Info</a>-->
        <#--</div>-->
        <#--</div>-->
        <#--</div>-->
        <#--<form method="post" action="${context.getContextPath()}/project/${project.getId()}/settings/addMember"-->
        <#--id="add_member">-->
        <#--<div class="form_add_member">-->
        <#--<div class="add_member_left_col">-->
        <#--<input type="text" name="username" id="add_member_input_text">-->
        <#--<div class="found_memebers">-->
        <#--<ul id="found_memebers_ul">-->
        <#--<!-- <li class="found_memeber_node">-->
        <#--<a href="#">user1</a>-->
        <#--</li>-->
        <#--<li class="found_memeber_node">-->
        <#--<a href="#">user1</a>-->
        <#--</li> &ndash;&gt;-->
        <#--</ul>-->
        <#--</div>-->
        <#--</div>-->
        <#--<div class="add_member_right_col">-->
        <#--<input type="submit" value="Add member" id="add_member_input_submit">-->
        <#--</div>-->
        <#--</div>-->
        <#--</div>-->
        <div class="container" id="members_of_project" style="font-size: 14px">
            <table class="table">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Username</th>
                    <th scope="col">Role</th>
                    <th scope="col">Edit</th>
                </tr>
                </thead>
                <tbody>
                <#list members as user>
                    <tr>
                        <th scope="row">${user_index + 1}</th>
                        <td><a href="${context.getContextPath()}/profile/${user.id}">${user.username}</a></td>
                        <td><#list user.roles as role>${role}<#sep>, </#list></td>
                        <td><a href="#" style="color: #bd2130">Delete</a></td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
        <div class=container">
            <form method="post" id="add_member"
                  action="${context.getContextPath()}/project/${project.getId()}/settings/addMember">
                <input type="text" name="username" id="add_member_name" style="border: 2px solid;">
                <input type="submit" value="Add member">
            </form>
        </div>
        <div class="found_memebers">
            <ul id="found_memebers_ul">
            </ul>
        </div>
    </div>
    <#if error??>
        <p style="color: mediumvioletred"> ${error} </p>
    </#if>
    <script type="text/javascript">
        //получаем div found_members
        let found_memebers_ul = document.getElementById("found_memebers_ul");

        function searchMembers() {
            //Здесь тащим json !!!!
            for (const member of members) {
                let found_memeber_node = document.createElement('li');
                found_memeber_node.className = 'found_memeber_node';
                //вставляем имя (логин?)
                found_memeber_node.innerHTML = member.login;
                found_memebers_ul.appendChild(found_memeber_node);
            }
        }
    </script>
</@p.projectTemplate>