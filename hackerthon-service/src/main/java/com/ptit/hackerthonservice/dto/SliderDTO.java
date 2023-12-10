package com.ptit.hackerthonservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ptit.hackerthonservice.utils.StatusEnum;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class SliderDTO {
    private Integer id;

    private String title;

    private String featureImage;

    private String targetLink;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;


    @JsonIgnore
    private MultipartFile file;
}
