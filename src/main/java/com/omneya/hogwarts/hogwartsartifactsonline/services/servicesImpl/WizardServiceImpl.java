package com.omneya.hogwarts.hogwartsartifactsonline.services.servicesImpl;

import com.omneya.hogwarts.hogwartsartifactsonline.models.Wizard;
import com.omneya.hogwarts.hogwartsartifactsonline.repositories.WizardRepository;
import com.omneya.hogwarts.hogwartsartifactsonline.services.WizardService;
import com.omneya.hogwarts.hogwartsartifactsonline.exceptions.WizardNotFoundException;
import com.omneya.hogwarts.hogwartsartifactsonline.utils.IdWorker;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor

public class WizardServiceImpl implements WizardService {
    private final WizardRepository wizardRepository;
    private final IdWorker idWorker;


    @Override
    public Wizard findById(Long id) {
        return this.wizardRepository.findById(id).orElseThrow(() -> new WizardNotFoundException(id));
    }

    @Override
    public List<Wizard> findAll() {
        return wizardRepository.findAll();
    }

    @Override
    public Wizard add(Wizard wizard) {
        wizard.setId(idWorker.nextId());
        return this.wizardRepository.save(wizard);
    }

    @Override
    public Wizard update(Long id ,Wizard update) {

        return this.wizardRepository.findById(id).map(
                (oldWizard)->{
                    oldWizard.setName(update.getName());
                    return this.wizardRepository.save(oldWizard);
                }).orElseThrow(() -> new WizardNotFoundException(id));
    }

    @Override
    public void delete(Long id) {
        Wizard wizard=wizardRepository.findById(id).orElseThrow(() -> new WizardNotFoundException(id));
        wizardRepository.deleteById(id);
    }
}
