package com.surecost.inventory.service;

import java.util.List;
import java.util.Optional;

import com.surecost.inventory.entity.Drug;

public interface DrugService {

	Optional<Drug> getDrugByUID(String uid);
	
	List<Drug> getDrugByManufacturer(String manufacturer);
	
	List<Drug> findAll();

	List<Drug> create(List<Drug> drugList);
	
	boolean doesExist(String uid);
}
