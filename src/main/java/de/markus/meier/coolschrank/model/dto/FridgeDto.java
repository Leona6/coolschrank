package de.markus.meier.coolschrank.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * The FridgeDto represents a Data Transfer Object (DTO) for a fridge.
 * It contains the ID of the fridge and a list of FridgeInventoryDto objects
 * that contain the inventory details of the fridge.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FridgeDto {
    /**
     * The ID of the fridge.
     */
    String id;
    /**
     * The list of FridgeInventoryDto objects that contain the inventory details of the fridge.
     */
    List<FridgeInventoryDto> inventory;

}
