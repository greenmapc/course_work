package ru.itis.teamwork.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.teamwork.forms.CreateProjectForm;
import ru.itis.teamwork.models.Message;
import ru.itis.teamwork.models.Project;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.models.dto.MembersDto;
import ru.itis.teamwork.models.dto.MessageDto;
import ru.itis.teamwork.models.dto.UserDto;
import ru.itis.teamwork.repositories.MessageRepository;
import ru.itis.teamwork.repositories.ProjectRepository;
import ru.itis.teamwork.repositories.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository,
                          UserRepository userRepository,
                          MessageRepository messageRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    public List<Project> findProjectsByUser(User user) {
        return new ArrayList<>();
    }

    public Project create(CreateProjectForm form) {
        User user = userRepository.findByUsername(form.getTeamLeaderLogin());

        Set<User> participants = new HashSet<>();
        participants.add(user);

        form.setName(form.getName().replace(" ", "_"));

        Project project = new Project();
        project.setName(form.getName());
        project.setDescription(form.getDescription());
        project.setTeamLeader(user);
        project.setUsers(participants);

        return projectRepository.save(project);
    }

    public Project getProjectById(Long id) {
        Optional<Project> candidate = projectRepository.findById(id);
        return candidate.orElse(null);
    }

    public boolean addMember(Project project, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        project.getUsers().add(user);
        projectRepository.save(project);
        user.getProjects().add(project);
        userRepository.save(user);
        return true;
    }

    public Set<User> getTelegramJoinedUser(Project project) {
        Set<User> users = project.getUsers();
        users.removeIf(a -> (a.getTelegramJoined() == null || !a.getTelegramJoined()));
        return users;
    }

    public List<MessageDto> getProjectMessages(Project project) {
        List<MessageDto> messageDtos = new ArrayList<>();
        if (project.getChat() != null && project.getChat().getMessages() != null) {
            Set<Message> messages = messageRepository.findAllByChatOrderByDate(project.getChat());
            messageDtos = messages
                    .stream()
                    .map(MessageDto::new)
                    .collect(Collectors.toList());

        }
        return messageDtos;
    }

    public MembersDto getUsersLike(String username) {
        List<User> users = userRepository.findLikeUsername(username + "%");
        List<UserDto> userDtos = users.stream().map(UserDto::new).collect(Collectors.toList());
        return MembersDto.builder().userDtoList(userDtos).build();
    }

    public void update(Project project) {
        this.projectRepository.save(project);
    }
}
