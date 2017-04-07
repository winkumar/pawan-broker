package com.flycatcher.pawn.broker.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * @author kumar
 * @version 1.0.0
 * @since 21-Mar-2017
 * 
 */
@EqualsAndHashCode
@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long accountId;
	
	private String accountNumber;

	private String area;

	private String city;

	private Date createdDate;

	private String currentAddress;

	private String fatherName;

	private String firstName;

	private String lastName;

	private Date modifiedDate;

	private String pinCode;

	private String presentAddress;

	private String state;

	private String createdBy;

	private String modifiedBy;
	
	private Long accountTypeId;
	
	
}


