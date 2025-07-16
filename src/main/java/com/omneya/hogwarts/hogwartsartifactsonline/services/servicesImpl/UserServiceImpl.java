package com.omneya.hogwarts.hogwartsartifactsonline.services.servicesImpl;

import com.omneya.hogwarts.hogwartsartifactsonline.exceptions.ObjectNotFoundException;
import com.omneya.hogwarts.hogwartsartifactsonline.models.HogwartsUser;
import com.omneya.hogwarts.hogwartsartifactsonline.repositories.HogwartsUserRepository;
import com.omneya.hogwarts.hogwartsartifactsonline.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final HogwartsUserRepository hogwartsUserRepository;

    public UserServiceImpl(HogwartsUserRepository hogwartsUserRepository) {
        this.hogwartsUserRepository = hogwartsUserRepository;
    }

    @Override
    public HogwartsUser findById(Long id) {
        return this.hogwartsUserRepository
                .findById(id)
                .orElseThrow(()->new ObjectNotFoundException("User",id));
    }

    @Override
    public List<HogwartsUser> findAll() {
        return this.hogwartsUserRepository.findAll();
    }

    @Override
    public HogwartsUser add(HogwartsUser user) {
        return hogwartsUserRepository.save(user);
    }

    @Override
    public HogwartsUser update(Long id,HogwartsUser user) {
        return this.hogwartsUserRepository
                .findById(id).map(
                        (oldUser)->{
                            oldUser.setUsername(user.getUsername());
                            oldUser.setEnabled(user.isEnabled());
                            oldUser.setRoles(user.getRoles());
                            return hogwartsUserRepository.save(oldUser);
                        }).orElseThrow(()->new ObjectNotFoundException("User",id));
    }

    @Override
    public void delete(Long id) {
        HogwartsUser user= this.hogwartsUserRepository
                .findById(id)
                .orElseThrow(()->new ObjectNotFoundException("User",id));
        this.hogwartsUserRepository.deleteById(id);

    }
}
