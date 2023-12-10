package com.ptit.hackerthonservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.ptit.hackerthonservice.entity.Answer;
import com.ptit.hackerthonservice.utils.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDTO {
	private Long id;

	private String title;

	private String description;

	private String metaDescription;

	private String instruction;// huong dan

	private int memoryLimit;

	private int timeLimit;

	private String slug;

	private String sourcecode;

	@Enumerated(EnumType.STRING)
	private StatusEnum status;

	@JsonIgnoreProperties("exercise")
	private List<TestCaseDTO> testCases;


	@JsonIncludeProperties({ "id", "title", "slug", "type" })
	private List<TagDTO> tags;
	
	@JsonIncludeProperties({ "id", "title", "slug", "type" })
	private TagDTO level;

	private Long statisticId;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
	private Date createdAt;

	@JsonIncludeProperties({ "id", "username", "photoURL" })
	private UserDTO createdBy;

	private Integer version;

	private Long answerNo;
	
	private Long acceptedNo;
}