<#import "parts/common.ftl" as c>
<#import "/spring.ftl" as spring/>

<@c.page "Profile Settings">
    <div class="container profileSettings">
        <h1>Update profile</h1>
        <form method="post" action="/settings" enctype="multipart/form-data">

            <@spring.bind "form"/>

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
                    <@spring.formInput "form.firstName" 'id="firstName" value="${user.firstName}"' 'text'/>
                    <@spring.showErrors "form.firstName"/>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <label for="lastName">Last name</label>
                </div>
                <div class="col">
                    <@spring.formInput "form.lastName" "id='lastName'' value='${user.lastName}'" 'text'/>
                    <@spring.showErrors "form.lastName"/>
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
                    <@spring.formPasswordInput "form.password" 'id="password"'/>
                    <@spring.showErrors "form.password"/>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <label for="password2">Repeat password</label>
                </div>
                <div class="col">
                    <@spring.formPasswordInput "form.repeatPassword" 'id="password2"'/>
                    <@spring.showErrors "form.repeatPassword"/>
                </div>
            </div>
            <div class="row">
                <div class="col"></div>
                <div class="col">
                    <button type="submit" class="btn btn-dark">Update</button>
                </div>
            </div>
        </form>
    </div>
</@c.page>