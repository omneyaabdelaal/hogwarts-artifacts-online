package com.omneya.hogwarts.hogwartsartifactsonline.converters.UserConverter;

import com.omneya.hogwarts.hogwartsartifactsonline.dto.UserDto;
import com.omneya.hogwarts.hogwartsartifactsonline.models.HogwartsUser;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, HogwartsUser> {
    @Override
    public HogwartsUser convert(UserDto source) {
        HogwartsUser user = new HogwartsUser();
        user.setId(source.id());
        user.setUsername(source.username());
        user.setEnabled(source.enabled());
        user.setRoles(source.roles());
        return user;
    }
}
