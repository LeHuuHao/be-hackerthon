package com.ptit.hackerthonservice.entity;

import com.ptit.hackerthonservice.utils.StatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Exam extends CreateAuditable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	private String description;

	@Enumerated(EnumType.STRING)
	private StatusEnum status;

	@Column(unique = true)
	private String slug;

	private boolean endTime;

	private String metaDescription;
	
	private String featureImage;// image url
	
	private int orderNo;// STT

	
	@ManyToMany
	@JoinTable(name = "exam_tag", joinColumns = @JoinColumn(name = "exam_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private List<Tag> tags;

	private String endAt;

	private String startAt;

}