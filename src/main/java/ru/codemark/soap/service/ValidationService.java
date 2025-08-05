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
        checkUserNameNotEmpty(userCreation.getUserName(), errors);
        checkFirstNameNotEmpty(userCreation.getFirstName(), errors);
        checkLastNameNotEmpty(userCreation.getLastName(), errors);
        checkPassword(userCreation.getPassword(), errors);

        if (errors.isEmpty()) {
            checkUserDoesNotExist(userCreation.getUserName(), errors);
            checkRolesExist(userCreation.getRoles().getRole(), errors);
        }
    }

    public void validateUserUpdate(UserUpdate userUpdate, List<String> errors) {
        if (userUpdate.getFirstName() != null) {
            checkFirstNameNotEmpty(userUpdate.getFirstName(), errors);
        }
        if (userUpdate.getLastName() != null) {
            checkLastNameNotEmpty(userUpdate.getLastName(), errors);
        }
        if (userUpdate.getPassword() != null) {
            checkPassword(userUpdate.getPassword(), errors);
        }
        if (userUpdate.getRoles() != null) {
            checkRolesExist(userUpdate.getRoles().getRole(), errors);
        }

        if (errors.isEmpty()) {
            checkUserExists(userUpdate.getUserName(), errors);
        }
    }

    public void checkUserNameNotEmpty(String userName, List<String> errors) {
        if (userName == null || userName.trim().isEmpty()) {
            errors.add("User name cannot be empty.");
        }
    }

    public void checkFirstNameNotEmpty(String firstName, List<String> errors) {
        if (firstName == null || firstName.trim().isEmpty()) {
            errors.add("First name cannot be empty.");
        }
    }

    public void checkLastNameNotEmpty(String lastName, List<String> errors) {
        if (lastName == null || lastName.trim().isEmpty()) {
            errors.add("Last name cannot be empty.");
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

    public void checkUserExists(String userName, List<String> errors) {
        if (userRepository.findByUserName(userName).isEmpty()) {
            errors.add("User with user name '" + userName + "' does not exist.");
        }
    }

    public void checkUserDoesNotExist(String userName, List<String> errors) {
        if (userRepository.findByUserName(userName).isPresent()) {
            errors.add("User with user name '" + userName + "' already exists.");
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