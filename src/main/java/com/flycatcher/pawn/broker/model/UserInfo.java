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
 * The persistent class for the user_info database table.
 * 
 */
@Entity
@Table(name="user_info")
@EqualsAndHashCode(exclude={"createdAccounts","modifiedAccounts","createdDayBooks","modifiedDayBooks","roles"})
@Getter
@Setter
@ToString(exclude={"createdAccounts","modifiedAccounts","createdDayBooks","modifiedDayBooks","roles"})
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="user_id")
	private Long userId;

	@Column(name="first_name")
	private String firstName;

	@Column(name="is_enable")
	private Boolean isEnable;

	@Temporal(TemporalType.DATE)
	@Column(name="last_login")
	private Date lastLogin;

	@Column(name="last_name")
	private String lastName;

	@Temporal(TemporalType.DATE)
	@Column(name="last_password_update")
	private Date lastPasswordUpdate;

	private String password;

	@Column(name="user_name")
	private String userName;

	//bi-directional many-to-one association to Account
	@OneToMany(mappedBy="createdBy",fetch=FetchType.LAZY)
	@JsonIgnore
	private Set<Account> createdAccounts;

	//bi-directional many-to-one association to Account
	@OneToMany(mappedBy="modifiedBy",fetch=FetchType.LAZY)
	@JsonIgnore
	private Set<Account> modifiedAccounts;

	//bi-directional many-to-one association to DayBook
	@OneToMany(mappedBy="createdBy",fetch=FetchType.LAZY)
	@JsonIgnore
	private Set<DayBook> createdDayBooks;

	//bi-directional many-to-one association to DayBook
	@OneToMany(mappedBy="modifiedBy",fetch=FetchType.LAZY)
	@JsonIgnore
	private Set<DayBook> modifiedDayBooks;

	//bi-directional many-to-many association to Role
	@ManyToMany(mappedBy="userInfos",fetch=FetchType.EAGER)
	@JsonIgnore
	private Set<Role> roles;

}