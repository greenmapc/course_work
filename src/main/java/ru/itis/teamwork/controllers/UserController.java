package ru.itis.teamwork.controllers;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.teamwork.models.Project;
import ru.itis.teamwork.models.Roles;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.models.UserMainImg;
import ru.itis.teamwork.services.UserService;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class UserController {
    private final UserService userService;

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profilePage(Model model) {
        Project project1 = Project.builder()
                .id(1l)
                .name("course-work")
                .build();
        Project project2 = Project.builder()
                .id(2l)
                .name("java-work")
                .build();
        Project project3 = Project.builder()
                .id(3l)
                .name("spring-twitter")
                .build();
        Project project4 = Project.builder()
                .id(4l)
                .name("hometask")
                .build();
        List<Project> projects = new ArrayList<>();
        projects.add(project1);
        projects.add(project2);
        projects.add(project3);
        projects.add(project4);
        model.addAttribute("projects", projects);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@AuthenticationPrincipal User user,
                                @RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam String password,
                                @RequestParam String password2,
                                @RequestParam("file") MultipartFile file,
                                Model model) {
        if (StringUtils.isEmpty(password) || !password.equals(password2)) {
            model.addAttribute("passwordError", "Passwords is not similar");
            return "profile";
        }
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        userService.updateInfo(user);
        return "profile";
    }

    @GetMapping("/user/{userId}")
    public String userEditForm(@PathVariable String userId,
                               Model model) {
        Optional<User> userCandidate = userService.getUserById(Long.valueOf(userId));
        if (userCandidate.isPresent()) {
            model.addAttribute("user", userCandidate.get());
            model.addAttribute("roles", Roles.values());
            return "userEdit";
        } else {
            return "userList";
        }
    }

    @GetMapping("/user")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "userList";
    }

    @PostMapping("/user")
    public String saveUser(@RequestParam String username,
                           @RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam Map<String, String> form,
                           @RequestParam("userId") String userId) {
        Optional<User> userCandidate = userService.getUserById(Long.valueOf(userId));
        if (userCandidate.isPresent()) {
            User user = userCandidate.get();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUsername(username);
            userService.saveUser(user, form);
        }
        return "redirect:/user";
    }

    private void saveFile(User user, MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            UserMainImg userMainImg = new UserMainImg();
            userMainImg.setHashName(resultFilename);
            userMainImg.setOriginalName(file.getOriginalFilename());
            userMainImg.setType(FilenameUtils.getExtension(file.getOriginalFilename()));
            user.setImg(userMainImg);
        }
    }
}
