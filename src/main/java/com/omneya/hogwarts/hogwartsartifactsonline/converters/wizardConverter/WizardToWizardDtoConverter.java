package com.omneya.hogwarts.hogwartsartifactsonline.converters.wizardConverter;

import com.omneya.hogwarts.hogwartsartifactsonline.dto.WizardDto;
import com.omneya.hogwarts.hogwartsartifactsonline.models.Wizard;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizardToWizardDtoConverter implements Converter<Wizard,WizardDto> {
    @Override
    public WizardDto convert(Wizard source) {
        return new WizardDto(
                source.getId(),
                source.getName(),
                source.getNumberOfArtifacts()

        );
    }
}
