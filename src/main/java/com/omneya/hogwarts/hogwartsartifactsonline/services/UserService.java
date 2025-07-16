package com.omneya.hogwarts.hogwartsartifactsonline.services;


import com.omneya.hogwarts.hogwartsartifactsonline.models.HogwartsUser;

import java.util.List;

public interface UserService {
    HogwartsUser findById(Long id);
    List<HogwartsUser> findAll();
    HogwartsUser add(HogwartsUser user);
    HogwartsUser update(Long id,HogwartsUser user);
    void delete(Long id);

}
