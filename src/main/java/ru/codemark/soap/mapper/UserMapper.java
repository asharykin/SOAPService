package ru.codemark.soap.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.codemark.soap.dto.RoleList;
import ru.codemark.soap.dto.UserCreation;
import ru.codemark.soap.dto.UserInfo;
import ru.codemark.soap.dto.UserUpdate;
import ru.codemark.soap.entity.Role;
import ru.codemark.soap.entity.User;
import ru.codemark.soap.repository.RoleRepository;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final RoleRepository roleRepository;

    public UserInfo userToUserInfo(User user, boolean withRoles) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(user.getUserName());
        userInfo.setFirstName(user.getFirstName());
        userInfo.setLastName(user.getLastName());

        if (withRoles) {
            RoleList roleList = new RoleList();
            for (Role role : user.getRoles()) {
                roleList.getRole().add(role.getName());
            }
            userInfo.setRoles(roleList);
        }

        return userInfo;
    }

    public User userCreationToUser(UserCreation userCreation) {
        User user = new User();
        user.setUserName(userCreation.getUserName());
        user.setFirstName(userCreation.getFirstName());
        user.setLastName(userCreation.getLastName());
        user.setPassword(userCreation.getPassword());

        for (String roleName : userCreation.getRoles().getRole()) {
            Role role = roleRepository.findByName(roleName.toLowerCase()).get();
            user.getRoles().add(role);
        }
        return user;
    }

    public void mergeUserUpdateToUser(UserUpdate userUpdate, User existingUser) {
        if (userUpdate.getFirstName() != null) {
            existingUser.setFirstName(userUpdate.getFirstName());
        }
        if (userUpdate.getLastName() != null) {
            existingUser.setLastName(userUpdate.getLastName());
        }
        if (userUpdate.getPassword() != null) {
            existingUser.setPassword(userUpdate.getPassword());
        }

        if (userUpdate.getRoles() != null) {
            existingUser.getRoles().clear();

            for (String roleName : userUpdate.getRoles().getRole()) {
                Role role = roleRepository.findByName(roleName.toLowerCase()).get();
                existingUser.getRoles().add(role);
            }
        }
    }
}

