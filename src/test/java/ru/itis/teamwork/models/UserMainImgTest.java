package ru.itis.teamwork.models;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.itis.teamwork.config.RootConfig;
import ru.itis.teamwork.repositories.UserMainImgRepository;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@ContextConfiguration(
        classes = {RootConfig.class}
)
public class UserMainImgTest {
    @Resource
    private UserMainImgRepository userMainImgRepository;

    @Test
    @Ignore
    public void getFullName() {
        System.out.println(userMainImgRepository.findById(1L).get().getFullName());
    }
}