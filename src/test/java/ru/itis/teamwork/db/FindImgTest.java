package ru.itis.teamwork.db;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.itis.teamwork.config.RootConfig;
import ru.itis.teamwork.forms.CreateProjectForm;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.models.UserMainImg;
import ru.itis.teamwork.repositories.UserMainImgRepository;
import ru.itis.teamwork.repositories.UserRepository;
import ru.itis.teamwork.services.creators.CreationService;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(
        classes = {RootConfig.class}
)
public class FindImgTest {

    @Resource
    private UserMainImgRepository userMainImgRepository;

    @Test
    public void findOneById() {
        UserMainImg userMainImg = new UserMainImg();
        userMainImg.setHashName("m_Dfz3laIT8");
        userMainImg.setOriginalName("A");
        userMainImg.setType(".jpg");

        UserMainImg result = userMainImgRepository.save(userMainImg);
        assertEquals("/WEB-INF/userData/mainImg/", result.getPath());
    }

}
