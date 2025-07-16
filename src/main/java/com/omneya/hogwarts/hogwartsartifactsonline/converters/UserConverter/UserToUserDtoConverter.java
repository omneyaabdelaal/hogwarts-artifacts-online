package com.omneya.hogwarts.hogwartsartifactsonline.converters.UserConverter;

import com.omneya.hogwarts.hogwartsartifactsonline.dto.UserDto;
import com.omneya.hogwarts.hogwartsartifactsonline.models.HogwartsUser;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<HogwartsUser, UserDto> {
    @Override
    public UserDto convert(HogwartsUser source) {
        return new UserDto(
                source.getId(),
                source.getUsername(),
                source.isEnabled(),
                source.getRoles()
        );
    }
}
