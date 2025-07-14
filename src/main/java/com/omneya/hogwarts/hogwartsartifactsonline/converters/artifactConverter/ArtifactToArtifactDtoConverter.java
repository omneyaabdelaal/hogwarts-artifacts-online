package com.omneya.hogwarts.hogwartsartifactsonline.converters.artifactConverter;

import com.omneya.hogwarts.hogwartsartifactsonline.converters.wizardConverter.WizardToWizardDtoConverter;
import com.omneya.hogwarts.hogwartsartifactsonline.dto.ArtifactDto;
import com.omneya.hogwarts.hogwartsartifactsonline.models.Artifact;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactToArtifactDtoConverter implements Converter<Artifact,ArtifactDto > {
    private final WizardToWizardDtoConverter wizardConverter;

    public ArtifactToArtifactDtoConverter(WizardToWizardDtoConverter wizardConverter) {
        this.wizardConverter = wizardConverter;
    }

    @Override
    public ArtifactDto convert(Artifact source) {
        return new ArtifactDto(
                source.getId(),
                source.getName(),
                source.getDescription(),
                source.getImageURL(),
                source.getOwner()!=null? this.wizardConverter.convert(source.getOwner()): null
                );

    }
}
