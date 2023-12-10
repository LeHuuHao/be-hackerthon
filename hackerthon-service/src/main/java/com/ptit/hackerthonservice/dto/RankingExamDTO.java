package com.ptit.hackerthonservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;
import org.hibernate.annotations.Formula;

import java.util.Date;

@Data
public class RankingExamDTO {

    private Long id;

    private Long score;

    private ExamDTO exam;

    private Long answerNo;

    private Long acceptedNo;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;

    @JsonIncludeProperties({ "id", "username", "photoURL" })
    private UserDTO createdBy;
}
