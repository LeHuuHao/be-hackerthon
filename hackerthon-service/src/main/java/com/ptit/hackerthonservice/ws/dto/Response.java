package com.ptit.hackerthonservice.ws.dto;

import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class Response {
	private String verdict;
	private int statusCode;
	private String error;
	private Map<String, TestCaseResult> testCasesResult;
	private float averageExecutionDuration;
	private int timeLimit;
	private int memoryLimit;
	private int compilationDuration;

	@Enumerated(EnumType.STRING)
	private Language language;

	private LocalDateTime dateTime;
}
