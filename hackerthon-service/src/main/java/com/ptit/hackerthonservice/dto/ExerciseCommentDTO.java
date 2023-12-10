package com.ptit.hackerthonservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;

import java.util.Date;

@Data
public class ExerciseCommentDTO {
    private Long commentId;

     @JsonIgnoreProperties({"replies"})
    private CommentDTO comment;

    @JsonIncludeProperties({ "id", "title" })
    private ExerciseDTO exercise;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;

    @JsonIncludeProperties({ "id", "displayName", "photoURL" })
    private UserDTO createdBy;
}
