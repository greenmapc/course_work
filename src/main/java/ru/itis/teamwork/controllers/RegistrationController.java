package ru.itis.teamwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.itis.teamwork.forms.RegistrationForm;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.services.UserService;


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

        model.addAttribute("form", new RegistrationForm());
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
    public String registerUser(@Validated @ModelAttribute("form") RegistrationForm form,
                               BindingResult bindingResult,
                               Model model) {

        if(bindingResult.hasErrors()) {
            return "registration";
        }

        if (!userService.addUser(form)) {
            bindingResult.rejectValue( "username", "exist.user");
            return "registration";
        }
        return "redirect:" + MvcUriComponentsBuilder.fromMappingName("RC#loginPage").build();
    }
}