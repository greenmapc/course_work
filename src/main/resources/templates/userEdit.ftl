<#import "parts/common.ftl" as c>
<@c.page "User list">
    <div class="container mt-5">
        <div class="form-group col-xl-4 col-md-6">
            <h3>User editor</h3>
            <form action="/user" method="post">
                <div class="form-group">
                    <input type="text" class="form-control" value="${user.username}" name="username">
                </div>
                <div class="form-group">
                    <input type="text" class="form-control" value="${user.firstName}" name="firstName">
                </div>
                <div class="form-group">
                    <input type="text" class="form-control" value="${user.lastName}" name="lastName">
                </div>
                <#list roles as role>
                    <label>
                        <input type="checkbox"
                               name="${role}" ${user.roles?seq_contains(role)?string("checked", "")}>${role}
                    </label>
                </#list>
                <input type="hidden" value="${user.id}" name="userId">
                <div class="form-group">
                    <button type="submit" class="btn btn-primary">Save</button>
                </div>
            </form>
        </div>
    </div>
</@c.page>
<#--
</body>
</html>-->
