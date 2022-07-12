package com.surecost.inventory.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.is;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.surecost.inventory.dto.DrugInfoDTO;
import com.surecost.inventory.dto.DrugInfoWrapper;
import com.surecost.inventory.entity.Drug;
import com.surecost.inventory.service.DrugService;

@AutoConfigureMockMvc
@SpringBootTest
class DrugControllerTest {

	@Autowired
    MockMvc mockMvc;

	@Autowired
	ModelMapper mapper;
	
	@Autowired
	ObjectMapper objMapper;
	
    @MockBean
    private DrugService drugService;

    @Test
    void testGetAll() throws Exception {
    	List<Drug> results = new ArrayList<>(Arrays.asList(getDrugInfo()));
    	Mockito.when(drugService.findAll()).thenReturn(results);
    	mockMvc.perform(MockMvcRequestBuilders.get("/api/druginfo/all").contentType(MediaType.APPLICATION_JSON))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("$", hasSize(1)))
    		.andExpect(jsonPath("$[0].uid", is("identifier")));
    }
    
    @Test
    void testFetchByUid() throws Exception {
    	Optional<Drug> result = Optional.of(getDrugInfo());
    	Mockito.when(drugService.getDrugByUID("identifier")).thenReturn(result);
    	mockMvc.perform(MockMvcRequestBuilders.get("/api/druginfo/identifier").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.uid", is("identifier")))
			.andExpect(jsonPath("$.name", is("drug")));    	
    }
    
    @Test
    void testFetchByUidNotFound() throws Exception {
    	Optional<Drug> result = Optional.empty();
    	Mockito.when(drugService.getDrugByUID("identifier")).thenReturn(result);
    	mockMvc.perform(MockMvcRequestBuilders.get("/api/druginfo/identifier").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is4xxClientError());
    }    

    @Test
    void testFetchByManufacturer() throws Exception {
    	List<Drug> results = new ArrayList<>(Arrays.asList(getDrugInfo()));
    	Mockito.when(drugService.getDrugByManufacturer("galaxy") ).thenReturn(results);
    	mockMvc.perform(MockMvcRequestBuilders.get("/api/druginfo/manufacturer/galaxy").contentType(MediaType.APPLICATION_JSON))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("$", hasSize(1)))
    		.andExpect(jsonPath("$[0].manufacturer", is("galaxy")));
    }
    
    @Test
    void testFetchByManufacturerNotFound() throws Exception {
    	List<Drug> results = new ArrayList<>();
    	Mockito.when(drugService.getDrugByManufacturer("galaxy") ).thenReturn(results);
    	mockMvc.perform(MockMvcRequestBuilders.get("/api/druginfo/manufacturer/galaxy").contentType(MediaType.APPLICATION_JSON))
    		.andExpect(status().is4xxClientError());
    		
    }
    
    @Test
    void testBadRequest() throws Exception {
    	mockMvc.perform(MockMvcRequestBuilders.get("/api/bad/request").contentType(MediaType.APPLICATION_JSON))
    		.andExpect(status().is4xxClientError());
    }
    
    @Test
    void testCreateResources() throws Exception {
    	List<DrugInfoDTO> resultsDto = new ArrayList<>(Arrays.asList(getDrugInfoDTO()));
    	List<Drug> results = new ArrayList<>(Arrays.asList(getDrugInfo()));
    	DrugInfoWrapper drugInfoWrapper = new DrugInfoWrapper();
    	drugInfoWrapper.setDrugList(resultsDto);
    	String payload = objMapper.writeValueAsString(drugInfoWrapper);
    	Mockito.when(drugService.create(results)).thenReturn(results);
    	mockMvc.perform(MockMvcRequestBuilders.post("/api/druginfo")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(payload))
				.andExpect(status().isOk());    	
    }
    
    @Test
    void testCreateResourcesForNull() throws Exception {
    	mockMvc.perform(MockMvcRequestBuilders.post("/api/druginfo")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(StringUtils.EMPTY))
				.andExpect(status().is4xxClientError());    	
    }    
    
    private Drug getDrugInfo() {
    	Drug drugInfo = new Drug();
    	drugInfo.setUid("identifier");
    	drugInfo.setManufacturer("galaxy");
    	drugInfo.setName("drug");
    	drugInfo.setPrice(BigDecimal.valueOf(12.5));
    	drugInfo.setQuantity(10);
    	return drugInfo;
    }
    
    private DrugInfoDTO getDrugInfoDTO() {
    	DrugInfoDTO drugInfoDto = new DrugInfoDTO();
    	drugInfoDto.setManufacturer("galaxy");
    	drugInfoDto.setName("drug");
    	drugInfoDto.setPrice(BigDecimal.valueOf(12.5));
    	drugInfoDto.setQuantity(10);
    	return drugInfoDto;
    }    
}
