package com.ptit.hackerthonservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseResulTDTO {
	private Long id;

	private String verdict;
	private int verdictStatusCode;
	private String error;
	private String output;
	private String expectedOutput;
	private int executionDuration;

	private String input;
	private int score;

}
