package com.ptit.hackerthonservice.entity;

import com.ptit.hackerthonservice.utils.StatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "exercise",schema = "ptit")
@EqualsAndHashCode(callSuper = true)
@EntityListeners(EntityListener.class)
public class Exercise extends CreateAuditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	private String description;

	private String metaDescription;

	private String instruction;// huong dan

	private int memoryLimit;

	private int timeLimit;

	@Column(unique = true)
	private String slug;

	private String sourcecode;

	@Enumerated(EnumType.STRING)
	private StatusEnum status;

	@OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("id ASC")
	private List<TestCase> testCases;

	@ManyToMany
	@JoinTable(name = "exercise_tag", joinColumns = @JoinColumn(name = "exercise_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private List<Tag> tags;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Statistic statistic;

	@OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ExerciseRevision> revisions;

	@Version
	private Integer version;

	@Formula("(SELECT COUNT(a.id) FROM answer a WHERE a.exercise_id = id)")
	private Long answerNo;
	
	@Formula("(SELECT COUNT(a.id) FROM answer a WHERE a.exercise_id = id AND a.status_code = 100)")
	private Long acceptedNo;
}
