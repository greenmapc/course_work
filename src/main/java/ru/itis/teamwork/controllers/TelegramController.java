package ru.itis.teamwork.controllers;

import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.itis.teamwork.controllers.util.ControllerUtils;
import ru.itis.teamwork.models.Chat;
import ru.itis.teamwork.models.Project;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.repositories.ChatRepository;
import ru.itis.teamwork.repositories.UserRepository;
import ru.itis.teamwork.services.ProjectService;
import ru.itis.teamwork.services.TelegramService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class TelegramController {
    private final TelegramService telegramService;

    private final ChatRepository chatRepository;

    private final ProjectService projectService;

    private final UserRepository userRepository;

    public TelegramController(TelegramService telegramService,
                              ChatRepository chatRepository,
                              ProjectService projectService,
                              UserRepository userRepository) {
        this.telegramService = telegramService;
        this.chatRepository = chatRepository;
        this.projectService = projectService;
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/telegram/connect", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    String getConnect(@AuthenticationPrincipal User user) {
        String phone = user.getPhone();
        if (phone == null) {
            phone = "Номер телефона";
        }
        return phone;
    }

    @SneakyThrows
    @PostMapping("/telegram/connect")
    public String connect(@RequestParam(value = "code", required = false) String phoneNumber,
                          @RequestParam("projectId") Project project,
                          @RequestParam(value = "buttonForm", required = false) String bf,
                          @AuthenticationPrincipal User user,
                          Model model) {
        model.addAttribute("project", project);
        model.addAttribute("user", user);

//        Set<User> members = project.getUsers();
//        members.removeIf(a -> (a.getTelegramJoined() == null || !a.getTelegramJoined()));
//        List<MessageDto> messageDtos = new ArrayList<>();
//        if (project.getChat() != null && project.getChat().getMessages() != null) {
//            messageDtos = project.getChat().getMessages().stream().map(MessageDto::new).collect(Collectors.toList());
//            model.addAttribute("chat", project.getChat());
//        }
//        model.addAttribute("messages", messageDtos);
//        members.remove(user);
//        model.addAttribute("members", members);

        if (bf != null && bf.equals("buttonForm")) {
            model.addAttribute("phoneForm", true);
            return "projectMessages";
        }
        if (phoneNumber != null && ControllerUtils.checkNumber(phoneNumber)) {
            Optional<Boolean> isConnected = telegramService.isConnectedUser(phoneNumber);

            if (isConnected.isPresent() && isConnected.get()) {
                user.setTelegramJoined(true);
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
                return "redirect:/project/messages/" + project.getId();
            } else {
                model.addAttribute("codeForm", true);
                return "projectMessages";
            }
        }
    }

    @PostMapping("/telegram/createChat")
    public String createChat(@RequestParam("members") Set<User> members,
                             @RequestParam("title") String title,
                             @AuthenticationPrincipal User user,
                             @RequestParam("project_id") Project project,
                             Model model) {
        List<String> usersPhone = members.stream()
                .map(User::getPhone)
                .collect(Collectors.toList());
        try {
            Optional<Long> chatId = telegramService.createChat(
                    usersPhone,
                    user.getPhone(),
                    title
            );

            if (chatId.isPresent()) {
                Chat chat = Chat.builder()
                        .id(chatId.get())
                        .members(members)
                        .project(project).build();
                project.setChat(chat);
                chatRepository.save(chat);
                projectService.update(project);
            }
        } catch (URISyntaxException | IOException e) {
            model.addAttribute("chatError", "Can't create chat:( We're already working on it");
            return "project/messages/" + project.getId();
        }
        return String.format("redirect:/project/messages/%s", project.getId());
    }
}