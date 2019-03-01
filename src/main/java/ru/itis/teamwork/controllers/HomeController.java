package ru.itis.teamwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.services.UserService;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

@Controller
public class HomeController {
    private UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registrationPage() {
        return "registration";
    }

    @RequestMapping
    public String loginPage() {
        return "login";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registerUser(@RequestParam String firstName,
                               @RequestParam String lastName,
                               @RequestParam String username,
                               @RequestParam String password,
                               Model model) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPassword(password);
        if (!userService.addUser(user)) {
            model.addAttribute("message", "Registration error. See log for details");
            return "registration";
        }
        return "redirect:/login";
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String getUsers(Model model) {
        //model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

}