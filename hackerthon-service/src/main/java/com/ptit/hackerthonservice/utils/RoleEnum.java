package com.ptit.hackerthonservice.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter

public enum RoleEnum {
	ADMIN(1, "ROLE_ADMIN"), MANAGER(2, "ROLE_MANAGER"), EDITOR(3, "ROLE_EDITOR"), MEMBER(4, "ROLE_MEMBER"),
	ANONYMOUS(5, "ROLE_ANONYMOUS");

	private int roleId;
	private String roleName;

	RoleEnum(int roleId, String roleName) {
		this.roleId = roleId;
		this.roleName = roleName;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
