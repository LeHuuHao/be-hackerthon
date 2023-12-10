package com.ptit.hackerthonservice.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
public class ExerciseComment {

	@Id
	@Column(insertable = false)
	private Long commentId;

	@OneToOne(cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	@MapsId
	private Comment comment;

	@ManyToOne
	private Exercise exercise;

}