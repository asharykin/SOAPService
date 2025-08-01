package ru.codemark.soap.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.codemark.soap.dto.*;
import ru.codemark.soap.service.UserService;

@Endpoint
@RequiredArgsConstructor
public class UserEndpoint {
    private static final String NAMESPACE_URI = "http://codemark.ru/soap/dto";

    private final UserService userService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllUsersRequest")
    @ResponsePayload
    public GetAllUsersResponse getAllUsers(@RequestPayload GetAllUsersRequest requestDto) {
        return userService.getAllUsers(requestDto);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserRequest")
    @ResponsePayload
    public GetUserResponse getUser(@RequestPayload GetUserRequest requestDto) {
        return userService.getUser(requestDto);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createUserRequest")
    @ResponsePayload
    public CreateUserResponse createUser(@RequestPayload CreateUserRequest requestDto) {
        return userService.createUser(requestDto);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateUserRequest")
    @ResponsePayload
    public UpdateUserResponse updateUser(@RequestPayload UpdateUserRequest requestDto) {
        return userService.updateUser(requestDto);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteUserRequest")
    @ResponsePayload
    public DeleteUserResponse deleteUser(@RequestPayload DeleteUserRequest requestDto) {
        return userService.deleteUser(requestDto);
    }
}
