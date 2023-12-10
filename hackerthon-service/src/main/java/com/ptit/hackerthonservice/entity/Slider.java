package com.ptit.hackerthonservice.entity;

import com.ptit.hackerthonservice.utils.StatusEnum;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Slider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String featureImage;

    private String targetLink;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;
}
