package com.ptit.hackerthonservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.ptit.hackerthonservice.utils.StatusEnum;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Data
public class ExamDTO {
	private Long id;
	
	@NotBlank
	private String title;
	private String description;
	private String metaDescription;
	private String featureImage;// image url
	private int orderNo;// STT

	@Enumerated(EnumType.STRING)
	private StatusEnum status;
	
	@JsonIncludeProperties({ "id", "title", "slug" })
	private List<TagDTO> tags;

	private String slug;


	private String endAt;

	private boolean endTime;

	private String startAt;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
	private Date createdAt;

	@JsonIncludeProperties({ "id", "username", "photoURL" })
	private UserDTO createdBy;


}