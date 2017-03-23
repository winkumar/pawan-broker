package com.flycatcher.pawn.broker.pojo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
 * @since 21-Mar-2017
 * 
 */

@EqualsAndHashCode(exclude={"accountInfos","pagePropertys"})
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude={"accountInfos","pagePropertys"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountPageInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<AccountInfo> accountInfos;
	private Map<String,Long> pagePropertys;
	
}
