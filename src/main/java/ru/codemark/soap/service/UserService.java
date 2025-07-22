package ru.codemark.soap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.codemark.soap.dto.*;
import ru.codemark.soap.entity.User;
import ru.codemark.soap.mapper.UserMapper;
import ru.codemark.soap.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private UserMapper userMapper;

    private <T extends BaseResponse> boolean handleValidationErrors(List<String> errors, T responseDto) {
        if (!errors.isEmpty()) {
            responseDto.setSuccess(false);
            ErrorList errorList = new ErrorList();
            errorList.getError().addAll(errors);
            responseDto.setErrors(errorList);
            return true;
        }
        return false;
    }

    public GetAllUsersResponse getAllUsers(GetAllUsersRequest requestDto) {
        GetAllUsersResponse responseDto = new GetAllUsersResponse();

        List<User> users = userRepository.findAll();

        for (User user : users) {
            UserInfo userInfo = userMapper.userToUserInfo(user, false);
            responseDto.getUser().add(userInfo);
        }

        responseDto.setSuccess(true);
        return responseDto;
    }

    @Transactional
    public GetUserResponse getUser(GetUserRequest requestDto) {
        GetUserResponse responseDto = new GetUserResponse();

        List<String> errors = new ArrayList<>();
        validationService.checkUserExists(requestDto.getUsername(), errors);

        if (handleValidationErrors(errors, responseDto)) {
            return responseDto;
        }

        User user = userRepository.findByUsername(requestDto.getUsername()).get();

        UserInfo userInfo = userMapper.userToUserInfo(user, true);
        responseDto.setUser(userInfo);
        responseDto.setSuccess(true);
        return responseDto;
    }

    @Transactional
    public CreateUserResponse createUser(CreateUserRequest requestDto) {
        CreateUserResponse responseDto = new CreateUserResponse();
        UserCreation userCreation = requestDto.getUser();

        List<String> errors = new ArrayList<>();
        validationService.validateUserCreation(userCreation, errors);

        if (handleValidationErrors(errors, responseDto)) {
            return responseDto;
        }

        User newUser = userMapper.userCreationToUser(userCreation);
        userRepository.save(newUser);

        responseDto.setSuccess(true);
        return responseDto;
    }

    @Transactional
    public UpdateUserResponse updateUser(UpdateUserRequest requestDto) {
        UpdateUserResponse responseDto = new UpdateUserResponse();
        UserUpdate userUpdate = requestDto.getUser();

        List<String> errors = new ArrayList<>();
        validationService.validateUserUpdate(userUpdate, errors);

        if (handleValidationErrors(errors, responseDto)) {
            return responseDto;
        }

        User existingUser = userRepository.findByUsername(userUpdate.getUsername()).get();
        userMapper.mergeUserUpdateToUser(userUpdate, existingUser);
        userRepository.save(existingUser);

        responseDto.setSuccess(true);
        return responseDto;
    }

    @Transactional
    public DeleteUserResponse deleteUser(DeleteUserRequest requestDto) {
        DeleteUserResponse responseDto = new DeleteUserResponse();

        List<String> errors = new ArrayList<>();
        validationService.checkUserExists(requestDto.getUsername(), errors);

        if (handleValidationErrors(errors, responseDto)) {
            return responseDto;
        }

        User userToDelete = userRepository.findByUsername(requestDto.getUsername()).get();
        userRepository.delete(userToDelete);

        responseDto.setSuccess(true);
        return responseDto;
    }
}