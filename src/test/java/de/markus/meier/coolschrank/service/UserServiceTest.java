package de.markus.meier.coolschrank.service;

import de.markus.meier.coolschrank.model.dto.UserDto;
import de.markus.meier.coolschrank.model.model.UserEntity;
import de.markus.meier.coolschrank.repository.UserRepository;
import de.markus.meier.coolschrank.util.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = {UserService.class})
public class UserServiceTest {

    @Autowired
    @InjectMocks
    private UserService userService;

    @MockBean(name = "userRepository")
    private UserRepository userRepository;

    @MockBean(name = "usermapper")
    private UserMapper userMapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_Created() {
        UserEntity userEntity = new UserEntity(1l,"Markus","test123","XXX");
        UserDto userDto = new UserDto(1l,"Markus","test123","XXX");
        Mockito.when(userRepository.save(any())).thenReturn(userEntity);
        Long result=userService.createUser(userDto);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(1L);
    }

    @Test
    void testGetUser_NotFound() {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        assertThat(userService.getUser(1L)).isNull();
    }

    @Test
    void testGetUser_Found() {
        UserEntity userEntity = new UserEntity(1l,"Markus","test123","XXX");
        UserDto userDto = new UserDto(1l,"Markus","test123","XXX");
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userEntity));
        Mockito.when(userMapper.toUserDto(any())).thenReturn(userDto);
        UserDto result=userService.getUser(1L);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userDto.getId());
        assertThat(result.getUsername()).isEqualTo(userDto.getUsername());
        assertThat(result.getPassword()).isEqualTo(userDto.getPassword());
        assertThat(result.getFridgeId()).isEqualTo(userDto.getFridgeId());
    }

    @Test
    void testUpdateFrigdeId_NotFound() {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        assertThat(userService.updateFrigdeId(1L,"XXX")).isNull();
    }

    @Test
    void testUpdateFrigdeId_Found() {
        UserEntity userEntity = new UserEntity(1l,"Markus","test123","XXX");
        UserDto userDto = new UserDto(1l,"Markus","test123","XXX");
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userEntity));
        Mockito.when(userRepository.save(any())).thenReturn(userEntity);
        Mockito.when(userMapper.toUserDto(any())).thenReturn(userDto);
        UserDto result=userService.updateFrigdeId(1L,"XXX");
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userDto.getId());
        assertThat(result.getUsername()).isEqualTo(userDto.getUsername());
        assertThat(result.getPassword()).isEqualTo(userDto.getPassword());
    }
}
