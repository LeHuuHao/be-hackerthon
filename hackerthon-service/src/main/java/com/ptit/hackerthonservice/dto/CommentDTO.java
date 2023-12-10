package com.ptit.hackerthonservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Data
public class CommentDTO {
	private Long id;

	@NotBlank
	private String description;

//	@JsonIncludeProperties({ "id", "displayName", "photoURL" })
//	private List<UserDTO> likers;

	private int likeNo;

	private Long replyToId;

	private int replyNo;

	private Long exerciseId;

	private Long solutionId;

	private Long forumId;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
	private Date createdAt;

	@JsonIncludeProperties({ "id", "displayName", "photoURL" })
	private UserDTO createdBy;

}