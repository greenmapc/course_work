<#import "parts/common.ftl" as c>
<@c.page "Profile Settings">
    <div class="container">
        <h3>Update profile</h3>
        <form method="post" action="${context.getContextPath()}/profileSettings" enctype="multipart/form-data">
            <label for="username">Username</label>
            <input type="text" id="username" name="username" readonly value="${user.username}">
            <br>
            <label for="firstName">First name</label>
            <input type="text" id="firstName" name="firstName" value="${user.firstName}">
            <br>
            <label for="lastName">Last name</label>
            <input type="text" id="lastName" name="lastName" value="${user.lastName}">
            <br>
            <label for="file">Choose photo</label>
            <input type="file" accept="image/*" name="file" id="file">
            <br>
            <label for="password">Password</label>
            <input type="password" id="password" name="password">
            <br>
            <label for="password2">Repeat password</label>
            <input type="password" id="password2" name="password2">
            <br>
            <button type="submit">Update</button>
        </form>
    </div>
</@c.page>