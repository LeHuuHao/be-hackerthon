package com.ptit.hackerthonservice.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class TestCaseResult {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String verdict;
	private int verdictStatusCode;
	private String error;
	private String output;
	private String expectedOutput;
	private int executionDuration;

	private String input;
	private int score;

	@ManyToOne
	private Answer answer;
}
