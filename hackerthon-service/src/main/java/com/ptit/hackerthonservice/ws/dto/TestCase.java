package com.ptit.hackerthonservice.ws.dto;

import lombok.Data;

@Data
public class TestCase {
	private String input;
	private String expectedOutput;
	private int score;
}
