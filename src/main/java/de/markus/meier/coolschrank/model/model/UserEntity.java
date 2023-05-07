package de.markus.meier.coolschrank.model.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The UserEntity class represents a user in the database.
 * It contains an automatically generated ID, the username, password, and fridge ID of the user.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserEntity {
    /**
     * The ID of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The username of the user.
     */
    @Column(nullable = false)
    private String username;
    /**
     * The password of the user.
     */
    @Column(nullable = false)
    private String password;
    /**
     * The ID of the fridge.
     */
    private String fridgeId;

    /**
     * Constructor for creating a user.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @param fridgeId The ID of the fridge.
     */
    public UserEntity(String username, String password, String fridgeId) {
        this.username = username;
        this.password = password;
        this.fridgeId = fridgeId;
    }
}
