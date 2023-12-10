package com.ptit.hackerthonservice.entity;

import com.ptit.hackerthonservice.utils.StatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "forum_post")
@EqualsAndHashCode(callSuper = true)
public class ForumPost extends CreateAuditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(insertable = false)
	private Long id;

	@Column(unique = true)
	private String title;

	private String description;

	@Column(unique = true)
	private String slug;

	@Enumerated(EnumType.STRING)
	private StatusEnum status;

	@ManyToMany
	@JoinTable(name = "forum_post_tag", joinColumns = @JoinColumn(name = "forum_post_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private List<Tag> tags;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Statistic statistic;

	@Version
	private Integer version;

	@Formula("(SELECT COUNT(c.id) FROM comment c JOIN forum_post_comment pc ON pc.comment_id=c.id JOIN forum_post p ON p.id= pc.forum_post_id WHERE p.id = id )")
	private Long commentNo;

	@OneToMany(mappedBy = "forumPost", cascade = CascadeType.REMOVE)
	private List<ForumPostComment> comments;

}
