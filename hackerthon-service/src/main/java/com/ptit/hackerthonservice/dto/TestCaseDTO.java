package com.ptit.hackerthonservice.dto;

import lombok.Data;

@Data
public class TestCaseDTO {
	private Long id;

	private String input;

	private String expectedOutput;
	
	private int score;
}
