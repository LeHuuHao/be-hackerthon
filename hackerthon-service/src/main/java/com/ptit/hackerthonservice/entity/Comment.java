package com.ptit.hackerthonservice.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Comment extends CreateAuditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(insertable = false)
	private Long id;

	private String description;

//	@ManyToMany
//	@JoinTable(name = "comment_liker", joinColumns = @JoinColumn(name = "comment_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
//	private List<User> likers;

	private int likeNo;

	@OneToMany(mappedBy = "replyTo", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> replies;

	@ManyToOne
	private Comment replyTo;

	private int replyNo;

	@OneToOne(mappedBy = "comment", cascade = CascadeType.ALL)
	private ExerciseComment exerciseComment;

	@OneToOne(mappedBy = "comment", cascade = CascadeType.ALL)
	private SolutionComment solutionComment;

	@OneToOne(mappedBy = "comment", cascade = CascadeType.ALL)
	private ForumPostComment forumPostComment;

	
}
