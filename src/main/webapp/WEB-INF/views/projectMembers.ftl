<#import "parts/common.ftl" as c>
<#import "parts/projectNavbar.ftl" as p>
<@c.page "Members">
    <div class="container">
        <@p.projectNavbar/>
        <div class="row ml-1 justify-content-between">
            <h4>Members of project:</h4>
            <a role="button" href="#" class="btn btn-info">Add</a>
        </div>
        <hr>
        <div class="row">
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
            </div>
        </div>
    </div>
</@c.page>