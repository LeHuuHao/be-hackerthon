package com.ptit.hackerthonservice.repository;

import com.ptit.hackerthonservice.dto.StatisticDTO;
import com.ptit.hackerthonservice.entity.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {

	@Query("SELECT new com.ptit.hackerthonservice.dto.StatisticDTO(SUM(s.viewNo) , SUM(s.likeNo)) FROM Statistic s")
	StatisticDTO statistic();
}
