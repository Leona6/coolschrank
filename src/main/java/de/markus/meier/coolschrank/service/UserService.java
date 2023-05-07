package de.markus.meier.coolschrank.service;

import de.markus.meier.coolschrank.model.dto.UserDto;
import de.markus.meier.coolschrank.model.model.UserEntity;
import de.markus.meier.coolschrank.repository.UserRepository;
import de.markus.meier.coolschrank.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing user-related operations.
 */
@Service
public class UserService {

    private final UserRepository userAPIRepository;

    private final UserMapper userMapper;

    /**
     * Constructs a new instance of the UserService class.
     *
     * @param userAPIRepository The UserRepository used for accessing user data.
     * @param userMapper        The UserMapper used for mapping between UserEntity and UserDto.
     */
    @Autowired
    public UserService(UserRepository userAPIRepository, UserMapper userMapper) {
        this.userAPIRepository = userAPIRepository;
        this.userMapper = userMapper;
    }

    /**
     * Creates a new user.
     *
     * @param userDto Data for the new user.
     * @return The ID of the new user.
     */
    public Long createUser(UserDto userDto){
        return userAPIRepository.save(new UserEntity(userDto.getUsername(), userDto.getPassword(), null)).getId();
    }

    /**
     * Retrieves a user based on the specified ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The UserDto object representing the retrieved user, or null if no user is found.
     */
    public UserDto getUser(Long id) {
        UserEntity userEntity=userAPIRepository.findById(id).orElse(null);
        if(userEntity == null){
            return null;
        }
        return userMapper.toUserDto(userAPIRepository.findById(id).get());
    }

    /**
     * Updates the fridge ID of the user.
     *
     * @param id       The ID of the user.
     * @param fridgeId The ID of the fridge.
     * @return The updated UserDto with the new ID.
     */
    public UserDto updateFrigdeId(Long id, String fridgeId) {
        UserEntity userEntity=userAPIRepository.findById(id).orElse(null);
        if(userEntity == null){
            return null;
        }
        userEntity.setFridgeId(fridgeId);
        return userMapper.toUserDto(userAPIRepository.save(userEntity));
    }

}
