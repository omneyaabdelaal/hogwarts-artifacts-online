package com.omneya.hogwarts.hogwartsartifactsonline.services.servicesImpl;

import com.omneya.hogwarts.hogwartsartifactsonline.exceptions.ArtifactNotFoundException;
import com.omneya.hogwarts.hogwartsartifactsonline.models.Artifact;
import com.omneya.hogwarts.hogwartsartifactsonline.repositories.ArtifactRepository;
import com.omneya.hogwarts.hogwartsartifactsonline.services.ArtifactService;
import com.omneya.hogwarts.hogwartsartifactsonline.utils.IdWorker;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Transactional
@AllArgsConstructor
public class ArtifactServiceImpl implements ArtifactService {

    private final ArtifactRepository artifactRepository;
    private final IdWorker idWorker;
    private final DataSourcePoolMetadataProvider hikariPoolDataSourceMetadataProvider;

    @Override
    public Artifact findByid(String id) {

      return this.artifactRepository
                 .findById(id)
                 .orElseThrow(()->new ArtifactNotFoundException(id));
    }

    @Override
    public List<Artifact> findAll() {
        return this.artifactRepository.findAll();
    }

    @Override
    public Artifact add(Artifact artifact) {
        artifact.setId(idWorker.nextId() + "");
        return this.artifactRepository.save(artifact);
    }

    @Override
    public Artifact update(String id, Artifact update) {
        return this.artifactRepository.findById(id)
                .map(oldArtifact->{
                    oldArtifact.setName(update.getName());
                    oldArtifact.setDescription(update.getDescription());
                    oldArtifact.setImageURL(update.getImageURL());

                    return this.artifactRepository.save(oldArtifact);
                }).orElseThrow(()->new ArtifactNotFoundException(id));

    }

    @Override
    public void delete(String id) {
       Artifact artifact=this.artifactRepository.findById(id).orElseThrow(()->new ArtifactNotFoundException(id));
        this.artifactRepository.deleteById(id);
    }


}
