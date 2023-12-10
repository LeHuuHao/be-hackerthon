package com.ptit.hackerthonservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(exclude = { "roles" })
public class PrivilegeDTO {
	private Integer id;

	private String authority;

	private String api;

	private String method;

	private boolean secured;

	private boolean authenticated;

	@JsonIgnoreProperties("privileges")
	private List<RoleDTO> roles;
}
