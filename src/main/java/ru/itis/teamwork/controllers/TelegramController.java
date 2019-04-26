package ru.itis.teamwork.controllers;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.itis.teamwork.models.Chat;
import ru.itis.teamwork.models.Project;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.models.dto.MessageDto;
import ru.itis.teamwork.repositories.ChatRepository;
import ru.itis.teamwork.repositories.UserRepository;
import ru.itis.teamwork.services.ProjectService;
import ru.itis.teamwork.services.TelegramService;
import ru.itis.teamwork.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class TelegramController {


    private UserService userService;

    private String number = null;

    @Autowired
    private TelegramService telegramService;

    /*@Autowired
    private Connection rabbitmqConnection;*/

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @SneakyThrows
    public TelegramController(UserService userService) {
        this.userService = userService;

        /*this.factory = new ConnectionFactory();
        factory.setHost("http://35.204.168.100");
        Channel channel =factory.newConnection().createChannel();
        channel.queueDeclare("telegram", false, false, false, null);
        channel.basicPublish("", "telegram", null, "hallo".getBytes());*/
    }

    @GetMapping(value = "/telegram/connect", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    String getConnect(@AuthenticationPrincipal User user) {
        String phone = user.getPhone();
        if (phone == null) {
            phone = "Номер телефона";
        }
        System.out.println(phone);
        return phone;
    }

    @SneakyThrows
    @PostMapping(value = "/telegram/connect"/*, produces = MediaType.APPLICATION_JSON_VALUE*/)
    public String connect(@RequestParam(value = "code", required = false) String phoneNumber,
                          @RequestParam String projectId,
                          @RequestParam(value = "buttonForm", required = false) String bf,
                          @AuthenticationPrincipal User user,
                          Model model) {
        Project project = projectService.getProjectById(Long.parseLong(projectId));
        model.addAttribute("project", project);
        model.addAttribute("user", user);

        Set<User> members = project.getUsers();
        members.removeIf(a -> (a.getTelegramJoined() == null || !a.getTelegramJoined()));
        List<MessageDto> messageDtos = new ArrayList<>();
        if (project.getChat() != null && project.getChat().getMessages() != null) {
            messageDtos = project.getChat().getMessages().stream().map(MessageDto::new).collect(Collectors.toList());
            model.addAttribute("chat", project.getChat());
        }
        model.addAttribute("messages", messageDtos);
        members.remove(user);
        model.addAttribute("members", members);

        if (bf != null && bf.equals("buttonForm")) {
            model.addAttribute("phoneForm", true);
            return "projectMessages";
        }
        if (phoneNumber != null && phoneNumber.length() > 9) {
            Optional<Boolean> isConnected = telegramService.isConnectedUser(phoneNumber);

            if (isConnected.isPresent() && isConnected.get()) {
                user.setTelegramJoined(true);
                //userService.updateInfo(user);
                user.setPhone(phoneNumber);
            } else if (isConnected.isPresent()) {
                user.setPhone(phoneNumber);
            }
            userRepository.save(user);
            model.addAttribute("codeForm", true);
            return "projectMessages";
        } else {
            Optional<Boolean> signed = telegramService.sendCode(user.getPhone(), phoneNumber);

            model.addAttribute("phoneForm", false);
            if (signed.isPresent() && signed.get()) {
                user.setTelegramJoined(true);
                userRepository.save(user);
                //userService.updateInfo(user);
                return "redirect:/projectMessages";
            } else {
                model.addAttribute("codeForm", true);
                return "projectMessages";
            }
        }
    }

    @GetMapping("/telegram/code")
    public String getCode() {
        return "telegram/formCode";
    }

    @SneakyThrows
    @PostMapping("/telegram/code")
    public String code(@RequestParam("code") String code, @AuthenticationPrincipal User user) {
        Optional<Boolean> signed = telegramService.sendCode(user.getPhone(), code);

        if (!signed.isPresent()) {

        }
        return "telegram/formSendMessage";
    }

    @GetMapping("/telegram/sendMessage")
    public String getSendMessagePage() {
        return "telegram/formSendMessage";
    }

    @SneakyThrows
    @PostMapping("/telegram/sendMessage")
    public String sendMessage(@RequestParam("toPhone") String toPhone,
                              @RequestParam("textMessage") String textMessage,
                              Model model) {
        URIBuilder uriBuilder = new URIBuilder("http://35.204.168.100");
        uriBuilder.setPort(8000);
        uriBuilder.setPath("/send_message");
        HttpPost httpPost = new HttpPost(uriBuilder.build());
        httpPost.addHeader("Content-Type", "application/json");

        StringEntity stringEntity = new StringEntity("{\"phone\":\"" +
                this.number + "\",\"text\":\"" +
                textMessage + "\",\"to\":\"" +
                toPhone + "\"}");
        httpPost.setEntity(stringEntity);
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse httpResponse = httpClient.execute(httpPost);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        model.addAttribute("statusCode", statusCode);
        return "telegram/formSendMessage";
    }

    @SneakyThrows
    @PostMapping("/telegram/createChat")
    public String createChat(@RequestParam("members") Set<User> members,
                             @RequestParam("title") String title,
                             @AuthenticationPrincipal User user,
                             @RequestParam("project_id") Long projectId) {
        List<String> usersPhone = members.stream().map(User::getPhone).collect(Collectors.toList());
        Optional<Long> chat_id = this.telegramService.createChat(usersPhone, user.getPhone(), title);

        Project project = this.projectService.getProjectById(projectId);
        System.out.println(project);
        if (chat_id.isPresent()) {
            System.out.println(chat_id.get());
            Chat chat = Chat.builder().id(chat_id.get()).members(members).project(project).build();
            project.setChat(chat);
            this.chatRepository.save(chat);
            this.projectService.update(project);
        }
        String path = String.format("redirect:/project/messages/%s", project.getId());
        return (path);
    }

    @SneakyThrows
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://guest:guest@35.204.168.100");
        System.out.println('d');
        Connection conn = factory.newConnection();
        System.out.println("connec");
        Channel channel = conn.createChannel();

//        channel.queueDeclare("telegram", false, false, false, null);
//        channel.basicPublish("", "telegram-in",
//                new AMQP.BasicProperties.Builder()
//                        .contentType("application/json")
//                        .deliveryMode(2)
//                        .priority(1)
//                        .build(),
//                "{\"sender\":\"89030621009\",\"recipient\":\"79969527695\",\"text\":\"hallo from server\"}".getBytes());
//    }
    }
}
