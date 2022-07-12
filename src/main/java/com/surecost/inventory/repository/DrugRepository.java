package com.surecost.inventory.repository;

import com.surecost.inventory.entity.Drug;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface DrugRepository extends JpaRepository<Drug, String> {
	
	List<Drug> getDrugByManufacturer(String manufacturer);
	
}
