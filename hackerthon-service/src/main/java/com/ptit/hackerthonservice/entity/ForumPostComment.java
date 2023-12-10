package com.ptit.hackerthonservice.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class ForumPostComment extends CreateAuditable{

    @Id
    private Long commentId;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @MapsId
    private Comment comment;

    @ManyToOne
    private ForumPost forumPost;

}
