package com.ptit.hackerthonservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import com.ptit.hackerthonservice.utils.ActionEnum;
import com.ptit.hackerthonservice.utils.TagTypeEnum;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Data
public class UserActivityDTO {
	private Long id;

	private String description;

	@Enumerated(EnumType.STRING)
	private ActionEnum action;

	@Enumerated(EnumType.STRING)
	private TagTypeEnum type;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
	private Date createdAt;

	@JsonIncludeProperties({ "id", "displayName" })
	private UserDTO createdBy;
}
