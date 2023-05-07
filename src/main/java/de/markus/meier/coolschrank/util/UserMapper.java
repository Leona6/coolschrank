package de.markus.meier.coolschrank.util;

import de.markus.meier.coolschrank.model.dto.UserDto;
import de.markus.meier.coolschrank.model.model.UserEntity;
import org.springframework.stereotype.Service;

/**
 * Utility class for mapping between UserEntity and UserDto.
 */
@Service
public class UserMapper {
    /**
     * Converts a UserEntity object to a UserDto object.
     *
     * @param userEntity The UserEntity object to be converted.
     * @return The converted UserDto object.
     */
    public UserDto toUserDto(UserEntity userEntity) {
        return new UserDto(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getFridgeId()
        );
    }
}
