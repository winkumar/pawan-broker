package com.flycatcher.pawn.broker.model;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The persistent class for the account database table.
 * 
 */
@Entity
@Table(name="account")
@EqualsAndHashCode(exclude={"dayBooks"})
@Getter
@Setter
@ToString(exclude={"dayBooks"})
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="account_id")
	private Long accountId;

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
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="created_by")
	@JsonIgnore
	private UserInfo createdBy;

	//bi-directional many-to-one association to UserInfo
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="modified_by")
	@JsonIgnore
	private UserInfo modifiedBy;
	
	//bi-directional many-to-one association to AccountType
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="account_type")
	@JsonIgnore
	private AccountType accountType;

	//bi-directional many-to-one association to DayBook
	@OneToMany(mappedBy="account",fetch=FetchType.LAZY)
	@JsonIgnore
	private Set<DayBook> dayBooks;



}