var ready = $(document).ready(function () {
    var CONTEXT_PATH = $('#contextPath').attr('data-contextPath');
    $(function(){
        $('#add_member_name').on('keyup', function(e) {
            var $part_of_username = $('#add_member_name').val();
            console.log($part_of_username);
            console.log(CONTEXT_PATH);

            var $form = $('#add_member');
            $.ajax({
                url: CONTEXT_PATH + "/show_like_users",
                type: 'GET',
                dataType: 'json',
                data : {username : $part_of_username},
                success:function(response){
                    console.log("success");
                    console.log(response);

                    let found_memebers_ul = document.getElementById("found_memebers_ul");
                    if(found_memebers_ul.hasChildNodes()){
                        found_memebers_ul.innerHTML = '';
                    }
                    for (const member of response) {
                        let found_memeber_node = document.createElement('li');
                        found_memeber_node.className = 'found_memeber_node';
                        //вставляем имя (логин?)
                        found_memeber_node.innerHTML = member.username;
                        found_memebers_ul.appendChild(found_memeber_node);
                    }
                    // return response;
                },
                error:function(jqXHR, textStatus, errorThrown) {
                    console.log(jqXHR);
                    console.log(jqXHR.responseText);
                    console.log(textStatus);
                    console.log(errorThrown);
                }
            })
        });
    });
});