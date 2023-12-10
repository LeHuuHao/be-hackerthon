package com.ptit.hackerthonservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.ptit.hackerthonservice.entity.Statistic;
import com.ptit.hackerthonservice.utils.StatusEnum;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.List;
@Data
public class ForumPostDTO {
    private Long id;

    private String title;

    private String description;

    private String slug;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    private List<TagDTO> tags;

    private Statistic statistic;

    private Integer version;

    private Long commentNo;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
    private Date updatedAt;

    @JsonIncludeProperties({ "id", "displayName", "photoURL" })
    private UserDTO createdBy;

    private UserDTO updatedBy;
}
