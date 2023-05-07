package de.markus.meier.coolschrank.model.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The ShoppingInventoryEntity class represents a shopping list item in the database.
 * It contains an automatically generated ID, the ID of the fridge inventory, the name of the fridge position,
 * and the amount of the order.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ShoppingInventoryEntity {
    /**
     * The ID of the shopping list item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The ID of the fridge inventory.
     */
    @Column(nullable = false)
    private Long fridgeInventoryId;
    /**
     * The name of the fridge position.
     */
    @Column(nullable = false)
    private String name;
    /**
     * The amount of the order.
     */
    @Column(nullable = false)
    private float amount;

    /**
     * Constructs a new ShoppingInventoryEntity with the given parameters.
     *
     * @param fridgeInventoryId The ID of the fridge inventory.
     * @param name              The name of the fridge position.
     * @param amount            The amount of the order value.
     */
    public ShoppingInventoryEntity(Long fridgeInventoryId, String name, float amount) {
        this.fridgeInventoryId = fridgeInventoryId;
        this.name = name;
        this.amount = amount;
    }
}
