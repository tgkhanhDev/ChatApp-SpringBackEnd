package com.chatApp.chatApp.controllers;

import com.chatApp.chatApp.dto.Response.AccountResponse;
import com.chatApp.chatApp.dto.request.AccountRequest;
import com.chatApp.chatApp.exception.AppException;
import com.chatApp.chatApp.exception.ErrorCode;
import com.chatApp.chatApp.services.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    //    i/o
    private AccountRequest accountRequest;
    private AccountResponse accountResponse;
    private LocalDate dob;

    @BeforeEach
    void initData() {
        dob = LocalDate.of(1990, 1, 1);

    }

    @Test
    void createAccount_success() throws Exception {
        accountRequest = AccountRequest.builder()
                .username("testAccount")
                .password("123123")
                .firstName("John")
                .lastName("Doe")
                .dob(dob)
                .build();
        accountResponse = AccountResponse.builder()
                .accountID("1213124134r")
                .username("testAccount")
                .firstName("John")
                .lastName("Doe")
                .dob(dob)
                .roles(Set.of("GUEST"))
                .build();
//        Given
        ObjectMapper objectMapper = new ObjectMapper();

        // Serialize LocaleDateTime bc it not understand LocalDate type
        objectMapper.registerModule(new JavaTimeModule());


        String content = objectMapper.writeValueAsString(accountRequest); // serialize response ve 1 chuoi String


        Mockito.when(accountService.registerAccount(ArgumentMatchers.any())).thenReturn(accountResponse);

//        When
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/account/createAccount")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(content) //Phai convert ve JSON nhe
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Create Account Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value(accountResponse.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.firstName").value(accountResponse.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.lastName").value(accountResponse.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.dob").value(dob.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles[0]").value("GUEST"))
                .andReturn();
        // Log the response content
//        String responseContent = result.getResponse().getContentAsString();
//        log.info("Response content: {}", responseContent); // Log the actual response

            //        Then
    }

    @Test
    void createAccount_fail_usernameExisted() throws Exception {
//        Given
        accountRequest = AccountRequest.builder()
                .username("testAccount")
                .password("123123")
                .firstName("John")
                .lastName("Doe")
                .dob(dob)
                .build();
        accountResponse = AccountResponse.builder()
                .accountID("1213124134r")
                .username("testAccount")
                .firstName("John")
                .lastName("Doe")
                .dob(dob)
                .roles(Set.of("GUEST"))
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        // Serialize LocaleDateTime bc it not understand LocalDate type
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(accountRequest); // serialize response ve 1 chuoi String
        Mockito.when(accountService.registerAccount(ArgumentMatchers.any())).thenThrow(new AppException(ErrorCode.USER_EXISTED));

//        When
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/account/createAccount")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(content)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.USER_EXISTED.getMessage()))
                .andReturn();
        //        Then
    }

    //WRONG ROLE -> Exception || Unauthenticated
//    @Test
//    void updateAccount_success() throws Exception {
//        // Given: Prepare the request and expected response
//        String accountId = "12345"; // The account ID path variable
//        AccountRequest accountRequest = AccountRequest.builder()
//                .username("updatedAccount")
//                .firstName("John_updated")
//                .lastName("Doe")
//                .dob(dob)
//                .roles(Set.of("GUEST","ADMIN"))
//                .build();
//
//        AccountResponse accountResponse = AccountResponse.builder()
//                .accountID(accountId)
//                .username("updatedAccount")
//                .firstName("John_updated")
//                .lastName("Doe")
//                .dob(dob)
//                .roles(Set.of("GUEST","ADMIN"))
//                .build();
//
//        // Serialize request to JSON
//        ObjectMapper objectMapper = new ObjectMapper();
//         objectMapper.registerModule(new JavaTimeModule()); // Register module for LocalDate/LocalDateTime
//        String content = objectMapper.writeValueAsString(accountRequest);
//
//        Mockito.when(accountService.updateAccount(ArgumentMatchers.any(AccountRequest.class), ArgumentMatchers.eq(accountId)))
//                .thenReturn(accountResponse);
//
////        When
//        MvcResult result = mockMvc.perform(
//                        MockMvcRequestBuilders
//                                .put("/updateAccount/{id}", accountId)
//                                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                                .content(content) //Phai convert ve JSON nhe
//                                .with(user("admin").authorities(new SimpleGrantedAuthority("UD_ACCOUNT"))) // Mock user with 'UD_ACCOUNT' authority
//                )
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Update Account Success"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value(accountResponse.getUsername()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.firstName").value(accountResponse.getFirstName()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.lastName").value(accountResponse.getLastName()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.dob").value(dob.toString()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles[0]").value("GUEST"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles[1]").value("ADMIN"))
//                .andReturn();
//        // Log the response content
////        String responseContent = result.getResponse().getContentAsString();
////        log.info("Response content: {}", responseContent); // Log the actual response
//
//        //        Then
//    }

}
