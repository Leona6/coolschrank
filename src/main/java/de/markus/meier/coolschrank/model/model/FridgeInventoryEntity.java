package de.markus.meier.coolschrank.model.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The FridgeInventoryEntity class represents a fridge position in the database.
 * It contains an automatically generated ID, as well as the name and target value of the fridge position.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FridgeInventoryEntity {
    /**
     * The name of the fridge position.
     */
    private String name;
    /**
     * The target value of the fridge position.
     */
    private float target;
}
