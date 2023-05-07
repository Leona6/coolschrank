package de.markus.meier.coolschrank.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * The ShoppingListDto represents a Data Transfer Object (DTO) for a shopping list.
 * It contains the ID of the shopping list, the ID of the fridge, and a list of ShoppingInventoryDto objects
 * that represent the fridge inventory items and their order quantities.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingListDto {
    /**
     * The ID of the shopping list.
     */
    private Long id;
    /**
     * The ID of the fridge.
     */
    private String fridgeId;
    /**
     * The list of ShoppingInventoryDto objects representing the fridge inventory items and their order quantities.
     */
    private List<ShoppingInventoryDto>shoppingInventoryDtoList;

}
