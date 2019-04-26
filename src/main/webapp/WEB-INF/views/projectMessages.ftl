<#import "parts/projectTemplate.ftl" as p>
<@p.projectTemplate "Messages">
    <script src="../../assets/js/sockjs-0.3.4.js"></script>
    <script src="../../assets/js/stomp.js"></script>
    <script type="text/javascript">
        var stompClient = null;

        $(document).ready(function() {
            disconnect();
            connect();
        });

        function setConnected(connected) {
            // document.getElementById('connect').disabled = connected;
            // document.getElementById('disconnect').disabled = !connected;
            // document.getElementById('conversationDiv').style.visibility
            //     = connected ? 'visible' : 'hidden';
            // document.getElementById('response').innerHTML = '';
            if (connected) {
                document.getElementById('chat_id').readOnly = true;
            } else {
                document.getElementById('chat_id').readOnly = false;
            }
        }

        function connect() {
            console.log("connect");
            var chat_id = document.getElementById('chat_id').value.trim().replace(/\s/g, '');
            console.log(chat_id);
            var socket = new SockJS('http://localhost:8080/chat');
            console.log(socket);
            stompClient = Stomp.over(socket);
            console.log(stompClient);
            stompClient.connect({}, function (frame) {
                setConnected(true);
                console.log('Connected: ' + frame);
                stompClient.subscribe('/topic/messages/'+chat_id, function (messageOutput) {
                    showMessageOutput(JSON.parse(messageOutput.body));
                });
            });
        }

        function disconnect() {
            if (stompClient != null) {
                stompClient.disconnect();
            }
            setConnected(false);
            console.log("Disconnected");
        }

        function sendMessage() {
            var from = document.getElementById('from').value;
            var text = document.getElementById('text').value;
            var chat = document.getElementById('chat_id').value;
            stompClient.send("/app/chat/"+chat.trim().replace(/\s+/g, ''), {},
                JSON.stringify({from: from, text: text, username: from, chat_id: chat}));
            document.getElementById('text').value = ''
        }

        function showMessageOutput(messageOutput) {
            console.log("ahsda");
            var response = document.getElementById('response');
            var p = document.createElement('p');
            console.log(p);
            p.style.wordWrap = 'break-word';
            p.appendChild(document.createTextNode(messageOutput.username + ": "
                + messageOutput.text + " (" + messageOutput.dateTime + ")"));
            response.appendChild(p);
        }
    </script>
    <#--если у проекта есть чат выводим все сообщения + поле для ввода-->
    <#if project.chat??>
        <div class="container">
            <#list messages as message>
                <span>${message.text}</span>
                <h6>${message.senderUserName}</h6>
                <h6>${message.date}</h6>
                <br>
            </#list>
        </div>



        <div class="container">
            <label for="textMessage">Enter message</label>
            <input type="text" name="textMessage" id="text">
            <input type="hidden" value="${chat.id}" id="chat_id" name="chat_id">
            <input type="hidden" value="${user.username}" id="from" name="from">
            <button onclick="sendMessage()">Send</button>
            <p id="response"></p>
        </div>
    <#else>
<#--если нет смотрим подключен ли узер к телеграмму-->
        <#if user.telegramJoined>
            <#--если подключен-->
            <#if members?size gt 0 >
                <form id="create_chat" method="post" action="/telegram/createChat">
                    <#--выводим всех у кого подключен телеграмм-->
                    <select multiple name="members">
                        <#list members as member>
                            <option value="${member.id}">${member.username}</option>
                            <#--<input type="checkbox" name="${member.id}">-->
                            <#--<label for="${member.id}">${member.username}</label>-->
                            <#--<br>-->
                        </#list>
                    </select>
                    <input type="hidden" value="${project.id}" name="project_id">
                    <input type="text" id="title" name="title">
                    <label for="title">Title</label>
                    <button type="submit">Create chat</button>
                </form>
            <#else>
                <h4>У вас еще нет пользователей с телегой</h4>
            </#if>
            <#--если нет предлагаем подключиться-->
        <#else >
            <div class="row">
                <div class="container mt-5" align="center">
                    <div class="jumbotron col-md-8" align="center">
                        <h1 class="display-4">Connect to Telegram</h1>
                        <p class="lead">Simple to use in your project</p>
                        <hr class="my-4">
                        <a class="btn btn-primary btn-lg" id="connection_btn" role="button">Connect
                            to Telegram</a>
                    </div>
                </div>
            </div>
        </#if>

    </#if>
</@p.projectTemplate>