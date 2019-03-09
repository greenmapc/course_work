package ru.itis.teamwork.services.creators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.teamwork.forms.CreateProjectForm;
import ru.itis.teamwork.models.Project;
import ru.itis.teamwork.repositories.ProjectRepository;

@Service
public class CreationProjectService implements CreationService<CreateProjectForm> {
    private ProjectRepository projectRepository;

    @Autowired
    public CreationProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public boolean create(CreateProjectForm form) {
        Project project = Project.builder()
                .name(form.getName())
                .description(form.getDescription())
                .teamLeader(form.getTeamLeaderLogin())
                .users(form.getParticipants())
                .build();

        return !(projectRepository.save(project) == null);

    }
}
