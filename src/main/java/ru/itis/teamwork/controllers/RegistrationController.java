package ru.itis.teamwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.itis.teamwork.forms.RegistrationForm;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.services.ConfirmAccountService;
import ru.itis.teamwork.services.UserService;


@Controller
public class RegistrationController {
    private UserService userService;
    private ConfirmAccountService confirmAccountService;

    @Autowired
    public RegistrationController(UserService userService,
                                  ConfirmAccountService confirmAccountService) {
        this.userService = userService;
        this.confirmAccountService = confirmAccountService;
    }

    @GetMapping("/registration")
    public String registrationPage(@AuthenticationPrincipal User authUser,
                                   Model model) {
        if (authUser != null) {
            return "redirect:" + MvcUriComponentsBuilder.fromMappingName("UC#profile").arg(1, authUser.getUsername()).build();
        }

        model.addAttribute("form", new RegistrationForm());
        return "security/registration";
    }

    @GetMapping("/login")
    public String loginPage(@AuthenticationPrincipal User authUser) {
        if (authUser != null) {
            return "redirect:" + MvcUriComponentsBuilder.fromMappingName("UC#profilePage").arg(1, authUser.getUsername()).build();
        }
        return "security/login";
    }

    @PostMapping("/registration")
    public String registerUser(@Validated @ModelAttribute("form") RegistrationForm form,
                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "security/registration";
        }

        if (!userService.addUser(form)) {
            bindingResult.rejectValue("username", "exist.user");
            return "security/registration";
        }
        return "redirect:" + MvcUriComponentsBuilder.fromMappingName("RC#loginPage").build();
    }

    @GetMapping("/confirm/{secret}")
    public String confirmAccount(@PathVariable String secret) {
        confirmAccountService.confirmAccount(secret);
//        ToDo: information about activation account
        return "redirect:/login";
    }
}