<#import "parts/projectTemplate.ftl" as p>
<@p.projectTemplate "Messages">
    <script src="../../assets/js/sockjs-0.3.4.js"></script>
    <script src="../../assets/js/stomp.js"></script>
    <script type="text/javascript">
        var stompClient = null;

        $(document).ready(function () {
            scrollToBottom();
            disconnect();
            connect();
        });

        function setConnected(connected) {
            document.getElementById('chat_id').readOnly = !!connected;
        }

        function connect() {
            console.log("connect");
            var chat_id = document.getElementById('chat_id').value.trim().replace(/\s/g, '');
            var telegram_user_id = document.getElementById('telegram_id').value.trim().replace(/\s/g, '');
            console.log(chat_id);
            var socket = new SockJS('http://localhost:8080/chat');
            console.log(socket);
            stompClient = Stomp.over(socket);
            console.log(stompClient);
            stompClient.connect({}, function (frame) {
                setConnected(true);
                console.log('Connected: ' + frame);
                stompClient.subscribe('/topic/messages/' + chat_id, function (messageOutput) {
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
            stompClient.send("/app/chat/" + chat.trim().replace(/\s+/g, ''), {},
                JSON.stringify({from: from, text: text, username: from, chat_id: chat}));
            document.getElementById('text').value = '';
            scrollToBottom();
        }

        function showMessageOutput(messageOutput) {
            let response = document.getElementById('chat_messages');

            let div = document.createElement('div');
            div.className = 'row justify-content-between';
            let p = document.createElement('div');
            let b = document.createElement('b');
            b.style.marginRight = '2px';
            b.innerText = messageOutput.username;
            let i = document.createElement('i');
            i.innerText = messageOutput.text;
            p.appendChild(b);
            p.appendChild(i);
            let span = document.createElement('span');
            span.innerText = messageOutput.dateTime;
            div.appendChild(p);
            div.appendChild(span);


            response.appendChild(div);
            scrollToBottom();
        }


        function scrollToBottom() {
            let objDiv = document.getElementById("chat_messages");
            objDiv.scrollTop = objDiv.scrollHeight;
        }

        document.getElementById('text').addEventListener("keyup", function (event) {
            if (event.keyCode === 13) {
                event.preventDefault();
                document.getElementById("send_msg_btn").click();
            }
        };
    </script>
<#--если у проекта есть чат выводим все сообщения + поле для ввода-->
    <div class="container">
        <#if project.chat??>
            <div class="container col-8" id="chat_messages" style="height: 60%; overflow: scroll">
                <hr>
                <#list messages as message>
                    <div class="row justify-content-between">
                        <div>
                            <b style="margin-right: 2px">${message.senderUserName}</b>
                            <i>${message.text}</i>
                        </div>
                        <span>${message.date}</span>
                    </div>
                </#list>
            </div>
            <div class="container col-8 justify-content-center">
                <hr>
                <label for="text">Enter message</label>
                <input type="text" name="textMessage" id="text" style="border:1px solid;border-radius:12px"
                       class="col-xl-8 col-md-6 col-sm-5 col-xs-5" autocomplete="off">
                <input type="hidden" value="${chat.id}" id="chat_id" name="chat_id" readonly="">
                <input type="hidden" value="${user.username}" id="from" name="from">
                <input type="hidden" value="${user.telegramId}" id="telegram_id" name="from">
                <button onclick="sendMessage()" class="logOutSubmit" id="send_msg_btn">Send</button>
            </div>
            <p id="response"></p>
        <#else>
        <#--если нет смотрим подключен ли узер к телеграмму-->
            <#if user.telegramJoined>
            <#--если подключен-->
                <#if members?size gt 0 >
                    <div class="flex-container" style="width: 80%; height: 80%">
                        <form id="create_chat" method="post" action="${context.getContextPath()}/telegram/createChat">
                            <#--выводим всех у кого подключен телеграмм-->
                            <label>Enter members
                                <select style="margin-left: 4px;width:150px;height:25px;border:2px solid" multiple
                                        name="members">
                                    <#list members as member>
                                        <option value="${member.id}">${member.username}</option>
                                    <#--<input type="checkbox" name="${member.id}">-->
                                    <#--<label for="${member.id}">${member.username}</label>-->
                                    <#--<br>-->
                                    </#list>
                                </select>
                            </label>
                            <br/>
                            <input type="hidden" value="${project.id}" name="project_id">


                            <label for="title">Enter chat title
                                <input type="text" name="title">
                            </label>
                            <br/>
                            <button type="submit" class="profile-edit-submit">Create chat</button>
                        </form>
                    </div>
                <#else>
                    <h4>У вас еще нет пользователей с телегой</h4>
                </#if>
            <#--если нет предлагаем подключиться-->
            <#else>
                <div class="row">
                    <div class="container mt-5" align="center">
                        <div class="jumbotron col-md-8" align="center">
                            <h1 class="display-4">Connect to Telegram</h1>
                            <p class="lead">Simple to use in your project</p>
                            <hr class="my-4">
                            <!--Если форма для кода или для телефона, то выводим ее-->
                            <#if connectionForm??>
                                <form action="${context.getContextPath()}/telegram/connect/${inputName}" method="post"
                                      id="telegram_form">
                                    <label for="code"
                                           id="code_label">Enter <#if phoneForm??>phone<#else>code</#if></label>
                                    <#--<input type="text" name="code" id="code" value="">-->
                                    <input type="text" name="${inputName}" id="code" value="">
                                    <input type="hidden" name="projectId" value="${project.id}">
                                    <button type="submit" id="send_code_btn">Send</button>
                                </form>
                            <#else >
                                <form action="${context.getContextPath()}/telegram/connect" method="post">
                                    <button type="submit" class="btn btn-primary btn-lg">Connect to Telegram</button>
                                    <input type="hidden" name="buttonForm" value="buttonForm">
                                    <input type="hidden" name="projectId" value="${project.id}">
                                </form>
                            </#if>
                        </div>
                    </div>
                </div>
            </#if>
        </#if>
    </div>
</@p.projectTemplate>