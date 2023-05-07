package de.markus.meier.coolschrank.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.markus.meier.coolschrank.model.dto.ShoppingInventoryDto;
import de.markus.meier.coolschrank.model.dto.ShoppingListDto;
import de.markus.meier.coolschrank.model.model.ShoppingInventoryEntity;
import de.markus.meier.coolschrank.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * RestController for managing shopping list operations.
 */
@RestController
@RequestMapping("/shoppingList")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    /**
     * Constructs a new ShoppingListController with the specified ShoppingListService.
     *
     * @param shoppingListAPIService the ShoppingListService to be used
     */
    @Autowired
    public ShoppingListController(ShoppingListService shoppingListAPIService) {
        this.shoppingListService = shoppingListAPIService;
    }

    /**
     * Retrieves the shopping list with the specified ID.
     *
     * @param id the ID of the shopping list to retrieve
     * @return the ResponseEntity containing the retrieved shopping list DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShoppingListDto> getShoppingList(@PathVariable Long id) {
        ShoppingListDto result = shoppingListService.getShoppingList(id);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Creates a new shopping list for the specified fridge ID.
     *
     * @param id the ID of the fridge to create the shopping list for
     * @return the ResponseEntity containing the created shopping list DTO
     */
    @PostMapping("/create/fridge/{id}")
    public ResponseEntity<ShoppingListDto> createShoppingList(@PathVariable("id") String id) {
        try {
            ShoppingListDto shoppingListAPIDto = shoppingListService.createShoppingList(id);
            if (shoppingListAPIDto == null) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(shoppingListAPIDto, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * Automatically updates the shopping list with the specified ID based on the fridge with the specified ID.
     *
     * @param id the ID of the shopping list to update
     * @param fridgeId the ID of the fridge to update the shopping list from
     * @return the ResponseEntity containing the updated shopping list DTO
     * @throws JsonProcessingException if an error occurs during JSON processing
     */
    @PutMapping("/update/{id}/fridge/{fridgeId}")
    public ResponseEntity<ShoppingListDto> autoUpdateShoppingList(@PathVariable Long id, @PathVariable String fridgeId) throws JsonProcessingException {
        ShoppingListDto result = shoppingListService.autoUpdateShoppingList(id, fridgeId);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    /**
     * Deletes the shopping inventory item with the specified ID from the shopping list with the specified ID.
     *
     * @param id the ID of the shopping list to delete the inventory item from
     * @param itemId the ID of the inventory item to delete
     * @return the ResponseEntity containing the deleted shopping inventory DTO
     */
    @DeleteMapping("/delete/{id}/item/{itemId}")
    public ResponseEntity<ShoppingInventoryDto> deleteShoppingInventory(@PathVariable Long id, @PathVariable Long itemId) {
        ShoppingInventoryDto shoppingInventoryDto = shoppingListService.deleteShoppingInventory(id, itemId);
        if (shoppingInventoryDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(shoppingInventoryDto, HttpStatus.OK);
    }

    /**
     * Deletes the shopping list with the specified ID.
     *
     * @param id the ID of the shopping list to delete
     * @return the ResponseEntity containing the deleted shopping list DTO
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ShoppingListDto> deleteShoppingList(@PathVariable Long id) {
        ShoppingListDto shoppingListDto = shoppingListService.deleteShoppingList(id);
        if (shoppingListDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(shoppingListDto, HttpStatus.OK);
    }

    /**
     * Updates the shopping list with the specified ID using the provided shopping inventory entity.
     *
     * @param id the ID of the shopping list to update
     * @param shoppingInventoryAPIEntity the shopping inventory entity containing the updated data
     * @return the ResponseEntity containing the updated shopping inventory DTO
     */
    @PutMapping("/update/item/{id}")
    public ResponseEntity<ShoppingInventoryDto> updateShoppingList(@PathVariable Long id, @RequestBody ShoppingInventoryEntity shoppingInventoryAPIEntity) {
        ShoppingInventoryDto result = shoppingListService.updateShoppingList(id, shoppingInventoryAPIEntity);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
