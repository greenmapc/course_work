package ru.itis.teamwork.controllers;

import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
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
    public String connect(@RequestParam("projectId") Project project,
                          @AuthenticationPrincipal User user,
                          Model model) {
        model.addAttribute("project", project);
        model.addAttribute("user", user);

        String phoneValue = user.getPhone() != null ? user.getPhone() : "Номер телефона";
        model.addAttribute("connectionForm", true);
        model.addAttribute("inputValue", phoneValue);
        model.addAttribute("inputName", "phone");
        return "projectMessages";

    }

    @PostMapping("/telegram/connect/phone")
    @SneakyThrows
    public String sendPhone(@RequestParam(value = "phone") String phoneNumber,
                            @RequestParam("projectId") Project project,
                            @AuthenticationPrincipal User user,
                            ModelMap model) {
        model.addAttribute("project", project);
        model.addAttribute("user", user);

        if (phoneNumber != null && ControllerUtils.checkNumber(phoneNumber)) {
            user.setPhone(phoneNumber);
            Optional<Boolean> isConnected = telegramService.isConnectedUser(phoneNumber);

            if (isConnected.isPresent() && isConnected.get()) {

                Long telegramId = telegramService.getTelegramId(user);
                if (telegramId != null) {
                    user.setTelegramJoined(true);
                    user.setTelegramId(telegramId);
                } else {
                    return "projectMessages";
                }

                return "redirect:/project/messages/" + project.getId();
            } else if (isConnected.isPresent()) {
                model.addAttribute("connectionForm", true);
                model.addAttribute("inputName", "code");
            }
            userRepository.save(user);
            return "projectMessages";
        } else {
            return "redirect:/telegram/connect";
        }
    }

    @PostMapping("/telegram/connect/code")
    @SneakyThrows
    public String sendCode(@RequestParam(value = "code") String code,
                           @RequestParam("projectId") Project project,
                           @AuthenticationPrincipal User user,
                           ModelMap model) {
        model.addAttribute("project", project);
        model.addAttribute("user", user);

        Optional<Boolean> authorized = telegramService.sendCode(user.getPhone(), code);
        if (authorized.isPresent() && authorized.get()) {
            user.setTelegramJoined(true);
            user.setTelegramId(telegramService.getTelegramId(user));
            userRepository.save(user);
        } else if (authorized.isPresent()) {

            model.addAttribute("connectionForm", true);
            model.addAttribute("inputName", "code");
        }
        return "redirect:/project/messages/" + project.getId();
    }

    @PostMapping("/telegram/createChat")
    public String createChat(@RequestParam("members") Set<User> members,
                             @RequestParam("title") String title,
                             @AuthenticationPrincipal User user,
                             @RequestParam("project_id") Project project,
                             Model model) {
        List<Long> usersTelegramId = members.stream()
                .map(User::getTelegramId)
                .collect(Collectors.toList());
        try {
            Optional<Long> chatId = telegramService.createChat(
                    usersTelegramId,
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
            return "redirect:/project/messages/" + project.getId();
        }
        return String.format("redirect:/project/messages/%s", project.getId());
    }
}