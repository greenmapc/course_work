var ready = $(document).ready(function () {
    $('#connection_btn').click(function () {
        var CONTEXT_PATH = $('#contextPath').attr('data-contextPath');
        console.log(CONTEXT_PATH);
        $.ajax({
            url: CONTEXT_PATH + "/telegram/connect",
            type: 'GET',
            dataType: 'json',
            success: function (response) {
                console.log("success");
                console.log(response);

                let connect_button = document.getElementById('connection_btn');
                // let html = '<form action="/telegram/connect" method="post"></form>';
                $('#connection_btn').replaceWith(' <form action="/telegram/connect" method="post" id="telegram_form">\n' +
                    '        <label for="code" id="code_label">Enter phone</label>\n' +
                    '        <input type="text" name="code" id="code" value="' + response + '">\n' +
                    '        <input type="hidden" name="projectId" value="${project.id}">' +
                    '        <button type="submit" id="send_code_btn">Send</button>\n' +
                    '    </form>');
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(jqXHR);
                console.log(jqXHR.responseText);
                console.log(textStatus);
                console.log(errorThrown);
            },
        });
    });
});

// var ready = $(document).ready(function () {
//     $('form').submit(function () {
//         console.log('hallo');
//         var CONTEXT_PATH = $('#contextPath').attr('data-contextPath');
//         console.log('z nen');
//         $.ajax({
//             url: CONTEXT_PATH + "/telegram/connect",
//             type: 'POST',
//             date: 'code='+ $('#code').val(),
//             success: function (response) {
//                 console.log(response);
//                 if (response) {
//                 //    обновить страницу
//                 }else {
//                     $('#code_label').text('Enter code');
//                 }
//
//             },
//             error: function (jqXHR, textStatus, errorThrown) {
//                 console.log(jqXHR);
//                 console.log(jqXHR.responseText);
//                 console.log(textStatus);
//                 console.log(errorThrown);
//             },
//         });
//     });
//
//
// });