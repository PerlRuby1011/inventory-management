package com.surecost.inventory.dto;

import java.io.Serializable;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class DrugInfoWrapper implements Serializable{

	private static final long serialVersionUID = -2420997122100730644L;
	
	private List<DrugInfoDTO> drugList;
}
