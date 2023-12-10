package com.ptit.hackerthonservice.ws.dto;

import lombok.Data;

@Data
public class TestCaseResult {
	private String verdict;
	private int verdictStatusCode;
	private String output;
	private String error;
	private String expectedOutput;
	private int executionDuration;
}
