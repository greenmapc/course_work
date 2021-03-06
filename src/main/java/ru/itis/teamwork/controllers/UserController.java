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
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.models.UserMainImg;
import ru.itis.teamwork.services.ImageUploadService;
import ru.itis.teamwork.services.UserService;

import java.io.IOException;
import java.util.Optional;

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

    @PostMapping("/settings")
    public String updateProfile(@Validated @ModelAttribute("form") ProfileUpdForm form,
                                BindingResult bindingResult,
                                @AuthenticationPrincipal User user,
                                @RequestParam(value = "file", required = false) MultipartFile file,
                                Model model) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "settings";
        }

        Optional<UserMainImg> userMainImg = Optional.empty();
        try {
            userMainImg = imageUploadService.saveFile(user, file);
        } catch (IOException e) {
            //ToDo: catch norm exception
            e.printStackTrace();
        }

        userService.transferredFormToUser(form, user);

        if (userMainImg.isPresent()) {
            userService.updateInfo(user, userMainImg.get());
        } else {
            userService.updateInfo(user);
        }

        return "redirect:/profile/" + user.getUsername();
    }

    @GetMapping("/settings")
    public String profileSettings(@AuthenticationPrincipal User user,
                                  Model model) {
        model.addAttribute("user", user);
        model.addAttribute("form", ProfileUpdForm.buildFormByUser(user));

        return "profileSettings";
    }

    @GetMapping("/profile/{username}")
    public String profilePage(@AuthenticationPrincipal User user,
                                         @PathVariable String username,
                                         Model model) {
        model.addAttribute("isCurrentUser", user.getUsername().equals(username));
        model.addAttribute("user", userService.compareUser(user, username));

        return "profile";
    }
}
