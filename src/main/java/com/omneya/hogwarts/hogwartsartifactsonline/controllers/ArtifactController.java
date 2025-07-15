package com.omneya.hogwarts.hogwartsartifactsonline.controllers;


import com.omneya.hogwarts.hogwartsartifactsonline.converters.artifactConverter.ArtifactDtoToArtifactConverter;
import com.omneya.hogwarts.hogwartsartifactsonline.converters.artifactConverter.ArtifactToArtifactDtoConverter;
import com.omneya.hogwarts.hogwartsartifactsonline.dto.ArtifactDto;
import com.omneya.hogwarts.hogwartsartifactsonline.models.Artifact;
import com.omneya.hogwarts.hogwartsartifactsonline.services.ArtifactService;
import com.omneya.hogwarts.hogwartsartifactsonline.system.Result;
import com.omneya.hogwarts.hogwartsartifactsonline.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/artifacts")

public class ArtifactController {

    private final ArtifactService artifactService;
    private final ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter;
    private final ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter;

    public ArtifactController(ArtifactService artifactService, ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter, ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter, ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter1) {
        this.artifactService = artifactService;
        this.artifactToArtifactDtoConverter = artifactToArtifactDtoConverter;
        this.artifactDtoToArtifactConverter = artifactDtoToArtifactConverter1;

    }

    @GetMapping("/{id}")
    public Result findArtifactById(@PathVariable String id) {
       Artifact foundArtifact= artifactService.findByid(id);
       ArtifactDto artifactDto = artifactToArtifactDtoConverter.convert(foundArtifact);
        return new Result(true, StatusCode.SUCCESS, "Find One Successfully!", artifactDto);
    }

    @GetMapping
    public Result findAllArtifacts() {
        List<Artifact> foundArtifacts = artifactService.findAll();
        List<ArtifactDto> artifactDtoList=foundArtifacts.stream().map(artifactToArtifactDtoConverter::convert).collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find All Successfully!", artifactDtoList);

    }

    @PostMapping
    public Result addArtifact(@Valid @RequestBody ArtifactDto artifactDto) {
        Artifact artifact = artifactDtoToArtifactConverter.convert(artifactDto);
        Artifact savedArtifact=artifactService.add(artifact);
        ArtifactDto returnArtifactDto=artifactToArtifactDtoConverter.convert(savedArtifact);
        return new Result(true,StatusCode.SUCCESS,"Add Artifact Successfully!",returnArtifactDto);
    }

    @PutMapping("/{id}")
    public Result updateArtifact(@Valid @RequestBody ArtifactDto artifactDto, @PathVariable  String id) {
        Artifact artifact = artifactDtoToArtifactConverter.convert(artifactDto);
        Artifact updatedArtifact =this.artifactService.update(id,artifact);
        ArtifactDto updatedArtifactDto=artifactToArtifactDtoConverter.convert(updatedArtifact);
        return new Result(true,StatusCode.SUCCESS,"Update Artifact Successfully!",updatedArtifactDto);
    }


    @DeleteMapping("/{id}")
    public Result deleteArtifact(@PathVariable String id) {
       this.artifactService.delete(id);
        return new Result(true,StatusCode.SUCCESS,"Delete Artifact Successfully!",null);
    }


}
