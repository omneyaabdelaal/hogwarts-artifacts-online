package com.omneya.hogwarts.hogwartsartifactsonline.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omneya.hogwarts.hogwartsartifactsonline.dto.WizardDto;
import com.omneya.hogwarts.hogwartsartifactsonline.exceptions.ObjectNotFoundException;
import com.omneya.hogwarts.hogwartsartifactsonline.models.Artifact;
import com.omneya.hogwarts.hogwartsartifactsonline.models.Wizard;
import com.omneya.hogwarts.hogwartsartifactsonline.services.servicesImpl.WizardServiceImpl;
import com.omneya.hogwarts.hogwartsartifactsonline.system.StatusCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc

class WizardControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    WizardServiceImpl wizardService;

    List<Wizard> wizards;
    @Value("${api.endpoint.base-url}")
    String baseUrl;

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
    void tearDown() {
    }


    @Test
    void testFindWizardByIdSuccess() throws Exception {
        //given
        Wizard wizard = new Wizard();
        wizard.setId(1L);
        wizard.setName("Wizard 1");

        given(wizardService.findById(1L)).willReturn(wizard);

        mockMvc.perform(get(this.baseUrl + "/wizards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Wizard found with id: 1"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Wizard 1"));

        verify(wizardService, times(1)).findById(1L);
    }

    @Test
    void testFindWizardByIdNotFound() throws Exception {
        //given
        given(wizardService.findById(1L)).willThrow(new ObjectNotFoundException(1L));

        //when and then

        mockMvc.perform(get(this.baseUrl + "/wizards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could Not Find Wizard with Id 1 :(!"))
                .andExpect(jsonPath("$.data").isEmpty());
        verify(wizardService, times(1)).findById(1L);
    }

    @Test
    void testFindAllWizards() throws Exception {
        //given
        given(wizardService.findAll()).willReturn(this.wizards);

        //when and then

        mockMvc.perform(get(this.baseUrl + "/wizards").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("All wizards found."))
                .andExpect(jsonPath("$.data[0].id").value(4))
                .andExpect(jsonPath("$.data[0].name").value("Wizard 4"));
        verify(wizardService, times(1)).findAll();

    }

    @Test
    void testAddWizardSuccess() throws Exception {
        //given
        WizardDto wizardDto = new WizardDto(null, "New-Wizard", 0);
        String json = objectMapper.writeValueAsString(wizardDto);

        Wizard wizard = new Wizard();
        wizard.setId(1L);
        wizard.setName("New-Wizard");

        given(wizardService.add(Mockito.any(Wizard.class))).willReturn(wizard);

        //when and then

        this.mockMvc.perform(post(this.baseUrl + "/wizards").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Wizard has been successfully added!"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("New-Wizard"));
    }

    @Test
    void testUpdateWizardSuccess() throws Exception {
        //given
        WizardDto wizardDto = new WizardDto(null, "Updated-Wizard", 0);
        String json = objectMapper.writeValueAsString(wizardDto);
        Wizard wizard = new Wizard();
        wizard.setId(1L);
        wizard.setName("Updated-Wizard");

        given(wizardService.update(Mockito.any(Long.class), Mockito.any(Wizard.class))).willReturn(wizard);

        //when and then

        mockMvc.perform(put(this.baseUrl + "/wizards/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Wizard has been successfully updated!"))
                .andExpect(jsonPath("$.data.id").value(wizard.getId()))
                .andExpect(jsonPath("$.data.name").value(wizard.getName()));

    }

    @Test
    void testUpdateWizardFailure() throws Exception {
        //given
        WizardDto wizardDto = new WizardDto(null, "Wizard", 0);
        String json = objectMapper.writeValueAsString(wizardDto);

        //when and then
        given(wizardService.update(Mockito.any(Long.class), Mockito.any(Wizard.class))).willThrow(new ObjectNotFoundException(1L));

        mockMvc.perform(put(this.baseUrl + "/wizards/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could Not Find Wizard with Id 1 :(!"))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    void testDeleteWizardSuccess() throws Exception {
        //given

        doNothing().when(wizardService).delete(1L);

        //when and then
        mockMvc.perform(delete(this.baseUrl + "/wizards/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Wizard has been deleted."))
                .andExpect(jsonPath("$.data").isEmpty());
        verify(wizardService, times(1)).delete(1L);
    }

    @Test
    void testDeleteWizardFailure() throws Exception {

        //given
        doThrow(
                new ObjectNotFoundException(1L))
                .when(wizardService).delete(1L);

        //when and then
        mockMvc.perform(delete(this.baseUrl + "/wizards/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could Not Find Wizard with Id 1 :(!"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactSuccess() throws Exception {
        //given
        doNothing().when(wizardService).assignArtifact(1L, "1");

        //when and then

        mockMvc.perform(put(this.baseUrl + "/wizards/1/artifacts/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Artifact Assignment Success!"))
                .andExpect(jsonPath("$.data").isEmpty());

    }


    @Test
    void testAssignArtifactWizardNonExistentError() throws Exception {
        //given
        doThrow(new ObjectNotFoundException(1L)).when(wizardService).assignArtifact(1L, "1");

        mockMvc.perform(put(this.baseUrl + "/wizards/1/artifacts/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could Not Find Wizard with Id 1 :(!"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactArtifactNonExistentError() throws Exception {
        //given
        doThrow(new ObjectNotFoundException("1")).when(wizardService).assignArtifact(1L, "1");

        mockMvc.perform(put(this.baseUrl + "/wizards/1/artifacts/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could Not Find Artifact with Id 1 :(!"))
                .andExpect(jsonPath("$.data").isEmpty());

    }

}

