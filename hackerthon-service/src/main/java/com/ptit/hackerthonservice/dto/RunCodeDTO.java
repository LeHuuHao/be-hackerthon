package com.ptit.hackerthonservice.dto;

import com.ptit.hackerthonservice.ws.dto.Language;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RunCodeDTO {
	private String input;

	@Enumerated(EnumType.STRING)
	private Language language;

	private String sourcecode;

	@Default
	private List<TestCaseDTO> testCases = new ArrayList<>();

	private Long exerciseId;

	private ExamDTO exam;
}
