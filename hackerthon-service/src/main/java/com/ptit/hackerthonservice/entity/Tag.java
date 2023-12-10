package com.ptit.hackerthonservice.entity;

import com.ptit.hackerthonservice.utils.TagTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@EntityListeners(EntityListener.class)
@Table(schema = "ptit")
public class Tag extends CreateAuditable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(unique = true)
	private String title;

	private String description;

	private int orderNo;// STT

	private String featureImage;// image url

	@Column(unique = true)
	private String slug;

	private String metaDescription;

	@Enumerated(EnumType.STRING)
	private TagTypeEnum type;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Statistic statistic;

	@Formula("(SELECT COUNT(e.id) FROM Exercise e JOIN exercise_tag et ON e.id = et.exercise_id WHERE et.tag_id = id)")
	private Long exerciseNo;
	
	@Formula("(SELECT COUNT(a.id) FROM Answer a JOIN Exercise e ON a.exercise_id = e.id JOIN exercise_tag et ON e.id = et.exercise_id WHERE et.tag_id = id AND a.status_code = 100)")
	private Long acceptedNo;
	
	@Formula("(SELECT COUNT(a.id) FROM Answer a JOIN Exercise e ON a.exercise_id = e.id JOIN exercise_tag et ON e.id = et.exercise_id WHERE et.tag_id = id)")
	private Long answerNo;
}