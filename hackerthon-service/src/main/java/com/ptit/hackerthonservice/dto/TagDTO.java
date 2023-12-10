package com.ptit.hackerthonservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.ptit.hackerthonservice.utils.TagTypeEnum;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Data
public class TagDTO {
	private Integer id;

	private String title;

	private String description;

	private int orderNo;// STT

	private String featureImage;// image url

	private String slug;

	private String metaDescription;

	@Enumerated(EnumType.STRING)
	private TagTypeEnum type;

	private Long exerciseNo;
	
	private Long acceptedNo;
	
	private Long answerNo;

	@JsonIncludeProperties({ "id" })
	private StatisticDTO statistic;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
	private Date createdAt;

	@JsonIgnore
	private MultipartFile file;

}