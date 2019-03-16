package ru.itis.teamwork.services.creators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.teamwork.forms.CreateProjectForm;
import ru.itis.teamwork.models.Project;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.repositories.ProjectRepository;
import ru.itis.teamwork.repositories.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class CreationProjectService implements CreationService<CreateProjectForm> {
    private ProjectRepository projectRepository;
    private UserRepository userRepository;

    @Autowired
    public CreationProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean create(CreateProjectForm form) {
        User user = userRepository.findByUsername(form.getTeamLeaderLogin());

        Set<User> participants = new HashSet<>();
        participants.add(user);

        form.setName(form.getName().replace(" ", "_"));


        Project project = new Project();
        project.setName(form.getName());
        project.setDescription(form.getDescription());
        project.setTeamLeader(user);
        project.setUsers(participants);

        return !(projectRepository.save(project) == null);

    }
}
