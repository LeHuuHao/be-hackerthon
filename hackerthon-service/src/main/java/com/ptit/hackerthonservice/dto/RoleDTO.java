package com.ptit.hackerthonservice.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(exclude = { "privileges" })
public class RoleDTO {
	private Integer id;
	private String name;
	private List<PrivilegeDTO> privileges;
}
