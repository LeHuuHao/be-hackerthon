package com.ptit.hackerthonservice.dto;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;

@Data
public class ProfileDTO {
	private String id;
	private int score;

	@JsonIncludeProperties({ "id", "displayName", "photoURL" })
	private UserDTO user;
}