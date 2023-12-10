package com.ptit.hackerthonservice.repository;

import com.ptit.hackerthonservice.entity.Slider;
import com.ptit.hackerthonservice.utils.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SliderRepository extends JpaRepository<Slider,Integer> {
    @Query("SELECT s FROM Slider s WHERE s.title LIKE :title AND ( :status IS NULL OR s.status = :status )")
    Page<Slider> find(@Param("title") String value, @Param("status") StatusEnum status, Pageable pageable);
}
