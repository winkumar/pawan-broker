package com.flycatcher.pawn.broker.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the account database table.
 * 
 */
@Entity
@Table(name="account")
@NamedQuery(name="Account.findAll", query="SELECT a FROM Account a")
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="account_id")
	private String accountId;

	@Column(name="account_number")
	private String accountNumber;

	private String area;

	private String city;

	@Temporal(TemporalType.DATE)
	@Column(name="created_date")
	private Date createdDate;

	@Column(name="current_address")
	private String currentAddress;

	@Column(name="father_name")
	private String fatherName;

	@Column(name="first_name")
	private String firstName;

	@Column(name="last_name")
	private String lastName;

	@Temporal(TemporalType.DATE)
	@Column(name="modified_date")
	private Date modifiedDate;

	@Column(name="pin_code")
	private String pinCode;

	@Column(name="present_address")
	private String presentAddress;

	private String state;

	//bi-directional many-to-one association to UserInfo
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="created_by")
	private UserInfo userInfo1;

	//bi-directional many-to-one association to UserInfo
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="modified_by")
	private UserInfo userInfo2;

	//bi-directional many-to-one association to DayBook
	@OneToMany(mappedBy="account")
	private Set<DayBook> dayBooks;

	public Account() {
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountNumber() {
		return this.accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCurrentAddress() {
		return this.currentAddress;
	}

	public void setCurrentAddress(String currentAddress) {
		this.currentAddress = currentAddress;
	}

	public String getFatherName() {
		return this.fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getPinCode() {
		return this.pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getPresentAddress() {
		return this.presentAddress;
	}

	public void setPresentAddress(String presentAddress) {
		this.presentAddress = presentAddress;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
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

	public Set<DayBook> getDayBooks() {
		return this.dayBooks;
	}

	public void setDayBooks(Set<DayBook> dayBooks) {
		this.dayBooks = dayBooks;
	}

	public DayBook addDayBook(DayBook dayBook) {
		getDayBooks().add(dayBook);
		dayBook.setAccount(this);

		return dayBook;
	}

	public DayBook removeDayBook(DayBook dayBook) {
		getDayBooks().remove(dayBook);
		dayBook.setAccount(null);

		return dayBook;
	}

}