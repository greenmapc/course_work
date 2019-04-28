package ru.itis.teamwork.services;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.teamwork.models.Message;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.util.githubApi.GitHubApi;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Data
public class TelegramService {

    private String host;

    private Integer port;

    private URIBuilder uriBuilder;

    private final String SEND_CODE_REQUEST = "{\"phone\":\"%s\",\"code\":\"%s\"}";

    private final String SEND_ID_REQUEST = "{\"phone\":\"%s\"}";

    private final String SEND_PHONE_REQUEST = "{\"phone\":\"%s\"}";

    private final String CREATE_CHAT_REQUEST = "{\"phone\":\"%s\",\"members\":%s,\"title\":\"%s\"}";

    private final String SEND_MESSAGE_TO_CHAT = "{\"sender\":\"%s\",\"recipient\":\"%s\",\"text\":\"%s\"}";

    @Autowired
    private AmqpTemplate template;

    private String queueName;


    public TelegramService(String host, Integer port, String queueName) {
        this.host = host;
        this.port = port;
        this.queueName = queueName;

        try {
            this.uriBuilder = new URIBuilder(this.host);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        this.uriBuilder.setPort(this.port);
    }

    public Optional<Boolean> isConnectedUser(String phone)
            throws URISyntaxException, IOException {
        uriBuilder.setPath("/connect");
        HttpPost httpPost = new HttpPost(uriBuilder.build());
        httpPost.addHeader("Content-Type", "application/json");

        StringEntity stringEntity = new StringEntity(String.format(SEND_PHONE_REQUEST, phone));
        httpPost.setEntity(stringEntity);
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(httpPost);

        if (response.getStatusLine().getStatusCode() == 200) {
            JSONArray jsonArray = GitHubApi.getJsonResp(response);
            return Optional.of(jsonArray.getJSONObject(0).getBoolean("is_connected"));
        } else {
            throw new IOException("Can't connect or phone is not available");
        }
    }

    public Optional<Boolean> sendCode(String phone, String code)
            throws IOException, URISyntaxException {
        uriBuilder.setPath("/sign");
        HttpPost httpPost = new HttpPost(uriBuilder.build());
        httpPost.addHeader("Content-Type", "application/json");

        StringEntity stringEntity = new StringEntity(String.format(SEND_CODE_REQUEST, phone, code));
        httpPost.setEntity(stringEntity);
        HttpClient httpClient = HttpClients.createDefault();

        HttpResponse response = httpClient.execute(httpPost);
        if (response.getStatusLine().getStatusCode() == 200) {
            return Optional.of(true);
        } else {
            throw new IOException("Wrong code " + code);
        }
    }

    public Optional<Long> createChat(List<Long> membersPhone, String creatorPhone, String chatTitle)
            throws URISyntaxException, IOException {
        uriBuilder.setPath("/create_chat");
        HttpPost httpPost = new HttpPost(uriBuilder.build());
        httpPost.addHeader("Content-Type", "application/json");
        if (chatTitle.isEmpty()) {
            chatTitle = UUID.randomUUID().toString().substring(0, 10);
        }

        StringEntity stringEntity = new StringEntity(
                String.format(
                        CREATE_CHAT_REQUEST,
                        creatorPhone,
                        membersPhone.toString(),
                        chatTitle)
        );
        httpPost.setEntity(stringEntity);
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(httpPost);

        if (response.getStatusLine().getStatusCode() == 200) {
            JSONArray jsonArray = GitHubApi.getJsonResp(response);
            Long chat_id = jsonArray.getJSONObject(0).getLong("chat_id");
            return Optional.of(chat_id);
        } else {
            throw new IOException("Can't create chat! Wrong parameters. " +
                    "Members phone: " + membersPhone.toString() +
                    ", sender phone: " + creatorPhone +
                    ". chat title: " + chatTitle);
        }
    }

    @SneakyThrows
    public Long getTelegramId(User user){
        uriBuilder.setPath("/get_id");
        HttpPost httpPost = new HttpPost(uriBuilder.build());
        httpPost.addHeader("Content-Type", "application/json");
        StringEntity stringEntity = new StringEntity(String.format(SEND_ID_REQUEST, user.getPhone()));

        httpPost.setEntity(stringEntity);
        HttpClient httpClient = HttpClients.createDefault();
        try {
            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                JSONArray jsonArray = GitHubApi.getJsonResp(response);
                Long telegramId = jsonArray.getJSONObject(0).getLong("telegram_id");
                return telegramId;
            }else {
                return null;
            }
        }catch (Exception e){
            return null;
        }


    }

    public void sendMessage(Message message) throws IOException {

        String messageRabbit = String.format(
                SEND_MESSAGE_TO_CHAT,
                message.getSender().getPhone(),
                message.getChat().getId(),
                message.getText()
        );

        template.convertAndSend(queueName, messageRabbit, m -> {
            m.getMessageProperties().setContentType("application/json");
            return m;
        });

    }
}
