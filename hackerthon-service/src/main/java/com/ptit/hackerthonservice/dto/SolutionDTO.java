package com.ptit.hackerthonservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
public class SolutionDTO {

	private Long id;

	@Size(max = 200)
	@NotBlank
	private String title;

	private String slug;

	private String description;

	private Long statisticId;

	private int orderNo;

	@JsonIncludeProperties({ "id", "title", "slug" })
	private ExerciseDTO exercise;


	@JsonIncludeProperties({ "id", "title", "slug" })
	private List<TagDTO> tags;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
	private Date createdAt;

	@JsonIncludeProperties({ "id", "displayName", "photoURL" })
	private UserDTO createdBy;
}
