package com.omneya.hogwarts.hogwartsartifactsonline.repositories;

import com.omneya.hogwarts.hogwartsartifactsonline.models.HogwartsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HogwartsUserRepository extends JpaRepository<HogwartsUser,Long> {
}
