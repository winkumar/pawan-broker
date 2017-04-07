package com.flycatcher.pawn.broker.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * @author kumar
 * @version 1.0.0
 * @since 06-Apr-2017
 * 
 */
@Entity
@Table(name="account_type")
@EqualsAndHashCode(exclude={"accounts"})
@Getter
@Setter
@ToString(exclude={"accounts"})
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="account_type")
	private String accountType;
	
	@Column(name="account_type_desc")
	private String accountTypeDesc;
	
	@Column(name="priority")
	private Integer priority;
	
	@Column(name="acc_start_from")
	private String accStartFrom;
	

	//bi-directional many-to-one association to Account
	@OneToMany(mappedBy="accountType",fetch=FetchType.LAZY)
	@JsonIgnore
	private Set<Account> accounts;
}


