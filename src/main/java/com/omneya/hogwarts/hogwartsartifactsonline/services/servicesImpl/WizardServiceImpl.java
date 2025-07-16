package com.omneya.hogwarts.hogwartsartifactsonline.services.servicesImpl;

import com.omneya.hogwarts.hogwartsartifactsonline.exceptions.ObjectNotFoundException;
import com.omneya.hogwarts.hogwartsartifactsonline.models.Artifact;
import com.omneya.hogwarts.hogwartsartifactsonline.models.Wizard;
import com.omneya.hogwarts.hogwartsartifactsonline.repositories.ArtifactRepository;
import com.omneya.hogwarts.hogwartsartifactsonline.repositories.WizardRepository;
import com.omneya.hogwarts.hogwartsartifactsonline.services.WizardService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor

public class WizardServiceImpl implements WizardService {
    private final WizardRepository wizardRepository;
    private final ArtifactRepository artifactRepository;



    @Override
    public Wizard findById(Long id) {
        return this.wizardRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id));
    }

    @Override
    public List<Wizard> findAll() {
        return wizardRepository.findAll();
    }

    @Override
    public Wizard add(Wizard wizard) {
        return this.wizardRepository.save(wizard);
    }

    @Override
    public Wizard update(Long id ,Wizard update) {

        return this.wizardRepository.findById(id).map(
                (oldWizard)->{
                    oldWizard.setName(update.getName());
                    return this.wizardRepository.save(oldWizard);
                }).orElseThrow(() -> new ObjectNotFoundException(id));
    }

    @Override
    public void delete(Long id) {
        Wizard wizard=wizardRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id));
        wizard.removeAllArtifacts();
        wizardRepository.deleteById(id);
    }

    @Override
    public void assignArtifact(Long wizardId, String artifactId) {
        Artifact artifact= this.artifactRepository.findById(artifactId).orElseThrow(()->  new ObjectNotFoundException(artifactId));
        Wizard wizard= this.wizardRepository.findById(wizardId).orElseThrow(() -> new ObjectNotFoundException(wizardId));


        if(artifact.getOwner() !=null){
            artifact.getOwner().removeArtifact(artifact);
        }
        wizard.addArtifact(artifact);

    }
}
