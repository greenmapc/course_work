package ru.itis.teamwork.controllers;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.models.UserMainImg;
import ru.itis.teamwork.services.UserService;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class UserController {
    private final UserService userService;

    @Value("${upload.path}")
    private String uploadPath;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profilePage(@AuthenticationPrincipal User user,
                              Model model) {
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@AuthenticationPrincipal User user,
                                @RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam String password,
                                @RequestParam String password2,
                                @RequestParam("file") MultipartFile file,
                                Model model) {
        if (StringUtils.isEmpty(password) || !password.equals(password2)) {
            model.addAttribute("passwordError", "Passwords is not similar");
            return "profile";
        }
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        userService.updateInfo(user);
        return "profile";
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
