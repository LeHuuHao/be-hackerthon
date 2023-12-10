package com.ptit.hackerthonservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AnswerDTO {
	private Long id;

	private String language;
	private String verdict;
	private String error;
	private int statusCode;
	private int timeLimit;
	private int memoryLimit;
	private int compilationDuration;
	private int averageExecutionDuration;

	private int score;
	private String sourceCode;

	@JsonIncludeProperties({ "slug", "id", "title" })
	private ExerciseDTO exercise;

	private List<TestCaseResulTDTO> testCasesResults;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
	private Date createdAt;

	@JsonIncludeProperties({ "id", "username", "photoURL" })
	private UserDTO createdBy;
}