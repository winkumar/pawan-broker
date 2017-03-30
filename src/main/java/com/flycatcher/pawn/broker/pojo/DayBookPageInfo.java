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
 * @since 30-Mar-2017
 * 
 */

@EqualsAndHashCode(exclude={"dayBookInfos","pagePropertys"})
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude={"dayBookInfos","pagePropertys"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class DayBookPageInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<DayBookInfo> dayBookInfos;
	private Map<String,Long> pagePropertys;
	
}


