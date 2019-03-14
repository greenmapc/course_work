package ru.itis.teamwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.itis.teamwork.forms.SignUpForm;
import ru.itis.teamwork.models.Project;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.services.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RegistrationController {
    private UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registrationPage(@AuthenticationPrincipal User authUser,
                                   Model model) {
        if (authUser != null) {
            return "redirect:" + MvcUriComponentsBuilder.fromMappingName("UC#profilePage").build();
        }

        model.addAttribute("form", new SignUpForm());
        return "registration";
    }

    @GetMapping("/login")
    public String loginPage(@AuthenticationPrincipal User authUser) {
        if (authUser != null) {
            return "redirect:" + MvcUriComponentsBuilder.fromMappingName("UC#profilePage").build();
        }
        return "login";
    }

    @PostMapping("/registration")
    public String registerUser(@Validated @ModelAttribute("form") SignUpForm form,
                               BindingResult bindingResult,
                               Model model) {
        System.out.println(form);
        //ToDO: поменяю на constraints
//        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);
//        if (isConfirmEmpty) {
//            model.addAttribute("password2Error", "Password confirmation cannot be empty");
//        }
//        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
//            model.addAttribute("passwordError", "Passwords are different");
//        }
//        if (isConfirmEmpty || bindingResult.hasErrors()) {
//            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
//            model.mergeAttributes(errorsMap);
//            return "registration";
//        }
        if (!userService.addUser(form)) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }
        return "redirect:" + MvcUriComponentsBuilder.fromMappingName("RC#loginPage").build();
    }
}