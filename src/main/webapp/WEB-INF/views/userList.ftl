<html lang="en">
<head>
    <title>Admin page</title>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
          integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous"/>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.1/css/all.css"
          integrity="sha384-fnmOCqbTlWIlj8LyTjo7mOUStjsKC4pOpQbqyi7RrhN7udi9RwhKkMHpvLbHG9Sr" crossorigin="anonymous"/>
</head>
<body>
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
</body>
</html>