package de.markus.meier.coolschrank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.markus.meier.coolschrank.model.dto.UserDto;
import de.markus.meier.coolschrank.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {UserController.class})
public class UserControllerTest {

    @Autowired
    @InjectMocks
    private UserController userController;

    @MockBean(name = "userService")
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.
                standaloneSetup(userController)
                .build();
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testCreateUserIsOk() throws Exception {
        Long userId = 1L;
        UserDto userDto = new UserDto(userId, "Markus", "test123", null);
        String json = new ObjectMapper().writeValueAsString(userDto);
        Mockito.when(userService.createUser(any())).thenReturn(1L);
        mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(userId))
                .andReturn();
    }

    @Test
    void testGetUserIsOk() throws Exception {
        UserDto userDto = new UserDto(1L, "Markus", "test123", "XXX");
        Mockito.when(userService.getUser(any())).thenReturn(userDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/user/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("Markus"))
                .andExpect(jsonPath("$.password").value("test123"))
                .andExpect(jsonPath("$.fridgeId").value("XXX"))
                .andReturn();
    }

    @Test
    void testGetUserNotFound() throws Exception {
        Mockito.when(userService.getUser(any())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/user/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void testUpdateFrigdeIdIsOk() throws Exception {
        UserDto userDto = new UserDto(1L, "Markus", "test123", "XXX");
        Mockito.when(userService.updateFrigdeId(any(), any())).thenReturn(userDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/user/1/frigde/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("Markus"))
                .andExpect(jsonPath("$.password").value("test123"))
                .andExpect(jsonPath("$.fridgeId").value("XXX"))
                .andReturn();
    }


    @Test
    void testUpdateFrigdeIdNotFound() throws Exception {
        Mockito.when(userService.getUser(any())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.put("/user/1/fridge/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
