package com.flycatcher.pawn.broker.pojo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Double balance;
	private Double creditTotal;
	private Double debitTotal;
	private boolean isPositive;
}
