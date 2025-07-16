package com.omneya.hogwarts.hogwartsartifactsonline.services.servicesImpl;

import com.omneya.hogwarts.hogwartsartifactsonline.exceptions.ObjectNotFoundException;
import com.omneya.hogwarts.hogwartsartifactsonline.models.HogwartsUser;
import com.omneya.hogwarts.hogwartsartifactsonline.repositories.HogwartsUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private HogwartsUserRepository hogwartsUserRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private List<HogwartsUser> hogwartsUsers;

    @BeforeEach
    void setUp() {
       hogwartsUsers = new ArrayList<>();

        HogwartsUser u1 = new HogwartsUser();
        u1.setId(1L);
        u1.setUsername("john");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRoles("admin user");


        HogwartsUser u2 = new HogwartsUser();
        u2.setId(2L);
        u2.setUsername("eric");
        u2.setPassword("654321");
        u2.setEnabled(true);
        u2.setRoles("user");

        HogwartsUser u3 = new HogwartsUser();
        u3.setId(3L);
        u3.setUsername("tom");
        u3.setPassword("qwerty");
        u3.setEnabled(false);
        u3.setRoles("user");

        hogwartsUsers.add(u1);
        hogwartsUsers.add(u2);
        hogwartsUsers.add(u3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
        //given
        HogwartsUser u1 = new HogwartsUser();
        u1.setId(1L);
        u1.setUsername("john");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRoles("admin user");
        given(this.hogwartsUserRepository.findById(1L)).willReturn(Optional.of(u1));

        //when
        HogwartsUser foundUser = userServiceImpl.findById(1L);

        //then
        assertThat(foundUser.getId()).isEqualTo(u1.getId());
        assertThat(foundUser.getUsername()).isEqualTo(u1.getUsername());
        verify(this.hogwartsUserRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        //given
        given(this.hogwartsUserRepository.findById(1L)).willReturn(Optional.empty());

        //when
        Throwable thrown = catchThrowable(() -> userServiceImpl.findById(1L));

        //then
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could Not Find User with Id 1 :(!");

    }

    @Test
    void testFindAllSuccess() {
        given(hogwartsUserRepository.findAll()).willReturn(this.hogwartsUsers);
        List<HogwartsUser> foundUsers = userServiceImpl.findAll();
        assertThat(foundUsers.size()).isEqualTo(hogwartsUsers.size());
        verify(hogwartsUserRepository, times(1)).findAll();
    }

    @Test
    void testAddSuccess() {
        //given
        HogwartsUser u1 = new HogwartsUser();
        u1.setId(1L);
        u1.setUsername("john");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRoles("admin user");
        given(this.hogwartsUserRepository.save(u1)).willReturn(u1);

        //when
        HogwartsUser savedUser = userServiceImpl.add(u1);

        //then
        assertThat(savedUser.getId()).isEqualTo(u1.getId());
        assertThat(savedUser.getUsername()).isEqualTo(u1.getUsername());
        assertThat(savedUser.getPassword()).isEqualTo(u1.getPassword());
        verify(this.hogwartsUserRepository, times(1)).save(u1);

    }

    @Test
    void testUpdateSuccess() {
        //given
        HogwartsUser oldUser = new HogwartsUser();
        oldUser.setId(1L);
        oldUser.setUsername("john");
        oldUser.setPassword("123456");
        oldUser.setEnabled(false);
        oldUser.setRoles("admin user");

        HogwartsUser updatedUser = new HogwartsUser();
        updatedUser.setId(1L);
        updatedUser.setUsername("john-update");
        updatedUser.setPassword("123456-update");
        updatedUser.setEnabled(true);
        updatedUser.setRoles("admin user");

        given(this.hogwartsUserRepository.findById(1L)).willReturn(Optional.of(oldUser));
        given(this.hogwartsUserRepository.save(oldUser)).willReturn(oldUser);

        //when
        HogwartsUser updatedUser0=userServiceImpl.update(1L, oldUser);

        //then
        assertThat(updatedUser0.getUsername()).isEqualTo(oldUser.getUsername());
        assertThat(updatedUser0.isEnabled()).isEqualTo(oldUser.isEnabled());
       verify(this.hogwartsUserRepository, times(1)).findById(1L);
       verify(this.hogwartsUserRepository, times(1)).save(oldUser);


    }

    @Test
    void testUpdateFailure() {
        //given
        given(this.hogwartsUserRepository.findById(1L)).willReturn(Optional.empty());
        //when
        Throwable thrown = catchThrowable(() -> userServiceImpl.update(1L, new HogwartsUser()));
        //then
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could Not Find User with Id 1 :(!");
        verify(this.hogwartsUserRepository, times(1)).findById(1L);

    }

    @Test
    void testDeleteSuccess() {
        //given
        HogwartsUser user = new HogwartsUser();
        user.setId(1L);
        user.setUsername("john");
        user.setPassword("123456");
        user.setEnabled(true);
        user.setRoles("admin user");
        given(this.hogwartsUserRepository.findById(1l)).willReturn(Optional.of(user));
        doNothing().when(this.hogwartsUserRepository).deleteById(1L);

        //when
        this.userServiceImpl.delete(1L);

        //then
        verify(this.hogwartsUserRepository, times(1)).findById(1L);
        verify(this.hogwartsUserRepository, times(1)).deleteById(1L);

    }


    @Test
    void testDeleteFailure() {
        //given
        given(hogwartsUserRepository.findById(1L)).willReturn(Optional.empty());
        //when
        Throwable thrown = catchThrowable(() -> userServiceImpl.delete(1L));
        //then
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could Not Find User with Id 1 :(!");
        verify(this.hogwartsUserRepository, times(1)).findById(1L);
    }
}