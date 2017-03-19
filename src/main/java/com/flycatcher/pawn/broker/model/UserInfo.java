package com.flycatcher.pawn.broker.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the user_info database table.
 * 
 */
@Entity
@Table(name="user_info")
@NamedQuery(name="UserInfo.findAll", query="SELECT u FROM UserInfo u")
public class UserInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="USER_INFO_USERID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USER_INFO_USERID_GENERATOR")
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
	@OneToMany(mappedBy="userInfo1")
	private Set<Account> accounts1;

	//bi-directional many-to-one association to Account
	@OneToMany(mappedBy="userInfo2")
	private Set<Account> accounts2;

	//bi-directional many-to-one association to DayBook
	@OneToMany(mappedBy="userInfo1")
	private Set<DayBook> dayBooks1;

	//bi-directional many-to-one association to DayBook
	@OneToMany(mappedBy="userInfo2")
	private Set<DayBook> dayBooks2;

	//bi-directional many-to-many association to Role
	@ManyToMany(mappedBy="userInfos")
	private Set<Role> roles;

	public UserInfo() {
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Boolean getIsEnable() {
		return this.isEnable;
	}

	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}

	public Date getLastLogin() {
		return this.lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getLastPasswordUpdate() {
		return this.lastPasswordUpdate;
	}

	public void setLastPasswordUpdate(Date lastPasswordUpdate) {
		this.lastPasswordUpdate = lastPasswordUpdate;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Set<Account> getAccounts1() {
		return this.accounts1;
	}

	public void setAccounts1(Set<Account> accounts1) {
		this.accounts1 = accounts1;
	}

	public Account addAccounts1(Account accounts1) {
		getAccounts1().add(accounts1);
		accounts1.setUserInfo1(this);

		return accounts1;
	}

	public Account removeAccounts1(Account accounts1) {
		getAccounts1().remove(accounts1);
		accounts1.setUserInfo1(null);

		return accounts1;
	}

	public Set<Account> getAccounts2() {
		return this.accounts2;
	}

	public void setAccounts2(Set<Account> accounts2) {
		this.accounts2 = accounts2;
	}

	public Account addAccounts2(Account accounts2) {
		getAccounts2().add(accounts2);
		accounts2.setUserInfo2(this);

		return accounts2;
	}

	public Account removeAccounts2(Account accounts2) {
		getAccounts2().remove(accounts2);
		accounts2.setUserInfo2(null);

		return accounts2;
	}

	public Set<DayBook> getDayBooks1() {
		return this.dayBooks1;
	}

	public void setDayBooks1(Set<DayBook> dayBooks1) {
		this.dayBooks1 = dayBooks1;
	}

	public DayBook addDayBooks1(DayBook dayBooks1) {
		getDayBooks1().add(dayBooks1);
		dayBooks1.setUserInfo1(this);

		return dayBooks1;
	}

	public DayBook removeDayBooks1(DayBook dayBooks1) {
		getDayBooks1().remove(dayBooks1);
		dayBooks1.setUserInfo1(null);

		return dayBooks1;
	}

	public Set<DayBook> getDayBooks2() {
		return this.dayBooks2;
	}

	public void setDayBooks2(Set<DayBook> dayBooks2) {
		this.dayBooks2 = dayBooks2;
	}

	public DayBook addDayBooks2(DayBook dayBooks2) {
		getDayBooks2().add(dayBooks2);
		dayBooks2.setUserInfo2(this);

		return dayBooks2;
	}

	public DayBook removeDayBooks2(DayBook dayBooks2) {
		getDayBooks2().remove(dayBooks2);
		dayBooks2.setUserInfo2(null);

		return dayBooks2;
	}

	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

}