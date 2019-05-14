package ru.itis.teamwork.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.teamwork.forms.RegistrationForm;
import ru.itis.teamwork.models.Roles;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.models.UserMainImg;
import ru.itis.teamwork.repositories.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private ConfirmAccountService confirmAccountService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       ConfirmAccountService confirmAccountService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.confirmAccountService = confirmAccountService;
        this.passwordEncoder = passwordEncoder;
    }

    public Set<User> getUsers(List<Long> ids){
            Set<User> users = new HashSet<>();
            for (Long id: ids){
                Optional<User> user = userRepository.findById(id);
                user.ifPresent(users::add);
            }
            return users;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public Optional<User> getUserByTelegramId(Long telegramId){
        return userRepository.findUserByTelegramId(telegramId);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public boolean addUser(RegistrationForm userForm) {

        String confirmString = UUID.randomUUID().toString();

        userForm.setPassword(passwordEncoder.encode(userForm.getPassword()));
        userForm.setRoles(Collections.singleton(Roles.USER));

        User newUser = new User();
        newUser.setFirstName(userForm.getFirstName());
        newUser.setLastName(userForm.getLastName());
        newUser.setUsername(userForm.getUsername());
        newUser.setPassword(userForm.getPassword());
        newUser.setRoles(userForm.getRoles());
        newUser.setTelegramJoined(false);
        newUser.setEmail(userForm.getEmail());
        newUser.setConfirmString(confirmString);

        try {
            userRepository.save(newUser);
        } catch (DataIntegrityViolationException e) {
            return false;
        }
        confirmAccountService.sendMessage(confirmString, newUser.getEmail());
        return true;
    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public void updateInfo(User user, UserMainImg userMainImg) {
        user.setImg(userMainImg);
        userRepository.save(user);
    }

    public void updateInfo(User user) {
        userRepository.settingsUpdate(user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                user.getId(),
                user.getTelegramJoined(),
                user.getPhone());
    }

    public void saveUser(User user, Map<String, String> form) {
        Set<String> roles = Arrays.stream(Roles.values())
                .map(Roles::name)
                .collect(Collectors.toSet());
        user.getRoles().clear();
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Roles.valueOf(key));
            }
        }
        userRepository.save(user);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}
