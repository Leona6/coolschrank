package de.markus.meier.coolschrank.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The UserDto class represents a Data Transfer Object (DTO) for a user.
 * It contains the user's ID, username, password, and fridge ID.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    /**
     * The ID of the user.
     */
    private Long id;
    /**
     * The username of the user.
     */
    private String username;
    /**
     * The password of the user.
     */
    private String password;
    /**
     * The ID of the fridge associated with the user.
     */
    private String fridgeId;
}
