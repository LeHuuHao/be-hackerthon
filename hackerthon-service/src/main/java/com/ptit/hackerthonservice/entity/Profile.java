package com.ptit.hackerthonservice.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Profile {
	@Id
	private String id;

	private int score;

	@OneToOne(cascade = CascadeType.ALL)
	private Statistic statistic;

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private User user;

}
