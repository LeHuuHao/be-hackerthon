package com.ptit.hackerthonservice.dto;

import lombok.Data;

@Data
public class CountDifficultyDTO {
		private long easy;
	    private long medium;
	    private long hard;
	    private long extreme;
    
	    public CountDifficultyDTO(long easy, long medium, long hard, long extreme) {
	        this.easy = easy;
	        this.medium = medium;
	        this.hard = hard;
	        this.extreme = extreme;
	    }
}
