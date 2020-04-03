package com.scmitltda.ppmtool.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Backlog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	private Integer PTSequence = 0;
	private String projectIndentifier;
	
	// OneToOne with project
	
	// OneToMany projectTask
	
	public Backlog() {}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Integer getPTSequence() {
		return PTSequence;
	}

	public void setPTSequence(Integer pTSequence) {
		PTSequence = pTSequence;
	}

	public String getProjectIndentifier() {
		return projectIndentifier;
	}

	public void setProjectIndentifier(String projectIndentifier) {
		this.projectIndentifier = projectIndentifier;
	}
}
