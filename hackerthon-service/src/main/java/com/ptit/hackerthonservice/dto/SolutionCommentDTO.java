package com.ptit.hackerthonservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;

import java.util.Date;

@Data
public class SolutionCommentDTO {
    private Long commentId;

    private CommentDTO comment;

    private SolutionDTO solution;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;

    @JsonIncludeProperties({ "id", "displayName", "photoURL" })
    private UserDTO createdBy;
}
