package com.omneya.hogwarts.hogwartsartifactsonline.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omneya.hogwarts.hogwartsartifactsonline.dto.ArtifactDto;
import com.omneya.hogwarts.hogwartsartifactsonline.exceptions.ArtifactNotFoundException;
import com.omneya.hogwarts.hogwartsartifactsonline.models.Artifact;
import com.omneya.hogwarts.hogwartsartifactsonline.services.servicesImpl.ArtifactServiceImpl;
import com.omneya.hogwarts.hogwartsartifactsonline.system.StatusCode;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class ArtifactControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    ArtifactServiceImpl artifactServiceImpl;

    @Autowired
    ObjectMapper objectMapper;

    List<Artifact> artifacts;


    @BeforeEach
    void setUp() {

        this.artifacts = new ArrayList<>();

        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that" +
                " resembles a cigarette lighter." +
                " It is used to remove or absorb (as well as return) " +
                "the light from any light source to provide cover to the user.");
        a1.setImageURL("ImageUrl");
        artifacts.add(a1);

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageURL("ImageUrl");
        artifacts.add(a2);

        Artifact a3 = new Artifact();
        a3.setId("1250808601744904193");
        a3.setName("Elder Wand");
        a3.setDescription("The Elder Wand, known throughout history as" +
                " the Deathstick or the Wand of Destiny, is an extremely" +
                " powerful wand made of elder wood with a core of Thestral tail hair.");
        a3.setImageURL("ImageUrl");
        artifacts.add(a3);

        Artifact a4 = new Artifact();
        a4.setId("1250808601744904194");
        a4.setName("The Marauder's Map");
        a4.setDescription("A magical map of Hogwarts created by Remus Lupin," +
                " Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        a4.setImageURL("ImageUrl");
        artifacts.add(a4);

        Artifact a5 = new Artifact();
        a5.setId("1250808601744904195");
        a5.setName("The Sword Of Gryffindor");
        a5.setDescription("A goblin-made sword adorned with large rubies on the pommel." +
                " It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
        a5.setImageURL("ImageUrl");
        artifacts.add(a5);

        Artifact a6 = new Artifact();
        a6.setId("1250808601744904196");
        a6.setName("Resurrection Stone");
        a6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones," +
                " in a semi-physical form, and communicate with them.");
        a6.setImageURL("ImageUrl");
        artifacts.add(a6);


    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindArtifactByIdSuccess() throws Exception {
        //given
        given(this.artifactServiceImpl.findByid("1250808601744904191")).willReturn(artifacts.get(0));

        //when and then

        this.mockMvc.perform(get("/api/v1/artifacts/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Successfully!"))
                .andExpect(jsonPath("$.data.id").value("1250808601744904191"))
                .andExpect(jsonPath("$.data.name").value("Deluminator"));
    }

    @Test
    void testFindArtifactByIdNotFound() throws Exception {
        given(this.artifactServiceImpl.findByid("1250808601744904191")).willThrow(new ArtifactNotFoundException("1250808601744904191"));

        this.mockMvc.perform(get("/api/v1/artifacts/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could Not Find Artifact with Id 1250808601744904191 :(!"))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

    @Test
    void testFindAllArtifactsSuccess() throws Exception {
        //Given
        given(this.artifactServiceImpl.findAll()).willReturn(artifacts);
        //When and Then
        this.mockMvc.perform(get("/api/v1/artifacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Successfully!"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(artifacts.size())  ))
                .andExpect(jsonPath("$.data[0].id").value("1250808601744904191"))
                .andExpect(jsonPath("$.data[0].name").value("Deluminator"))


        ;
    }

    @Test
    void testAddArtifactSuccess() throws Exception {
        //Given

        ArtifactDto artifactDto = new ArtifactDto(null,"Artifact","Desc...","imgUrl",null);
        String json= objectMapper.writeValueAsString(artifactDto);

        Artifact savedArtifact=new Artifact(
                "1250808601744904197",
                "Artifact",
                "Desc...",
                "imgUrl",
                null
        );

        given(this.artifactServiceImpl.add(Mockito.any(Artifact.class))).willReturn(savedArtifact);

        //When and Then

        mockMvc.perform(post("/api/v1/artifacts").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Artifact Successfully!"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(savedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(savedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(savedArtifact.getImageURL()));
    }

    @Test
    void testUpdateArtifactSuccess() throws Exception {
        //Given

        ArtifactDto artifactDto = new ArtifactDto(
                "1250808601744904191",
                "Invisibility Cloak",
                "Updated-Description",
                "imgUrl",null);

        String json= objectMapper.writeValueAsString(artifactDto);

        Artifact newArtifact = new Artifact();
        newArtifact.setId("1250808601744904191");
        newArtifact.setName("Invisibility Cloak");
        newArtifact.setDescription("Updated-Description");
        newArtifact.setImageURL("ImageUrl");



        given(artifactServiceImpl.update(eq("1250808601744904191"),Mockito.any(Artifact.class))).willReturn(newArtifact);

        //When And Then

        mockMvc.perform(
                put("/api/v1/artifacts/1250808601744904191")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Artifact Successfully!"))
                .andExpect(jsonPath("$.data.id").value("1250808601744904191"))
                .andExpect(jsonPath("$.data.name").value(newArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(newArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(newArtifact.getImageURL()));


    }

    @Test
    public void testUpdateArtifactErrorWithNonExistenId() throws Exception {

        ArtifactDto artifactDto = new ArtifactDto(
                "1250808601744904191",
                "Invisibility Cloak",
                "Updated-Description",
                "imgUrl",null);

        String json= objectMapper.writeValueAsString(artifactDto);

        given(artifactServiceImpl.update(eq("1250808601744904191"),Mockito.any(Artifact.class))).willThrow(new ArtifactNotFoundException("1250808601744904191"));
        mockMvc.perform(put("/api/v1/artifacts/1250808601744904191")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could Not Find Artifact with Id 1250808601744904191 :(!"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    public void testDeleteArtifactSuccess() throws Exception {
        //given

        doNothing().when(artifactServiceImpl).delete("1250808601744904191");

        //when and then

        mockMvc.perform(

                delete("/api/v1/artifacts/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Artifact Successfully!"))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    public void testDeleteArtifactErrorWithNonExistenId() throws Exception {
        //given
        doThrow(
                new ArtifactNotFoundException("1250808601744904191"))
                .when(artifactServiceImpl).delete("1250808601744904191");

        //when and then

        mockMvc.perform(delete("/api/v1/artifacts/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could Not Find Artifact with Id 1250808601744904191 :(!"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}