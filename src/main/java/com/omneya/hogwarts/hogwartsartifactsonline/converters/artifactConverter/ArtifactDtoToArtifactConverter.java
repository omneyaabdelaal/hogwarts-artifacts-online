package com.omneya.hogwarts.hogwartsartifactsonline.converters.artifactConverter;

import com.omneya.hogwarts.hogwartsartifactsonline.dto.ArtifactDto;
import com.omneya.hogwarts.hogwartsartifactsonline.models.Artifact;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactDtoToArtifactConverter implements Converter<ArtifactDto, Artifact> {
    @Override
    public Artifact convert(ArtifactDto source) {
        return new Artifact(
                source.id(),
                source.name(),
                source.description(),
                source.imageUrl(),
                null

        );
    }
}
