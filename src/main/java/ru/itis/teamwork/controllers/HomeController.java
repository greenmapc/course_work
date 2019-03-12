package ru.itis.teamwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.itis.teamwork.controllers.util.ControllerUtils;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.services.UserService;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class HomeController {
    private UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registrationPage(@AuthenticationPrincipal User authUser) {
        if (authUser != null) {
            return "redirect:" + MvcUriComponentsBuilder.fromMappingName("HC#getUsers").build();
        }
        return "registration";
    }

    @GetMapping("/login")
    public String loginPage(@AuthenticationPrincipal User authUser) {
        if (authUser != null) {
            return "redirect:" + MvcUriComponentsBuilder.fromMappingName("HC#getUsers").build();
        }
        return "login";
    }

    @PostMapping("/registration")
    public String registerUser(@Valid User user,
                               @RequestParam("password2") String passwordConfirm,
                               BindingResult bindingResult,
                               Model model) {
        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);
        if (isConfirmEmpty) {
            model.addAttribute("password2Error", "Password confirmation cannot be empty");
        }
        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Passwords are different");
        }
        if (isConfirmEmpty || bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            return "registration";
        }
        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }
        return "redirect:" + MvcUriComponentsBuilder.fromMappingName("HC#loginPage").build();
    }

    @GetMapping("/addProject")
    public String addProject() {
        return "addProject";
    }

    @GetMapping("/projects")
    public String projects() {
        return "projects";
    }
}