package com.ptit.hackerthonservice.entity;

import com.ptit.hackerthonservice.utils.ActionEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class UserActivity extends CreateAuditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String description;

	@Enumerated(EnumType.STRING)
	private ActionEnum action;

	private String type;
}
