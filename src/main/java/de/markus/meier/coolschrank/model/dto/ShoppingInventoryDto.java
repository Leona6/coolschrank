package de.markus.meier.coolschrank.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The ShoppingInventoryDto represents a Data Transfer Object (DTO) for a shopping inventory item.
 * It contains the ID of the shopping inventory item, the ID of the fridge inventory item,
 * the name of the fridge inventory item, and the quantity of the item to be ordered.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingInventoryDto {
    /**
     * The ID of the shopping inventory item.
     */
    private Long id;
    /**
     * The ID of the fridge inventory item.
     */
    private Long fridgeInventoryId;
    /**
     * The name of the fridge inventory item.
     */
    private String name;
    /**
     * The quantity of the item to be ordered.
     */
    private float amount;
}
