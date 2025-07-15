package com.omneya.hogwarts.hogwartsartifactsonline.services;

import com.omneya.hogwarts.hogwartsartifactsonline.dto.WizardDto;
import com.omneya.hogwarts.hogwartsartifactsonline.models.Wizard;

import java.util.List;

public interface WizardService {

    Wizard findById(Long id);
    List<Wizard> findAll();
    Wizard add(Wizard wizard);
    Wizard update(Long id, Wizard wizard);
    void delete(Long id);
}
