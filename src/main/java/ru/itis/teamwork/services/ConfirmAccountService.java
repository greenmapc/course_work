package ru.itis.teamwork.services;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import ru.itis.teamwork.repositories.UserRepository;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ConfirmAccountService {

    private EmailService emailService;
    private Configuration freemarkerConfig;
    private UserRepository userRepository;

    public ConfirmAccountService(EmailService emailService,
                                 Configuration freemarkerConfig,
                                 UserRepository userRepository) {
        this.emailService = emailService;
        this.freemarkerConfig = freemarkerConfig;
        this.userRepository = userRepository;
    }


    public void sendMessage(String secret, String emailTo) {
        Map args = new HashMap();
        args.put("link", "http://localhost:9000/confirm/" + secret);

//        ToDo: exception
        try {
            Template emailTemplate = freemarkerConfig.getTemplate("email/registration_confirmation.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(emailTemplate, args);
            emailService.sendMail("Подтвреждение регистрации", html, emailTo);
        }
        catch (IOException | TemplateException e) {
            System.out.println("Can not send mail");
        }

    }

    public void confirmAccount(String secret) {
//        ToDo: create timer for activation link
        userRepository.activeAccount(secret);
    }
}
