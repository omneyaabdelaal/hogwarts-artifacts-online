package com.omneya.hogwarts.hogwartsartifactsonline.controllers;

import com.omneya.hogwarts.hogwartsartifactsonline.converters.wizardConverter.WizardDtoToWizardConverter;
import com.omneya.hogwarts.hogwartsartifactsonline.converters.wizardConverter.WizardToWizardDtoConverter;
import com.omneya.hogwarts.hogwartsartifactsonline.dto.WizardDto;
import com.omneya.hogwarts.hogwartsartifactsonline.models.Wizard;
import com.omneya.hogwarts.hogwartsartifactsonline.services.servicesImpl.WizardServiceImpl;
import com.omneya.hogwarts.hogwartsartifactsonline.system.Result;
import com.omneya.hogwarts.hogwartsartifactsonline.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/wizards")
public class WizardController {
   private final WizardServiceImpl wizardServiceImpl;
   private final WizardToWizardDtoConverter wizardToWizardDtoConverter;
   private final WizardDtoToWizardConverter  wizardDtoToWizardConverter;

    public WizardController(WizardServiceImpl wizardServiceImpl, WizardToWizardDtoConverter wizardToWizardDtoConverter, WizardDtoToWizardConverter wizardDtoToWizardConverter) {
        this.wizardServiceImpl = wizardServiceImpl;
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
        this.wizardDtoToWizardConverter = wizardDtoToWizardConverter;
    }

    @GetMapping("/{id}")
    public Result findWizardById(@PathVariable Long id) {
        Wizard wizard =wizardServiceImpl.findById(id);
        WizardDto wizardDto =wizardToWizardDtoConverter.convert(wizard);
        return new Result(true, StatusCode.SUCCESS, "Wizard found with id: " + id, wizardDto);
    }

    @GetMapping
    public Result findAllWizards() {
        List<Wizard> wizards =wizardServiceImpl.findAll();
        List<WizardDto> wizardsDto= wizards.stream().map(wizardToWizardDtoConverter::convert).toList();
        return new Result(true, StatusCode.SUCCESS, "All wizards found.", wizardsDto);

    }

    @PostMapping
    public Result addWizard(@Valid @RequestBody WizardDto wizardDto) {

        Wizard wizard=wizardDtoToWizardConverter.convert(wizardDto);
        assert wizard != null;
        Wizard savedWizard=wizardServiceImpl.add(wizard);
        WizardDto savedWizardDto=wizardToWizardDtoConverter.convert(savedWizard);
        return new Result(true, StatusCode.SUCCESS, "Wizard has been successfully added!", savedWizardDto);
    }

    @PutMapping("/{id}")
    public Result updateWizard(@PathVariable Long id, @Valid @RequestBody WizardDto wizardDto) {
        Wizard wizard=wizardDtoToWizardConverter.convert(wizardDto);
        assert wizard != null;
        Wizard updatedWizard=wizardServiceImpl.update(id, wizard);
        WizardDto updatedWizardDto=wizardToWizardDtoConverter.convert(updatedWizard);
        return new Result(true, StatusCode.SUCCESS, "Wizard has been successfully updated!", updatedWizardDto);
    }

    @DeleteMapping("/{id}")
    public Result deleteWizard(@PathVariable Long id) {
        wizardServiceImpl.delete(id);
        return new Result(true, StatusCode.SUCCESS, "Wizard has been deleted.", null);
    }

}
