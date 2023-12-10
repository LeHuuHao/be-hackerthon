package com.ptit.hackerthonservice.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class TestCase {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String input;

	private String expectedOutput;

	private int score;

	@ManyToOne
	private Exercise exercise;
}
