package com.omneya.hogwarts.hogwartsartifactsonline.services;
import com.omneya.hogwarts.hogwartsartifactsonline.models.Artifact;

import java.util.List;

public interface ArtifactService {

    public Artifact findByid(String id);
    public List<Artifact> findAll();
    public Artifact add(Artifact artifact);
    public Artifact update(String id, Artifact artifact);
    public void delete(String id);

}
