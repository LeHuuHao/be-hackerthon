package com.ptit.hackerthonservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
public class StatisticDTO {
	private Long id;

	private Long viewNo;

	private Long likeNo;

	private Long answerNo;

	private Long solutionNo;

	private Long userNo;

	public StatisticDTO(Long viewNo, Long likeNo) {
		super();
		this.viewNo = viewNo;
		this.likeNo = likeNo;
	}

}
