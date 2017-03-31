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
 * @since 23-Mar-2017
 * 
 */
@EqualsAndHashCode
@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DayBookInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long dayBookId;
	private Date createdDate;
	private Date modifiedDate;
	private Double transactionAmount;
	private String transactionDesc;
	
	private String transactionType;
	private Date transactionDate;
	private String createdBy;
	private String modifiedBy;
	private Long accountId;
	private String accountNumber;
	private String accountName;

}
