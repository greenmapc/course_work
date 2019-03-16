package ru.itis.teamwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.services.UserService;
import ru.itis.teamwork.services.githubApi.GitHubApi;
import ru.itis.teamwork.services.githubApi.GitHubScope;

import javax.validation.Valid;

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
    public String registrationPage(@AuthenticationPrincipal User authUser) {
        if (authUser != null) {
            return "redirect:" + MvcUriComponentsBuilder.fromMappingName("HC#getUsers").build();
        }
        return "registration";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(@AuthenticationPrincipal User authUser) {
        if (authUser != null) {
            return "redirect:" + MvcUriComponentsBuilder.fromMappingName("HC#getUsers").build();
        }
        return "login";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registerUser(@Valid User user,
                               Model model) {
        if (!userService.addUser(user)) {
            model.addAttribute("message", "Registration error. See log for details");
            return "registration";
        }
        return "redirect:" + MvcUriComponentsBuilder.fromMappingName("HC#loginPage").build();
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String getUsers(@AuthenticationPrincipal User user,
                           Model model) {
        model.addAttribute("user", user);
        return "users";
    }

    @RequestMapping(value = "/gitAuth", method = RequestMethod.GET)
    public String gitAuth(){
        return "redirect:" + gitHubApi.getAuthLink(GitHubScope.getFullAccess());
    }

    @RequestMapping(value = "/gitCode", method = RequestMethod.GET)
    public String code(@RequestParam String code, ModelMap modelMap){
        String token = gitHubApi.getAccessToken(code);
        modelMap.put("email", gitHubApi.getUserEmail(token));
        modelMap.put("username", gitHubApi.getUsername(token));
        return "login";
    }

}