package com.omneya.hogwarts.hogwartsartifactsonline.converters.wizardConverter;

import com.omneya.hogwarts.hogwartsartifactsonline.dto.WizardDto;
import com.omneya.hogwarts.hogwartsartifactsonline.models.Wizard;
import org.springframework.core.convert.converter.Converter;

public class WizardDtoToWizardConverter implements Converter<WizardDto, Wizard> {
    @Override
    public Wizard convert(WizardDto source) {
        return null;
    }
}