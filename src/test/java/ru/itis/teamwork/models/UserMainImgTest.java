package ru.itis.teamwork.models;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.itis.teamwork.config.RootConfig;
import ru.itis.teamwork.forms.CreateProjectForm;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.repositories.UserMainImgRepository;
import ru.itis.teamwork.repositories.UserRepository;
import ru.itis.teamwork.services.creators.CreationService;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(
        classes = {RootConfig.class}
)
public class UserMainImgTest {
    @Resource
    private UserMainImgRepository userMainImgRepository;

    @Test
    public void getFullName() {
        System.out.println(userMainImgRepository.findById(1L).get().getFullName());
    }
}