package com.surecost.inventory.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable implements Serializable {

	private static final long serialVersionUID = 2059295448600567528L;

	@CreatedBy
	@Column(name = "create_usr", updatable = false)
	private String createUsr;

	@CreatedDate
	@Column(name = "create_ts", columnDefinition = "DATETIME", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTs;

	@LastModifiedBy
	@Column(name = "update_usr")
	private String updateUsr;

	@LastModifiedDate
	@Column(name = "update_ts", columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTs;

}