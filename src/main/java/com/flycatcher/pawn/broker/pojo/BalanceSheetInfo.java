package com.flycatcher.pawn.broker.pojo;

import java.io.Serializable;

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
 * @since 08-Apr-2017
 * 
 */
@EqualsAndHashCode
@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceSheetInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String accountType;
	private Double balance;
	private Boolean isPositive;

}


