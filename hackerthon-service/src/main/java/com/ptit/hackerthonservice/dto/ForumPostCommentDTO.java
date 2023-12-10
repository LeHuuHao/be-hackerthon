package com.ptit.hackerthonservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;

import java.util.Date;
@Data
public class ForumPostCommentDTO {
    private Long commentId;

    @JsonIgnoreProperties({"replies"})
    private CommentDTO comment;

    @JsonIncludeProperties({ "id", "title" })
    private ForumPostDTO forumPost;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;

    @JsonIncludeProperties({ "id", "displayName", "photoURL" })
    private UserDTO createdBy;
}


