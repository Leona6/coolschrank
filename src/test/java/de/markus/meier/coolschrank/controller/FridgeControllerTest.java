package de.markus.meier.coolschrank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.markus.meier.coolschrank.model.dto.FridgeDto;
import de.markus.meier.coolschrank.model.dto.FridgeInventoryDto;
import de.markus.meier.coolschrank.service.FridgeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {FridgeController.class})
public class FridgeControllerTest {

    @Autowired
    @InjectMocks
    private FridgeController fridgeController;

    @MockBean(name = "shoppingListService")
    private FridgeService fridgeService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(fridgeController)
                .build();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFridgeIsCreated() throws Exception {
        List<FridgeInventoryDto> fridgeInventoryDtoList = new ArrayList<>();
        FridgeDto fridgeDto = new FridgeDto("XXX", fridgeInventoryDtoList);
        String json = new ObjectMapper().writeValueAsString(fridgeDto);
        Mockito.when(fridgeService.createFridge()).thenReturn("XXX");
        mockMvc.perform(MockMvcRequestBuilders.post("/fridge/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value("XXX"))
                .andReturn();
    }

    @Test
    void testGetFridgeIsOk() throws Exception {
        List<FridgeInventoryDto> fridgeInventoryDtoList = new ArrayList<>();
        fridgeInventoryDtoList.add(new FridgeInventoryDto(1L, "Cola", 0.5f, 0.5f));
        FridgeDto fridgeDto = new FridgeDto("XXX", fridgeInventoryDtoList);
        Mockito.when(fridgeService.getFridge(any())).thenReturn(fridgeDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/fridge/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("XXX"))
                .andExpect(jsonPath("$.inventory[0].id").value(1L))
                .andExpect(jsonPath("$.inventory[0].name").value("Cola"))
                .andExpect(jsonPath("$.inventory[0].actual").value(0.5f))
                .andExpect(jsonPath("$.inventory[0].target").value(0.5f))
                .andReturn();
    }

    @Test
    void testGetFridgeIsNotFound() throws Exception {
        Mockito.when(fridgeService.getFridge(any())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/fridge/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void testAddNewItemIsCreated() throws Exception {
        FridgeInventoryDto fridgeInventoryDto = new FridgeInventoryDto(1l, "Cola", 0.5f, 0.5f);
        String json = new ObjectMapper().writeValueAsString(fridgeInventoryDto);
        Mockito.when(fridgeService.addFridgeInventory(any(), any())).thenReturn(fridgeInventoryDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/fridge/addInventory/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Cola"))
                .andExpect(jsonPath("$.actual").value(0.5f))
                .andExpect(jsonPath("$.target").value(0.5f))
                .andReturn();
    }

    @Test
    void testAddNewItemIsNotFound() throws Exception {
        FridgeInventoryDto fridgeInventoryDto = new FridgeInventoryDto(1l, "Cola", 0.5f, 0.5f);
        String json = new ObjectMapper().writeValueAsString(fridgeInventoryDto);
        Mockito.when(fridgeService.addFridgeInventory(any(), any())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/fridge/addInventory/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void testUpDateFridgeInventoryIsOk() throws Exception {
        FridgeInventoryDto fridgeInventoryDto = new FridgeInventoryDto(1l, "Cola", 0.5f, 0.5f);
        String json = new ObjectMapper().writeValueAsString(fridgeInventoryDto);
        Mockito.when(fridgeService.upDateFridgeInventory(any(), any())).thenReturn(fridgeInventoryDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/fridge/updateInventory/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Cola"))
                .andExpect(jsonPath("$.actual").value(0.5f))
                .andExpect(jsonPath("$.target").value(0.5f))
                .andReturn();
    }

    @Test
    void testUpDateFridgeInventoryIsNotFound() throws Exception {
        FridgeInventoryDto fridgeInventoryDto = new FridgeInventoryDto(1l, "Cola", 0.5f, 0.5f);
        String json = new ObjectMapper().writeValueAsString(fridgeInventoryDto);
        Mockito.when(fridgeService.upDateFridgeInventory(any(), any()))
                 .thenThrow(new WebClientResponseException(HttpStatus.NOT_FOUND.value(), "Not Found", null, null, null));
        mockMvc.perform(MockMvcRequestBuilders.put("/fridge/updateInventory/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

}
