package de.markus.meier.coolschrank.util;

import de.markus.meier.coolschrank.model.dto.UserDto;
import de.markus.meier.coolschrank.model.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = {UserMapper.class})
public class UserMapperTest {


    @Autowired
    @InjectMocks
    private UserMapper userMapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToUserDto(){
        UserEntity userEntity= new UserEntity(1L,"Markus","test123","XXX");
        UserDto result= userMapper.toUserDto(userEntity);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userEntity.getId());
        assertThat(result.getUsername()).isEqualTo(userEntity.getUsername());
        assertThat(result.getPassword()).isEqualTo(userEntity.getPassword());
        assertThat(result.getFridgeId()).isEqualTo(userEntity.getFridgeId());
    }
}
