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
                }
            })
        });
    });
});