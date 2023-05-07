package de.markus.meier.coolschrank.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * The FridgeInventoryDto represents a Data Transfer Object (DTO) for a fridge inventory item.
 * It contains the ID of the fridge inventory item, the name of the item, and the current/target value of the item.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FridgeInventoryDto {
    /**
     * The ID of the fridge inventory item.
     */
    private Long id;
    /**
     * The name of the fridge inventory item.
     */
    private String name;
    /**
     * The current value of the item.
     */
    private float actual;
    /**
     * The target value of the item.
     */
    private float target;

    /**
     * Constructs a new FridgeInventoryDto with the given name and actual value.
     *
     * @param name   The name of the fridge position.
     * @param actual The actual value of the position.
     */
    public FridgeInventoryDto(String name, float actual) {
        this.name = name;
        this.actual = actual;
    }

    /**
     * Constructs a new FridgeInventoryDto with the given name, actual value, and target value.
     *
     * @param name   The name of the fridge position.
     * @param actual The actual value of the position.
     * @param target The target value of the position.
     */
    public FridgeInventoryDto(String name, float actual, float target) {
        this.name = name;
        this.actual = actual;
        this.target = target;
    }
}
