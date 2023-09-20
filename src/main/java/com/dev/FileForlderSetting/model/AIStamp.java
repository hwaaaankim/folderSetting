package com.dev.FileForlderSetting.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name="tb_ai")
@Entity
public class AIStamp {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="AI_ID")
	private Long id;
	
	@Column(name="AI_ANIMAL")
	private String animal;
	
	@Column(name="AI_ACTION")
	private String action;
	
	@Column(name="AI_POINT")
	private String point;
	
	@Column(name="AI_ENV")
	private String env;
	
	@Column(name="AI_STYLE")
	private String style;
	
	@Column(name="AI_PROMPT_STYLE")
	private String prompt;
}


























