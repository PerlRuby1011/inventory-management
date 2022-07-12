package com.surecost.inventory.dto;

import java.io.Serializable;
import java.math.BigDecimal;

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
public class DrugInfoDTO implements Serializable {

	private static final long serialVersionUID = -1887530665067888850L;
	
	private String uid;
	
	private String manufacturer;
	
	private String name;

	private int quantity;
	
	private BigDecimal price;
	
}
