package test.java.ru.itis.teamwork;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.itis.teamwork.config.*;
import ru.itis.teamwork.forms.CreateProjectForm;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.repositories.UserRepository;
import ru.itis.teamwork.services.creators.CreationService;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
            classes = {RootConfig.class}
        )
public class CreationProjectServiceTest {
    @Resource
    private UserRepository userRepository;
    @Autowired
    private CreationService createProjectService;

    @Test
    public void create() {
        User teamLeader = userRepository.findByUsername("greenmapc");
        User firstUser = userRepository.findByUsername("merenaas");

        Set<User> users = new HashSet<>();
        users.add(teamLeader);
        users.add(firstUser);


        CreateProjectForm createProjectForm = CreateProjectForm.builder()
                .description("good project!")
                .name("ok project")
                .teamLeaderLogin(teamLeader)
                .participants(users)
                .build();

        assertTrue(createProjectService.create(createProjectForm));
    }
}