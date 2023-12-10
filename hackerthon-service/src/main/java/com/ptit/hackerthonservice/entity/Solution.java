package com.ptit.hackerthonservice.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Entity
public class Solution extends CreateAuditable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(insertable = false)
	private Long id;

	private String title;

	private String slug;

	private String description;

	private int orderNo;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Statistic statistic;

	@OneToOne(mappedBy = "solution", cascade = CascadeType.ALL)
	private ExerciseSolution exerciseSolution;

	@ManyToMany
	@JoinTable(name = "solution_tag", joinColumns = @JoinColumn(name = "solution_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private List<Tag> tags;

	@OneToMany(mappedBy = "solution", cascade = CascadeType.REMOVE)
	private List<SolutionComment> comments;
}
