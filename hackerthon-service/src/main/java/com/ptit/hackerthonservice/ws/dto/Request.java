package com.ptit.hackerthonservice.ws.dto;

import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Map;

@Data
public class Request {
	@Enumerated(EnumType.STRING)
	private Language language;

	private int memoryLimit;

	private int timeLimit;

	private String sourcecode;

	private Map<String, TestCase> testCases;

}