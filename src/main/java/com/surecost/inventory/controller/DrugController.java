package com.surecost.inventory.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.surecost.inventory.dto.DrugInfoDTO;
import com.surecost.inventory.dto.DrugInfoWrapper;
import com.surecost.inventory.entity.Drug;
import com.surecost.inventory.exception.ResourceNotFoundException;
import com.surecost.inventory.service.DrugService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Controller to fetch and add drug info from/into the system. Endpoints available here can be access via Swagger UI too.
 * 
 * @author mm
 *
 */
@RestController
@RequestMapping("/api/druginfo")
@CrossOrigin
//TODO: Add distributed caching mechanism, preferably using Redis like: @Cacheable(value="drug", key = "#uuid") Redis can be configured as Hibernate Secondary Cache as well. Easy to wire with AWS ElastiCache 
//TODO: Add endpoint authorization like: @PreAuthorize("hasAuthority('role')"). 

public class DrugController {
	private static final Logger logger = LoggerFactory.getLogger(DrugController.class);
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private DrugService drugService;
	
	@Operation(summary = "Get drug info passing in UID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "return the drug info", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DrugInfoDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "resource not found", content = @Content) })
	@GetMapping("/{uuid}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<DrugInfoDTO> fetchByUid(@PathVariable String uuid) {
		
		logger.info("fetching drug info for uuid {}", uuid);
		
		Optional<Drug> drugInfo = drugService.getDrugByUID(uuid);
		
		if(drugInfo.isPresent()) {
			return ResponseEntity.ok(modelMapper.map(drugInfo.get(), DrugInfoDTO.class));
		} else {
			throw new ResourceNotFoundException("resource not found!");
		}
	}
	
	@Operation(summary = "Get drug info passing in manufacturer info")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "return drug info as list", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DrugInfoDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "resource not found", content = @Content) })	
	@GetMapping("/manufacturer/{manufacturer}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<DrugInfoDTO>> fetchByManufacturer(@PathVariable String manufacturer) {
	
		logger.info("fetching drug info for manufacturer {}", manufacturer);
		
		List<Drug> drugInfo = drugService.getDrugByManufacturer(manufacturer);
		
		if(drugInfo.isEmpty()) {
			throw new ResourceNotFoundException("resource not found!");
		} else {
			return ResponseEntity.ok(drugInfo.stream().map(drug -> modelMapper.map(drug, DrugInfoDTO.class)).toList());
		}
	}
	
	@GetMapping("/all")
	@ResponseStatus(HttpStatus.OK)
	public List<DrugInfoDTO> getAllDrugInfo() {
	
		logger.info("fetching all info!");
		
		return drugService.findAll().stream().map(drug -> modelMapper.map(drug, DrugInfoDTO.class)).toList();
	}
	
	@Operation(summary = "Create drug info based on the incoming json message")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "resource(s) created", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DrugInfoDTO.class)) }),
			@ApiResponse(responseCode = "406", description = "invalid input", content = @Content) })	
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<List<DrugInfoDTO>> createResources(@RequestBody DrugInfoWrapper drugInfoWrapper) {
		
		List<Drug> drugList = Optional.ofNullable(drugInfoWrapper.getDrugList())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "invalid input"))
				.stream()
				.filter(Objects::nonNull).map(drugDto ->  modelMapper.map(drugDto, Drug.class))
				.toList();
		
		logger.info("saving list of size {}", drugList.size());
		
		drugList = drugService.create(drugList);
		
		logger.debug("successfuly persisted {} objects ", drugList.size());
		
		return ResponseEntity.ok(drugList.stream().map(drug -> modelMapper.map(drug, DrugInfoDTO.class)).toList());
	}
}
