package ru.itis.teamwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.services.UserService;
import ru.itis.teamwork.services.GitHubApi;

@Controller
public class HomeController {
    private UserService userService;

    @Autowired
    private GitHubApi gitHubApi;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registrationPage() {
        return "registration";
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
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @RequestMapping(value = "/gitAuth", method = RequestMethod.GET)
    public String gitAuth(){
        return "redirect:" + gitHubApi.getAuthLink();
    }

    @RequestMapping(value = "/gitCode", method = RequestMethod.GET)
    public String code(@RequestParam String code, ModelMap modelMap){
        String token = gitHubApi.getAccessToken(code);
        modelMap.put("email", gitHubApi.getUserEmail(token));
        modelMap.put("username", gitHubApi.getUsername(token));
        return "login";
    }

}