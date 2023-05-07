package de.markus.meier.coolschrank.controller;

import de.markus.meier.coolschrank.model.dto.UserDto;
import de.markus.meier.coolschrank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling user-related requests.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    /**
     * Constructs a new UserController with the given UserService.
     *
     * @param userService the UserService to be used
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Creates a new user.
     *
     * @param userDto the UserDto object containing the user data
     * @return the ResponseEntity containing the ID of the created user
     */
    @PostMapping("/")
    public ResponseEntity<Long> createUser(@RequestBody UserDto userDto) {
        try {
            return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id the ID of the user to retrieve
     * @return the ResponseEntity containing the retrieved UserDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        UserDto userDto = userService.getUser(id);
        if (userDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * Updates the fridge ID of a user.
     *
     * @param id the ID of the user to update
     * @param fridgeId the new fridge ID
     * @return the ResponseEntity containing the updated UserDto
     */
    @PutMapping("/{id}/frigde/{fridgeId}")
    public ResponseEntity<UserDto> updateFrigdeId(@PathVariable Long id, @PathVariable String fridgeId) {
        UserDto userDto = userService.updateFrigdeId(id, fridgeId);
        if (userDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
