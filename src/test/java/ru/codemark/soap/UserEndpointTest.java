package ru.codemark.soap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.webservices.server.WebServiceServerTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;
import ru.codemark.soap.dto.*;
import ru.codemark.soap.service.UserService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.*;

@WebServiceServerTest
public class UserEndpointTest {

    private static final String NAMESPACE_URI = "http://codemark.ru/soap/dto";

    @Autowired
    private MockWebServiceClient mockClient;

    @MockBean
    private UserService userService;

    @Test
    void testGetUserSuccess() throws IOException {
        String testUsername = "ivan_admin";
        String testName = "Ivan Ivanov";

        GetUserResponse mockUserServiceResponse = new GetUserResponse();
        mockUserServiceResponse.setSuccess(true);

        UserInfo userInfo = new UserInfo();
        userInfo.setName(testName);
        userInfo.setUsername(testUsername);

        RoleList roleList = new RoleList();
        roleList.getRole().addAll(Arrays.asList("admin", "operator"));
        userInfo.setRoles(roleList);

        mockUserServiceResponse.setUser(userInfo);

        when(userService.getUser(any(GetUserRequest.class))).thenReturn(mockUserServiceResponse);

        String requestXml =
                "<dto:getUserRequest xmlns:dto=\"" + NAMESPACE_URI + "\">" +
                        "   <dto:username>" + testUsername + "</dto:username>" +
                        "</dto:getUserRequest>";

        String expectedResponseXml =
                "<ns2:getUserResponse xmlns:ns2=\"" + NAMESPACE_URI + "\">" +
                        "   <ns2:success>true</ns2:success>" +
                        "   <ns2:user>" +
                        "      <ns2:username>" + testUsername + "</ns2:username>" +
                        "      <ns2:name>" + testName + "</ns2:name>" +
                        "      <ns2:roles>" +
                        "         <ns2:role>admin</ns2:role>" +
                        "         <ns2:role>operator</ns2:role>" +
                        "      </ns2:roles>" +
                        "   </ns2:user>" +
                        "</ns2:getUserResponse>";

        mockClient
                .sendRequest(withPayload(new StringSource(requestXml)))
                .andExpect(noFault())
                .andExpect(payload(new StringSource(expectedResponseXml)))
                .andExpect(xpath("/ns2:getUserResponse/ns2:success", Collections.singletonMap("ns2", NAMESPACE_URI)).evaluatesTo(true))
                .andExpect(xpath("/ns2:getUserResponse/ns2:user/ns2:username", Collections.singletonMap("ns2", NAMESPACE_URI)).evaluatesTo(testUsername))
                .andExpect(xpath("/ns2:getUserResponse/ns2:user/ns2:name", Collections.singletonMap("ns2", NAMESPACE_URI)).evaluatesTo(testName))
                .andExpect(xpath("count(/ns2:getUserResponse/ns2:user/ns2:roles/ns2:role)", Collections.singletonMap("ns2", NAMESPACE_URI)).evaluatesTo(2));
    }

    @Test
    void testGetUserNotFound() throws IOException {
        String testUsername = "non_existent_user";

        GetUserResponse mockUserServiceResponse = new GetUserResponse();
        mockUserServiceResponse.setSuccess(false);

        ErrorList errorList = new ErrorList();
        errorList.getError().add("User with username '" + testUsername + "' does not exist.");
        mockUserServiceResponse.setErrors(errorList);

        when(userService.getUser(any(GetUserRequest.class))).thenReturn(mockUserServiceResponse);

        String requestXml =
                "<dto:getUserRequest xmlns:dto=\"" + NAMESPACE_URI + "\">" +
                        "   <dto:username>" + testUsername + "</dto:username>" +
                        "</dto:getUserRequest>";

        String expectedResponseXml =
                "<ns2:getUserResponse xmlns:ns2=\"" + NAMESPACE_URI + "\">" +
                        "   <ns2:success>false</ns2:success>" +
                        "   <ns2:errors>" +
                        "      <ns2:error>User with username '" + testUsername + "' does not exist.</ns2:error>" +
                        "   </ns2:errors>" +
                        "</ns2:getUserResponse>";

        mockClient
                .sendRequest(withPayload(new StringSource(requestXml)))
                .andExpect(noFault())
                .andExpect(payload(new StringSource(expectedResponseXml)))
                .andExpect(xpath("/ns2:getUserResponse/ns2:errors/ns2:error", Collections.singletonMap("ns2", NAMESPACE_URI)).evaluatesTo("User with username 'non_existent_user' does not exist."))
                .andExpect(xpath("count(/ns2:getUserResponse/ns2:errors/ns2:error)", Collections.singletonMap("ns2", NAMESPACE_URI)).evaluatesTo(1));
    }
}
