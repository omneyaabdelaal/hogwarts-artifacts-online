package com.omneya.hogwarts.hogwartsartifactsonline.controllers;


import com.omneya.hogwarts.hogwartsartifactsonline.converters.UserConverter.UserDtoToUserConverter;
import com.omneya.hogwarts.hogwartsartifactsonline.converters.UserConverter.UserToUserDtoConverter;
import com.omneya.hogwarts.hogwartsartifactsonline.dto.UserDto;
import com.omneya.hogwarts.hogwartsartifactsonline.models.HogwartsUser;
import com.omneya.hogwarts.hogwartsartifactsonline.services.UserService;
import com.omneya.hogwarts.hogwartsartifactsonline.services.servicesImpl.UserServiceImpl;
import com.omneya.hogwarts.hogwartsartifactsonline.system.Result;
import com.omneya.hogwarts.hogwartsartifactsonline.system.StatusCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class UserController {

    private final UserServiceImpl userService;
    private final UserToUserDtoConverter userToUserDtoConverter;
    private final UserDtoToUserConverter userDtoToUserConverter;

    public UserController(UserServiceImpl userService, UserToUserDtoConverter userToUserDtoConverter, UserDtoToUserConverter userDtoToUserConverter) {
        this.userService = userService;
        this.userToUserDtoConverter = userToUserDtoConverter;
        this.userDtoToUserConverter = userDtoToUserConverter;
    }

    @GetMapping
    public Result findAllUsers() {
        List<HogwartsUser> users = userService.findAll();
        List<UserDto> userDtos= users.stream().map(userToUserDtoConverter::convert).toList();
        return new Result(true, StatusCode.SUCCESS,"Find All Users Success!",userDtos);


    }

    @GetMapping("/{id}")
    public Result findUserById(@PathVariable Long id) {
        HogwartsUser foundUser = userService.findById(id);
        UserDto foundUserDto = userToUserDtoConverter.convert(foundUser);
        return new Result(true, StatusCode.SUCCESS,"Find User Success!",foundUserDto);
    }

    @PostMapping
    public Result addUser(@RequestBody HogwartsUser user) {
        HogwartsUser savedUser=userService.add(user);
        UserDto savedUserDTO=userToUserDtoConverter.convert(savedUser);
        return new Result(true, StatusCode.SUCCESS,"Add User Success!",savedUserDTO);
    }

    @PutMapping("/{id}")
    public Result updateUser(@PathVariable Long id, @RequestBody UserDto user) {
        HogwartsUser toBeUpdated=userDtoToUserConverter.convert(user);
        HogwartsUser UpdatedUser=userService.update(id, toBeUpdated);
        UserDto savedUserDTO=userToUserDtoConverter.convert(UpdatedUser);
        return new Result(true, StatusCode.SUCCESS,"Update User Success!",savedUserDTO);
    }

    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return new Result(true, StatusCode.SUCCESS,"Delete User Success!",null);
    }

}
