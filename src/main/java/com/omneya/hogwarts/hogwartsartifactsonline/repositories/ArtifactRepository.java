package com.omneya.hogwarts.hogwartsartifactsonline.repositories;

import com.omneya.hogwarts.hogwartsartifactsonline.models.Artifact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtifactRepository extends JpaRepository<Artifact, String> {
}
