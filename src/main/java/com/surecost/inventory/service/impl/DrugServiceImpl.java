package com.surecost.inventory.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.surecost.inventory.entity.Drug;
import com.surecost.inventory.repository.DrugRepository;
import com.surecost.inventory.service.DrugService;

@Service
public class DrugServiceImpl implements DrugService {

	private static final Logger logger = LoggerFactory.getLogger(DrugServiceImpl.class);
	
	@Autowired
	private DrugRepository drugRepository;
	
	@Override
	public Optional<Drug> getDrugByUID(String uid) {
		logger.debug("retrieving data for uid {}", uid);
		return drugRepository.findById(uid);
	}

	@Override
	public List<Drug> findAll() {
		return drugRepository.findAll();
	}
	
	@Override
	public List<Drug> create(List<Drug> drugList) {
		logger.debug("persisting list of size {}", drugList.size());
		return drugRepository.saveAll(drugList);
	}

	@Override
	public boolean doesExist(String uid) {
		return drugRepository.existsById(uid);
	}

	@Override
	public List<Drug> getDrugByManufacturer(String manufacturer) {
		logger.debug("retrieving data for manufacturer {}", manufacturer);
		return drugRepository.getDrugByManufacturer(manufacturer);
	}

}
