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
    public String profilePage(@AuthenticationPrincipal User user,
                              Model model) {
        Set<Project> projects = user.getProjects();
        model.addAttribute("isCurrentUser", true);
        model.addAttribute("projects", projects);
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profileSettings")
    public String updateProfile(@AuthenticationPrincipal User user,
                                @RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam(required = false) String password,
                                @RequestParam(required = false) String password2,
                                /*@RequestParam(value = "file", required = false) MultipartFile file,*/
                                Model model) {
        if (password == null || password2 == null || password.equals("") || password2.equals("")) {

        } else if (!password.equals(password2)) {
            model.addAttribute("passwordError", "Password is not similar");
            return "profileSettings";
        } else {
            user.setPassword(password);
        }
        //System.out.println("File size: " + file.getSize());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        userService.updateInfo(user);
        return "redirect:/profile";
    }

    @GetMapping("/profileSettings")
    public String profileSettings(@AuthenticationPrincipal User user,
                                  Model model) {
        model.addAttribute("user", user);
        return "profileSettings";
    }

    @GetMapping("/profile/{userId}")
    public String profilePageAnotherUser(@AuthenticationPrincipal User user,
                                         @PathVariable String userId,
                                         Model model) {
        Long uId = Long.valueOf(userId);
        model.addAttribute("isCurrentUser", user.getId().equals(uId));
        if (user.getId().equals(uId)) {
            return "redirect:/profile";
        }
        Optional<User> anotherUserCandidate = userService.getUserById(uId);
        if (anotherUserCandidate.isPresent()) {
            model.addAttribute("user", anotherUserCandidate.get());
            model.addAttribute("projects", anotherUserCandidate.get().getProjects());
            return "profile";
        } else {
            return "redirect:/profile";
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
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
