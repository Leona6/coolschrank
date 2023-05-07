package de.markus.meier.coolschrank.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.markus.meier.coolschrank.model.dto.FridgeDto;
import de.markus.meier.coolschrank.model.dto.FridgeInventoryDto;
import de.markus.meier.coolschrank.service.FridgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * RestController for managing fridge operations.
 */
@RestController
@RequestMapping("/fridge")
public class FridgeController {

    private final FridgeService fridgeAPIService;

    /**
     * Constructs a new FridgeController with the specified FridgeService.
     *
     * @param fridgeAPIService the FridgeService to be used
     */
    @Autowired
    public FridgeController(FridgeService fridgeAPIService) {
        this.fridgeAPIService = fridgeAPIService;
    }

    /**
     * Creates a new fridge and returns the created fridge ID.
     *
     * @return the ResponseEntity containing the created fridge ID
     * @throws JsonProcessingException if an error occurs during JSON processing
     */
    @PostMapping("/create")
    public ResponseEntity<String> createFridge() throws JsonProcessingException {
        return new ResponseEntity<>(fridgeAPIService.createFridge(), HttpStatus.CREATED);
    }

    /**
     * Retrieves the fridge with the specified ID.
     *
     * @param id the ID of the fridge to retrieve
     * @return the ResponseEntity containing the retrieved fridge DTO
     * @throws JsonProcessingException if an error occurs during JSON processing
     */
    @GetMapping("/{id}")
    public ResponseEntity<FridgeDto> getFridge(@PathVariable String id) throws JsonProcessingException {
        FridgeDto fridgeAPIDto = fridgeAPIService.getFridge(id);
        if (fridgeAPIDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(fridgeAPIDto, HttpStatus.OK);
    }

    /**
     * Adds a new inventory item to the fridge with the specified ID.
     *
     * @param fridgeInvetoryAPIDto the DTO representing the inventory item to be added
     * @param fridgeId the ID of the fridge to add the inventory item to
     * @return the ResponseEntity containing the added inventory item DTO
     * @throws JsonProcessingException if an error occurs during JSON processing
     */
    @PostMapping("/addInventory/{fridgeId}")
    public ResponseEntity<FridgeInventoryDto> addFridgeInventory(@RequestBody FridgeInventoryDto fridgeInvetoryAPIDto, @PathVariable String fridgeId) throws JsonProcessingException {
        fridgeInvetoryAPIDto = fridgeAPIService.addFridgeInventory(fridgeInvetoryAPIDto, fridgeId);
        if (fridgeInvetoryAPIDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(fridgeInvetoryAPIDto, HttpStatus.CREATED);
    }

    /**
     * Updates the inventory item in the fridge with the specified ID.
     *
     * @param fridgeInvetoryAPIDto the DTO representing the updated inventory item
     * @param fridgeId the ID of the fridge to update the inventory item in
     * @return the ResponseEntity containing the updated inventory item DTO
     * @throws JsonProcessingException if an error occurs during JSON processing
     */
    @PutMapping("/updateInventory/{fridgeId}")
    public ResponseEntity<FridgeInventoryDto> upDateFridgeInventory(@RequestBody FridgeInventoryDto fridgeInvetoryAPIDto, @PathVariable String fridgeId) throws JsonProcessingException {
        try {
            fridgeInvetoryAPIDto = fridgeAPIService.upDateFridgeInventory(fridgeInvetoryAPIDto, fridgeId);
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 404) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else if (e.getStatusCode().value() == 400) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>(fridgeInvetoryAPIDto, HttpStatus.OK);
    }

}
