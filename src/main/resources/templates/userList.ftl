<#import "parts/common.ftl" as c>
<@c.page "User list">
    <div class="container mt-5">
        <div class="container col-md-10">
            <h4>List of users:</h4>
            <table class="table">
                <thead class="thead-light">
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Username</th>
                    <th scope="col">First name</th>
                    <th scope="col">Last name</th>
                    <th scope="col">Role</th>
                    <th scope="col">#</th>
                </tr>
                </thead>
                <tbody>
                <#list users as user>
                    <tr>
                        <th scope="row">${user_index + 1}</th>
                        <td><a href="#">${user.username}</a></td>
                        <td>${user.firstName}</td>
                        <td>${user.lastName}</td>
                        <td><#list user.roles as role>${role}<#sep>, </#list></td>
                        <td><a href="/user/${user.id}">edit</a></td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>
</@c.page>
<#--
</body>
</html>-->
