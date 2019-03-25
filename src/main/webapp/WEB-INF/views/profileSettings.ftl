<#import "parts/common.ftl" as c>
<@c.page "Profile Settings">
    <div class="container profileSettings">
        <h1>Update profile</h1>
        <form method="post" action="${context.getContextPath()}/profileSettings" enctype="multipart/form-data">
            <div class="row">
                <div class="col">
                    <label for="username">Username</label>
                </div>
                <div class="col">
                    <input type="text" id="username" name="username" readonly value="${user.username}">
                </div>
            </div>

            <div class="row">
                <div class="col">
                    <label for="firstName">First name</label>
                </div>
                <div class="col">
                    <input type="text" id="firstName" name="firstName" value="${user.firstName}">
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <label for="lastName">Last name</label>
                </div>
                <div class="col">
                    <input type="text" id="lastName" name="lastName" value="${user.lastName}">
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <label for="file">Choose photo</label>
                </div>
                <div class="col">
                    <input type="file" accept="image/*" name="file" id="file">
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <label for="password">Password</label>
                </div>
                <div class="col">
                    <input type="password" id="password" name="password">
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <label for="password2">Repeat password</label>
                </div>
                <div class="col">
                    <input type="password" id="password2" name="password2">
                </div>
            </div>
            <div class="row">
                <div class="col"></div>
                <div class="col">
                    <button type="submit">Update</button>
                </div>
            </div>
        </form>
    </div>
</@c.page>