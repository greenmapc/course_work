package ru.itis.teamwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.teamwork.forms.ProfileUpdForm;
import ru.itis.teamwork.models.Project;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.models.UserMainImg;
import ru.itis.teamwork.services.ImageUploadService;
import ru.itis.teamwork.services.UserService;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@Controller
public class UserController {

    private final UserService userService;
    private final ImageUploadService imageUploadService;

    @Autowired
    public UserController(UserService userService,
                          ImageUploadService imageUploadService) {
        this.userService = userService;
        this.imageUploadService = imageUploadService;
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
    public String updateProfile(@Validated @ModelAttribute("form") ProfileUpdForm form,
                                BindingResult bindingResult,
                                @AuthenticationPrincipal User user,
                                @RequestParam(value = "file", required = false) MultipartFile file,
                                Model model) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "profileSettings";
        }

        Optional<UserMainImg> userMainImg = Optional.empty();
        try {
            userMainImg = imageUploadService.saveFile(user, file);
        } catch (IOException e) {
            //ToDo: catch norm exception
            e.printStackTrace();
        }

//        ToDo: Password???
        userService.transferredFormToUser(form, user);

        if (userMainImg.isPresent()) {
            userService.updateInfo(user, userMainImg.get());
        } else {
            userService.updateInfo(user);
        }

        return "redirect:/profile";
    }

    @GetMapping("/profileSettings")
    public String profileSettings(@AuthenticationPrincipal User user,
                                  Model model) {
        model.addAttribute("user", user);
        model.addAttribute("form", ProfileUpdForm.buildFormByUser(user));

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
}
