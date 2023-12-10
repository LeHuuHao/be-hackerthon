package com.ptit.hackerthonservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;

import java.util.Date;


@Data
public class BookmarkDTO {
    private Long id;

    private ExerciseDTO exercise;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;

    @JsonIncludeProperties({ "id", "username", "photoURL" })
    private UserDTO createdBy;
}
