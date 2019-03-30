<#import "parts/common.ftl" as c>
<#import "parts/projectNavbar.ftl" as p>
<@c.page "Files">
    <div class="container">
        <div class="row">
            <@p.projectNavbar/>
        </div>

        <div class="container profileSettings" style="margin-top:10px;">
            <h2>Upload files:</h2>
            <div class="row mt-2">
                <div class="col">
                    <label for="username">Doc.docx</label>
                </div>
                <div class="col">
                    <a href="#">Download</a>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <label for="file2">Pravki.docx</label>
                </div>
                <div class="col">
                    <a href="#">Download</a>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <label for="file3">Readme.md</label>
                </div>
                <div class="col">
                    <a href="#">Download</a>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <label for="file4">Description.pdf</label>
                </div>
                <div class="col">
                    <a href="#">Download</a>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <label for="file">Add document</label>
                </div>
                <div class="col">
                    <input type="file" accept="image/*" name="file" id="file">
                </div>
            </div>
        </div>
    </div>
</@c.page>