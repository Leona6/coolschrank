package de.markus.meier.coolschrank.model.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * The ShoppingListEntity class represents a shopping list in the database.
 * It contains an automatically generated ID, the ID of the fridge, and a list of shopping inventory items
 * that represent the fridge positions and their order values.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ShoppingListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The ID of the fridge.
     */
    @Column(nullable = false)
    private String fridgeId;
    /**
     * The list of shopping inventory items representing the fridge positions and their order values.
     */
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn
    private List<ShoppingInventoryEntity> shoppingInventoryEntityList;
}
