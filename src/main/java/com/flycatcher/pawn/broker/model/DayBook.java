package com.flycatcher.pawn.broker.model;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 * The persistent class for the day_book database table.
 * 
 */
@Entity
@Table(name="day_book")
@EqualsAndHashCode
@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DayBook implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="day_book_id")
	private Long dayBookId;

	@Temporal(TemporalType.DATE)
	@Column(name="created_date")
	private Date createdDate;

	@Temporal(TemporalType.DATE)
	@Column(name="modified_date")
	private Date modifiedDate;

	@Column(name="transaction_amount")
	private BigDecimal transactionAmount;

	@Column(name="transaction_desc")
	private String transactionDesc;

	@Column(name="transaction_type")
    @Enumerated(EnumType.STRING)
	private TransactionType transactionType;

	@Temporal(TemporalType.DATE)
	@Column(name="transaction_date")
	private Date transactionDate;
	
	//bi-directional many-to-one association to UserInfo
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="created_by")
	@JsonIgnore
	private UserInfo createdBy;

	//bi-directional many-to-one association to UserInfo
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="modified_by")
	@JsonIgnore
	private UserInfo modifiedBy;

	//bi-directional many-to-one association to Account
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="account_id")
	@JsonIgnore
	private Account account;

}

