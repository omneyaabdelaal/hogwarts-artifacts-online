package com.omneya.hogwarts.hogwartsartifactsonline.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omneya.hogwarts.hogwartsartifactsonline.converters.UserConverter.UserDtoToUserConverter;
import com.omneya.hogwarts.hogwartsartifactsonline.converters.UserConverter.UserToUserDtoConverter;
import com.omneya.hogwarts.hogwartsartifactsonline.dto.UserDto;
import com.omneya.hogwarts.hogwartsartifactsonline.exceptions.ObjectNotFoundException;
import com.omneya.hogwarts.hogwartsartifactsonline.models.HogwartsUser;
import com.omneya.hogwarts.hogwartsartifactsonline.services.servicesImpl.UserServiceImpl;
import com.omneya.hogwarts.hogwartsartifactsonline.system.StatusCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @MockitoBean
    private UserServiceImpl userService;

    @Autowired
    ObjectMapper mapper ;

    @Value("${api.endpoint.base-url}")
    private String baseUrl;

    List<HogwartsUser> hogwartsUsers;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
            hogwartsUsers = new ArrayList<>();

            HogwartsUser u1 = new HogwartsUser();
            u1.setId(1L);
            u1.setUsername("john");
            u1.setPassword("123456");
            u1.setEnabled(true);
            u1.setRoles("admin user");


            HogwartsUser u2 = new HogwartsUser();
            u2.setId(2L);
            u2.setUsername("eric");
            u2.setPassword("654321");
            u2.setEnabled(true);
            u2.setRoles("user");

            HogwartsUser u3 = new HogwartsUser();
            u3.setId(3L);
            u3.setUsername("tom");
            u3.setPassword("qwerty");
            u3.setEnabled(false);
            u3.setRoles("user");

            hogwartsUsers.add(u1);
            hogwartsUsers.add(u2);
            hogwartsUsers.add(u3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllUsersSuccess() throws Exception {
        //given
        given(this.userService.findAll()).willReturn(this.hogwartsUsers);

        //when and then

        this.mockMvc.perform(get(baseUrl+"/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Users Success!"))
                .andExpect(jsonPath("$.data.size()").value(this.hogwartsUsers.size()));

    }

    @Test
    void testFindUserByIdSuccess() throws Exception {
        //given
        HogwartsUser u1 = new HogwartsUser();
        u1.setId(1L);
        u1.setUsername("john");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRoles("admin user");

        given(this.userService.findById(1L)).willReturn(u1);

        //when and then

        this.mockMvc.perform(get(baseUrl+"/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find User Success!"))
                .andExpect(jsonPath("$.data.id").value(1L));
        verify(userService,times(1)).findById(1L);


    }

    @Test
    void testFindUserByIdNonExistent() throws Exception {
        //Given
        given(this.userService.findById(1L)).willThrow(new ObjectNotFoundException("User",1L));

        //When and Then
        this.mockMvc.perform(get(baseUrl+"/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could Not Find User with Id 1 :(!"))
                .andExpect(jsonPath("$.data").isEmpty());
        verify(userService,times(1)).findById(1L);
    }
    @Test
    void testAddUserSuccess() throws Exception {
        HogwartsUser u1 = new HogwartsUser();
        u1.setId(1L);
        u1.setUsername("john");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRoles("admin user");
        String json=objectMapper.writeValueAsString(u1);

        given(this.userService.add(Mockito.any(HogwartsUser.class))).willReturn(u1);

        this.mockMvc.perform(post(baseUrl+"/users").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add User Success!"))
                .andExpect(jsonPath("$.data.id").value(1L));

    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        UserDto userDto = new UserDto(3L, "tom123", false, "user");

        HogwartsUser updatedUser = new HogwartsUser();
        updatedUser.setId(3L);
        updatedUser.setUsername("tom123"); // Username is changed. It was tom.
        updatedUser.setEnabled(false);
        updatedUser.setRoles("user");

        String json = this.objectMapper.writeValueAsString(userDto);

        // Given. Arrange inputs and targets. Define the behavior of Mock object userService.
        given(this.userService.update(eq(3L), Mockito.any(HogwartsUser.class))).willReturn(updatedUser);

        // When and then
        this.mockMvc.perform(put(this.baseUrl + "/users/3").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update User Success!"))
                .andExpect(jsonPath("$.data.id").value(3))
                .andExpect(jsonPath("$.data.username").value("tom123"))
                .andExpect(jsonPath("$.data.enabled").value(false))
                .andExpect(jsonPath("$.data.roles").value("user"));
    }


    @Test
    void testUpdateUserErrorWithNonExistentId() throws Exception {
        // Given. Arrange inputs and targets. Define the behavior of Mock object userService.
        given(this.userService.update(eq(5L), Mockito.any(HogwartsUser.class))).willThrow(new ObjectNotFoundException("User", 5L));

        UserDto userDto = new UserDto(5L, "tom123", false, "user");

        String json = this.objectMapper.writeValueAsString(userDto);

        // When and then
        this.mockMvc.perform(put(this.baseUrl + "/users/5").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could Not Find User with Id 5 :(!"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void deleteUser() throws Exception {
        doNothing().when(userService).delete(eq(1L));

        this.mockMvc.perform(delete(baseUrl+"/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete User Success!"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteUserErrorWithNonExistentId() throws Exception {
        // Given. Arrange inputs and targets. Define the behavior of Mock object userService.
        doThrow(new ObjectNotFoundException("User", 5L)).when(this.userService).delete(5L);

        // When and then
        this.mockMvc.perform(delete(this.baseUrl + "/users/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could Not Find User with Id 5 :(!"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}