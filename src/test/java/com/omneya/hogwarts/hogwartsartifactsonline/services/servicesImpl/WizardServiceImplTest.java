package com.omneya.hogwarts.hogwartsartifactsonline.services.servicesImpl;

import com.omneya.hogwarts.hogwartsartifactsonline.exceptions.ObjectNotFoundException;
import com.omneya.hogwarts.hogwartsartifactsonline.models.Artifact;
import com.omneya.hogwarts.hogwartsartifactsonline.models.Wizard;
import com.omneya.hogwarts.hogwartsartifactsonline.repositories.WizardRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class WizardServiceImplTest {

    @Mock
    private WizardRepository wizardRepository;
    @InjectMocks
    private WizardServiceImpl wizardServiceImpl;

    List<Wizard> wizards;

    @BeforeEach
    void setUp() {

        wizards = new ArrayList<>();
        Wizard wizard4 = new Wizard();
        wizard4.setId(4L);
        wizard4.setName("Wizard 4");

        Wizard wizard5 = new Wizard();
        wizard5.setId(5L);
        wizard5.setName("Wizard 5");

        Wizard wizard6 = new Wizard();
        wizard6.setId(6L);
        wizard6.setName("Wizard 6");

        Artifact artifact1 = new Artifact();

        artifact1.setId("1");
        artifact1.setName("Artifact 1");
        artifact1.setDescription("Artifact 1 description");
        artifact1.setImageURL("imageURL");

        Artifact artifact2 = new Artifact();
        artifact2.setId("2");
        artifact2.setName("Artifact 2");
        artifact2.setDescription("Artifact 2 description");
        artifact2.setImageURL("imageURL");

        wizard4.addArtifact(artifact1);
        wizard4.addArtifact(artifact2);

        wizard5.addArtifact(artifact1);
        wizard6.addArtifact(artifact2);

        wizards.add(wizard4);
        wizards.add(wizard5);
        wizards.add(wizard6);
    }
    @AfterEach
    void tearDown() {}

    @Test
    void testFindByIdSuccess() {
        //given
        List<Artifact> artifacts = new ArrayList<>();
        Artifact artifact = new Artifact();
        artifact.setId("1250808601744904192");
        artifact.setName("Invisibility Cloak");
        artifact.setDescription("An invisibility cloak is used to make the wearer invisible.");
        artifact.setImageURL("ImageUrl");
        artifacts.add(artifact);

        Wizard wizard = new Wizard();
        wizard.setId(1L);
        wizard.setName("Wizard 1");
        wizard.setArtifacts(artifacts);

        given(wizardRepository.findById(1L)).willReturn(Optional.of(wizard));

        // when
        Wizard requesteddWizard= wizardServiceImpl.findById(1L);

        //then

        assertThat(requesteddWizard.getId()).isEqualTo(wizard.getId());
        assertThat(requesteddWizard.getName()).isEqualTo(wizard.getName());
        assertThat(requesteddWizard.getArtifacts().size()).isEqualTo(artifacts.size());
        assertThat(requesteddWizard.getArtifacts().contains(artifact)).isTrue();

        verify(wizardRepository,times(1)).findById(1L);
    }

    @Test
    void testFindByidNotFound() {

        given(wizardRepository.findById(Mockito.any(Long.class))).willReturn(Optional.empty());

        //when
        Throwable throwable= catchThrowable(()-> {
                    Wizard wizard = wizardServiceImpl.findById(1L);
                });

        //then
        assertThat(throwable)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could Not Find Wizard with Id 1 :(!");

        verify(wizardRepository,times(1)).findById(1L);
    }

    @Test
    void testFindAllSuccess() {
        //given
        given(wizardRepository.findAll()).willReturn(this.wizards);

        //when
        List<Wizard> foundWizards = wizardServiceImpl.findAll();

        //Then
        assertThat(foundWizards.size()).isEqualTo(wizards.size());
        verify(wizardRepository,times(1)).findAll();

    }

    @Test
    void testAddSuccess() {
        //given
        Wizard wizard = new Wizard();
        wizard.setName("Wizard 123");


        given(wizardRepository.save(wizard)).willReturn(wizard);

        //when
        Wizard newWizard = wizardServiceImpl.add(wizard);
        assertThat(newWizard.getName()).isEqualTo(wizard.getName());
        verify(wizardRepository,times(1)).save(wizard);
    }

    @Test
    void testUpdateSuccess() {
        Wizard oldWizard = new Wizard();
        oldWizard.setId(123L);
        oldWizard.setName("Wizard 123");

        Wizard updateWizard = new Wizard();
        updateWizard.setId(123L);
        updateWizard.setName("Wizard 123-updated");

        given(wizardRepository.findById(123L)).willReturn(Optional.of(oldWizard));
        given(wizardRepository.save(oldWizard)).willReturn(oldWizard);

        //when
        Wizard newWizard=wizardServiceImpl.update(123L,updateWizard);

        assertThat(newWizard.getId()).isEqualTo(oldWizard.getId());
        assertThat(newWizard.getName()).isEqualTo(oldWizard.getName());
        verify(wizardRepository,times(1)).save(oldWizard);
        verify(wizardRepository,times(1)).save(newWizard);


    }

    @Test
    void testUpdateNotFound() {
        //given
        Wizard oldWizard = new Wizard();
        oldWizard.setId(123L);
        oldWizard.setName("Wizard 123");

        given(wizardRepository.findById(123L)).willReturn(Optional.empty());

        //when
        assertThrows(ObjectNotFoundException.class,()->{
            wizardServiceImpl.update(123L,oldWizard);
        });
        //then
        verify(wizardRepository,times(1)).findById(123L);

    }
    @Test
    void testDeleteSuccess() {
        //given
        Wizard oldWizard = new Wizard();
        oldWizard.setId(123L);
        given(wizardRepository.findById(123L)).willReturn(Optional.of(oldWizard));
        doNothing().when(wizardRepository).deleteById(123L);

        //When
        this.wizardServiceImpl.delete(123L);
        //then
        verify(wizardRepository,times(1)).deleteById(123L);
    }

    @Test
    void testDeleteNotFound() {
        //given
        given(wizardRepository.findById(123L)).willReturn(Optional.empty());
        //when
        assertThrows(ObjectNotFoundException.class,()->{
            wizardServiceImpl.delete(123L);
        });
        //then
        verify(wizardRepository,times(1)).findById(123L);
    }
}