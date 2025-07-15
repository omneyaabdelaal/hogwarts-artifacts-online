package com.omneya.hogwarts.hogwartsartifactsonline.services.servicesImpl;

import com.omneya.hogwarts.hogwartsartifactsonline.system.exceptions.ArtifactNotFoundException;
import com.omneya.hogwarts.hogwartsartifactsonline.models.Artifact;
import com.omneya.hogwarts.hogwartsartifactsonline.models.Wizard;
import com.omneya.hogwarts.hogwartsartifactsonline.repositories.ArtifactRepository;
import com.omneya.hogwarts.hogwartsartifactsonline.utils.IdWorker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ArtifactServiceImplTest {
    @Mock
    ArtifactRepository artifactRepository;
    @Mock
    IdWorker  idWorker;
    @InjectMocks
    ArtifactServiceImpl artifactServiceImpl;
    List<Artifact> artifacts;

    @BeforeEach
    void setUp() {

        Artifact a1=new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that" +
                " resembles a cigarette lighter." +
                " It is used to remove or absorb (as well as return) " +
                "the light from any light source to provide cover to the user.");
        a1.setImageURL("ImageUrl");


        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageURL("ImageUrl");

        artifacts=new ArrayList<>();
        this.artifacts.add(a1);
        this.artifacts.add(a2);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByidSuccess() {
        //Given. Arrange inputs and targets. Define the behavior of mock object artifactRepository.

        /*
         "id": "1250808601744904192",
         "name": "Invisibility Cloak",
         "description": "An invisibility cloak is used to make the wearer invisible.",
         "imageUrl": "ImageUrl"
        */
        Artifact artifact = new Artifact();
        artifact.setId("1250808601744904192");
        artifact.setName("Invisibility Cloak");
        artifact.setDescription("An invisibility cloak is used to make the wearer invisible.");
        artifact.setImageURL("ImageUrl");

        Wizard wizard = new Wizard();
        wizard.setId(2L);
        wizard.setName("Harry Potter");

        artifact.setOwner(wizard);

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(artifact));
        //When. Act on the target behavior. When steps should cover the method to be tested.
        Artifact returnedArtifact=artifactServiceImpl.findByid("1250808601744904192");
        //Then. Assert expected outcomes.
        assertThat(returnedArtifact.getId()).isEqualTo(artifact.getId());
        assertThat(returnedArtifact.getName()).isEqualTo(artifact.getName());
        assertThat(returnedArtifact.getDescription()).isEqualTo(artifact.getDescription());
        assertThat(returnedArtifact.getImageURL()).isEqualTo(artifact.getImageURL());
        verify(artifactRepository, times(1)).findById("1250808601744904192");
    }

    @Test
    void testFindByidNotFound() {
        //Given
        given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());
        //When
        Throwable throwable = catchThrowable(()->{
            Artifact artifact=artifactServiceImpl.findByid("1250808601744904192");

        });
        //Then
        assertThat(throwable)
                .isInstanceOf(ArtifactNotFoundException.class)
                .hasMessage("Artifact with id 1250808601744904192 not found :(");
        verify(artifactRepository, times(1)).findById("1250808601744904192");



    }

    @Test
     void testFindAllSuccess() {
        //Given
        given(artifactRepository.findAll()).willReturn(this.artifacts);
        //When
        List<Artifact> actualArtifacts=artifactServiceImpl.findAll();
        //Then
        assertThat(this.artifacts.size()).isEqualTo(actualArtifacts.size());
        verify(this.artifactRepository, times(1)).findAll();


    }

    @Test
    void testAddSuccess() {
        //Given
        Artifact newArtifact = new Artifact();
        newArtifact.setName("Artifact 3");
        newArtifact.setDescription("Artifact description 3");
        newArtifact.setImageURL("ImageUrl");

        given(idWorker.nextId()).willReturn(12345L);
        given(artifactRepository.save(newArtifact)).willReturn(newArtifact);

        //When
        Artifact addedArtifact =artifactServiceImpl.add(newArtifact);

        //then
        assertThat(addedArtifact.getId()).isEqualTo("12345");
        assertThat(addedArtifact.getName()).isEqualTo(newArtifact.getName());
        assertThat(addedArtifact.getDescription()).isEqualTo(newArtifact.getDescription());
        assertThat(addedArtifact.getImageURL()).isEqualTo(newArtifact.getImageURL());

        verify(artifactRepository, times(1)).save(newArtifact);
    }

    @Test
    public void testUpdateArtifactSuccess(){
        //GIVEN
        Artifact oldArtifact = new Artifact();
        oldArtifact.setId("1250808601744904192");
        oldArtifact.setName("Invisibility Cloak");
        oldArtifact.setDescription("An invisibility cloak is used to make the wearer invisible.");
        oldArtifact.setImageURL("ImageUrl");

        Artifact newArtifact = new Artifact();
        newArtifact.setId("1250808601744904192");
        newArtifact.setName("Invisibility Cloak");
        newArtifact.setDescription("Updated-Description");
        newArtifact.setImageURL("ImageUrl");

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(oldArtifact));
        given(artifactRepository.save(oldArtifact)).willReturn(oldArtifact);

        //WHEN

        Artifact updatedArtifact =artifactServiceImpl.update("1250808601744904192",newArtifact);

        //THEN

        assertThat(updatedArtifact.getId()).isEqualTo(oldArtifact.getId());
        assertThat(updatedArtifact.getDescription()).isEqualTo(newArtifact.getDescription());
        verify(artifactRepository, times(1)).findById("1250808601744904192");
        verify(artifactRepository, times(1)).save(oldArtifact);


    }

    @Test
    public void testUpdateArtifactNotFound(){
        //GIVEN
        Artifact newArtifact = new Artifact();
        newArtifact.setName("Invisibility Cloak");
        newArtifact.setDescription("Updated-Description");
        newArtifact.setImageURL("ImageUrl");

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());

        //WHEN and then

        assertThrows(ArtifactNotFoundException.class,()->{
            artifactServiceImpl.update("1250808601744904192",newArtifact);
        });

        verify(artifactRepository,times(1)).findById("1250808601744904192");
    }

    @Test

    public void testDeleteArtifactSuccess() {
        //given
        Artifact  artifact = new Artifact();
        artifact.setId("123");
        artifact.setName("Artifact 1");
        artifact.setDescription("Artifact description");
        artifact.setImageURL("ImageUrl");

        given(artifactRepository.findById("123")).willReturn(Optional.of(artifact));
        doNothing().when(artifactRepository).deleteById("123");
        //when
        artifactServiceImpl.delete("123");

        verify(artifactRepository, times(1)).deleteById("123");


    }

    @Test
    public void testDeleteArtifactNotFound() {
        //Given
        given(artifactRepository.findById("123")).willReturn(Optional.empty());

        //When
        assertThrows(ArtifactNotFoundException.class,()->artifactServiceImpl.delete("123"));

        //then
        verify(artifactRepository,times(1)).findById("123");
    }


}