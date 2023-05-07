package de.markus.meier.coolschrank.controller;

import de.markus.meier.coolschrank.model.dto.ShoppingInventoryDto;
import de.markus.meier.coolschrank.model.dto.ShoppingListDto;
import de.markus.meier.coolschrank.service.ShoppingListService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {ShoppingListController.class})
public class ShoppingListControllerTest {

    @Autowired
    @InjectMocks
    private ShoppingListController shoppingListController;

    @MockBean(name = "shoppingListService")
    private ShoppingListService shoppingListService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.
                standaloneSetup(shoppingListController)
                .build();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetShoppingListIsOk() throws Exception {
        List<ShoppingInventoryDto> shoppingInventoryDtoList = new ArrayList<>();
        shoppingInventoryDtoList.add(new ShoppingInventoryDto(1l, 1L, "Name", 0.5f));
        ShoppingListDto shoppingListDto = new ShoppingListDto(1L, "XXX", shoppingInventoryDtoList);
        Mockito.when(shoppingListService.getShoppingList(any())).thenReturn(shoppingListDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/shoppingList/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fridgeId").value("XXX"))
                .andExpect(jsonPath("$.shoppingInventoryDtoList[0].id").value(1L))
                .andExpect(jsonPath("$.shoppingInventoryDtoList[0].fridgeInventoryId").value(1L))
                .andExpect(jsonPath("$.shoppingInventoryDtoList[0].name").value("Name"))
                .andExpect(jsonPath("$.shoppingInventoryDtoList[0].amount").value(0.5f))
                .andReturn();
    }

    @Test
    void testGetShoppingListNotFound() throws Exception {
        Mockito.when(shoppingListService.getShoppingList(any())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/shoppingList/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void testCreateShoppingListIsCreated() throws Exception {
        List<ShoppingInventoryDto> shoppingInventoryDtoList = new ArrayList<>();
        shoppingInventoryDtoList.add(new ShoppingInventoryDto(1l, 1L, "Name", 0.5f));
        ShoppingListDto shoppingListDto = new ShoppingListDto(1L, "XXX", shoppingInventoryDtoList);
        Mockito.when(shoppingListService.createShoppingList(any())).thenReturn(shoppingListDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/shoppingList/create/fridge/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fridgeId").value("XXX"))
                .andExpect(jsonPath("$.shoppingInventoryDtoList[0].id").value(1L))
                .andExpect(jsonPath("$.shoppingInventoryDtoList[0].fridgeInventoryId").value(1L))
                .andExpect(jsonPath("$.shoppingInventoryDtoList[0].name").value("Name"))
                .andExpect(jsonPath("$.shoppingInventoryDtoList[0].amount").value(0.5f))
                .andReturn();
    }

    @Test
    void testCreateShoppingListIsConflict() throws Exception {
        Mockito.when(shoppingListService.createShoppingList(any())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/shoppingList/create/fridge/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andReturn();
    }

    @Test
    void testAutoUpdateShoppingListIsOk() throws Exception {
        List<ShoppingInventoryDto> shoppingInventoryDtoList = new ArrayList<>();
        shoppingInventoryDtoList.add(new ShoppingInventoryDto(1L, 1L, "Cola", 0.5f));
        ShoppingListDto shoppingListDto = new ShoppingListDto(1l, "XXX", shoppingInventoryDtoList);

        Mockito.when(shoppingListService.autoUpdateShoppingList(any(), any())).thenReturn(shoppingListDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/shoppingList/update/1/fridge/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fridgeId").value("XXX"))
                .andExpect(jsonPath("$.shoppingInventoryDtoList[0].id").value(1L))
                .andExpect(jsonPath("$.shoppingInventoryDtoList[0].fridgeInventoryId").value(1L))
                .andExpect(jsonPath("$.shoppingInventoryDtoList[0].name").value("Cola"))
                .andExpect(jsonPath("$.shoppingInventoryDtoList[0].amount").value(0.5f))
                .andReturn();
    }

    @Test
    void testDeleteShoppingInventoryIsNotFound() throws Exception {

        Mockito.when(shoppingListService.deleteShoppingInventory(any(), any())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.delete("/shoppingList/delete/1/item/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void testDeleteShoppingInventoryIsOk() throws Exception {
        ShoppingInventoryDto shoppingInventoryDto = new ShoppingInventoryDto(1L, 1L, "Cola", 0.5f);
        Mockito.when(shoppingListService.deleteShoppingInventory(any(), any())).thenReturn(shoppingInventoryDto);
        mockMvc.perform(MockMvcRequestBuilders.delete("/shoppingList/delete/1/item/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fridgeInventoryId").value(1L))
                .andExpect(jsonPath("$.name").value("Cola"))
                .andExpect(jsonPath("$.amount").value(0.5f))
                .andReturn();
    }

    @Test
    void testDeleteShoppingListIsNotFound() throws Exception {
        Mockito.when(shoppingListService.deleteShoppingList(any())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.delete("/shoppingList/delete/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void testDeleteShoppingListIsOk() throws Exception {
        List<ShoppingInventoryDto> shoppingInventoryDtoList = new ArrayList<>();
        shoppingInventoryDtoList.add(new ShoppingInventoryDto(1L, 1L, "Cola", 0.5f));
        ShoppingListDto shoppingListDto = new ShoppingListDto(1l, "XXX", shoppingInventoryDtoList);

        Mockito.when(shoppingListService.deleteShoppingList(any())).thenReturn(shoppingListDto);
        mockMvc.perform(MockMvcRequestBuilders.delete("/shoppingList/delete/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fridgeId").value("XXX"))
                .andExpect(jsonPath("$.shoppingInventoryDtoList[0].id").value(1L))
                .andExpect(jsonPath("$.shoppingInventoryDtoList[0].fridgeInventoryId").value(1L))
                .andExpect(jsonPath("$.shoppingInventoryDtoList[0].name").value("Cola"))
                .andExpect(jsonPath("$.shoppingInventoryDtoList[0].amount").value(0.5f))
                .andReturn();
    }
    /* Bad Request 400
    @Test
    void testUpdateShoppingListIsOk() throws Exception {
        ShoppingInventoryDto shoppingInventoryDto = new ShoppingInventoryDto(1L, 1L, "Cola", 0.5f);
        Mockito.when(shoppingListService.updateShoppingList(any(), any())).thenReturn(shoppingInventoryDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/shoppingList/update/item/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fridgeId").value(1L))
                .andExpect(jsonPath("$.name").value("Cola"))
                .andExpect(jsonPath("$.amount").value(0.5f))
                .andReturn();
    }
     */
/* Bad Request 400
    @Test
    void testUpdateShoppingListIsNotFound() throws Exception {

        Mockito.when(shoppingListService.updateShoppingList(any(), any())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.put("/shoppingList/update/item/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

 */

}
