package com.ptit.hackerthonservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ptit.hackerthonservice.utils.StatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = { "role" })
public class User extends TimeAuditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(unique = true)
	private String username;

	private String name;
	private String photoURL;
	private String password;
	private String phone;
	private String address;
	private String gender;
	private String note;
	private String email;
	private String website;
	private Boolean enabled;

	@JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
	private Date birthdate;

	@ManyToOne
	private Role role;

}