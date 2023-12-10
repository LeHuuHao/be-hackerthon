package com.ptit.hackerthonservice.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "answer",schema = "ptit")
public class Answer extends CreateAuditable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String language;
	private String verdict;
	private String error;
	private int statusCode;
	private int timeLimit;
	private int memoryLimit;
	private int compilationDuration;
	private int averageExecutionDuration;
	
	private Long score;
	private String sourceCode;

	@OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TestCaseResult> testCasesResults;

	@ManyToOne
	private Exercise exercise;
}
