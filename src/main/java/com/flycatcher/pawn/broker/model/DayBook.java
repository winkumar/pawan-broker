package com.flycatcher.pawn.broker.model;

import java.io.Serializable;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the day_book database table.
 * 
 */
@Entity
@Table(name="day_book")
@NamedQuery(name="DayBook.findAll", query="SELECT d FROM DayBook d")
public class DayBook implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DAY_BOOK_ID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DAY_BOOK_ID_GENERATOR")
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
	private String transactionType;

	//bi-directional many-to-one association to UserInfo
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="created_by")
	private UserInfo userInfo1;

	//bi-directional many-to-one association to UserInfo
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="modified_by")
	private UserInfo userInfo2;

	//bi-directional many-to-one association to Account
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="account_id")
	private Account account;

	public DayBook() {
	}

	public Long getDayBookId() {
		return this.dayBookId;
	}

	public void setDayBookId(Long dayBookId) {
		this.dayBookId = dayBookId;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public BigDecimal getTransactionAmount() {
		return this.transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getTransactionDesc() {
		return this.transactionDesc;
	}

	public void setTransactionDesc(String transactionDesc) {
		this.transactionDesc = transactionDesc;
	}

	public String getTransactionType() {
		return this.transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public UserInfo getUserInfo1() {
		return this.userInfo1;
	}

	public void setUserInfo1(UserInfo userInfo1) {
		this.userInfo1 = userInfo1;
	}

	public UserInfo getUserInfo2() {
		return this.userInfo2;
	}

	public void setUserInfo2(UserInfo userInfo2) {
		this.userInfo2 = userInfo2;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}