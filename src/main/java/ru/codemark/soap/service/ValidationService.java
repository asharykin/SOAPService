package ru.codemark.soap.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.codemark.soap.dto.UserCreation;
import ru.codemark.soap.dto.UserUpdate;
import ru.codemark.soap.repository.RoleRepository;
import ru.codemark.soap.repository.UserRepository;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ValidationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public void validateUserCreation(UserCreation userCreation, List<String> errors) {
        checkUsernameNotEmpty(userCreation.getUsername(), errors);
        checkNameNotEmpty(userCreation.getName(), errors);
        checkPassword(userCreation.getPassword(), errors);

        if (errors.isEmpty()) {
            checkUserDoesNotExist(userCreation.getUsername(), errors);
            checkRolesExist(userCreation.getRoles().getRole(), errors);
        }
    }

    public void validateUserUpdate(UserUpdate userUpdate, List<String> errors) {
        if (userUpdate.getName() != null) {
            checkNameNotEmpty(userUpdate.getName(), errors);
        }
        if (userUpdate.getPassword() != null) {
            checkPassword(userUpdate.getPassword(), errors);
        }
        if (userUpdate.getRoles() != null) {
            checkRolesExist(userUpdate.getRoles().getRole(), errors);
        }

        if (errors.isEmpty()) {
            checkUserExists(userUpdate.getUsername(), errors);
        }
    }

    public void checkUsernameNotEmpty(String username, List<String> errors) {
        if (username == null || username.trim().isEmpty()) {
            errors.add("Username cannot be empty.");
        }
    }

    public void checkNameNotEmpty(String name, List<String> errors) {
        if (name == null || name.trim().isEmpty()) {
            errors.add("Name cannot be empty.");
        }
    }

    public void checkPasswordNotEmpty(String password, List<String> errors) {
        if (password == null || password.trim().isEmpty()) {
            errors.add("Password cannot be empty.");
        }
    }

    public void checkPassword(String password, List<String> errors) {
        checkPasswordNotEmpty(password, errors);

        Pattern upperCasePattern = Pattern.compile("[A-Z]");
        Pattern digitPattern = Pattern.compile("[0-9]");

        Matcher upperCaseMatcher = upperCasePattern.matcher(password);
        Matcher digitMatcher = digitPattern.matcher(password);

        if (!upperCaseMatcher.find()) {
            errors.add("Password must contain at least one uppercase letter.");
        }
        if (!digitMatcher.find()) {
            errors.add("Password must contain at least one digit.");
        }
    }

    public void checkUserExists(String username, List<String> errors) {
        if (errors.isEmpty() && userRepository.findByUsername(username).isEmpty()) {
            errors.add("User with username '" + username + "' does not exist.");
        }
    }

    public void checkUserDoesNotExist(String username, List<String> errors) {
        if (userRepository.findByUsername(username).isPresent()) {
            errors.add("User with username '" + username + "' already exists.");
        }
    }

    public void checkRolesExist(List<String> roleNames, List<String> errors) {
        for (String roleName : roleNames) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                errors.add("Role '" + roleName + "' does not exist.");
            }
        }
    }
}