package com.ptit.hackerthonservice.repository;


import com.ptit.hackerthonservice.dto.CountLevelDTO;
import com.ptit.hackerthonservice.dto.StatisticUserDTO;
import com.ptit.hackerthonservice.entity.User;
import com.ptit.hackerthonservice.utils.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
	@Query("SELECT DISTINCT t FROM User t JOIN t.role r WHERE (t.username LIKE :name OR t.email LIKE :name OR t.phone LIKE :name) AND ( :role is null OR r.name = :role )")
	Page<User> find(@Param("name") String value, @Param("role") String role, Pageable pageable);

	@Query("SELECT new com.ptit.hackerthonservice.dto.StatisticUserDTO(u.username,u.photoURL,SUM(a1.score) as score,COUNT(a1.id) as submission, COUNT(  a1.id) as ac) \n" +
			"FROM Answer a1 JOIN a1.createdBy u JOIN a1.exercise ex1\n" +
			"WHERE a1.id IN ( SELECT la.id FROM Exercise ex JOIN Answer la ON ex.id = la.exercise.id WHERE la.statusCode = 100 AND la.createdBy.id = u.id \n" +
			"GROUP BY ex.id HAVING MAX(la.createdAt) IS NOT NULL) \n" +
			"AND ex1.status=:statusEnum AND (u.username LIKE :name OR u.email LIKE :name OR u.phone LIKE :name)  AND ( :startAt IS NULL OR a1.createdAt >= :startAt) AND ( :endAt IS NULL OR a1.createdAt <= :endAt) GROUP BY u.id")
	Page<StatisticUserDTO> statistic(@Param("name") String value, @Param("statusEnum") StatusEnum statusEnum, @Param("startAt") Date startAt, @Param("endAt") Date endAt, Pageable pageable);

	@Query("SELECT new com.ptit.hackerthonservice.dto.StatisticUserDTO(u.username, u.photoURL, SUM(a.score) AS score, COUNT(a.id) AS submissionCOUNT,\n" +
			"SUM(CASE WHEN a.statusCode = 100 THEN 1 ELSE 0 END) AS AC)\n" +
			"FROM com.ptit.hackerthonservice.entity.Answer a\n" +
			"JOIN a.createdBy u WHERE a.createdAt >= :startAt AND a.createdAt <= :endAt AND YEAR(a.createdAt) = YEAR(NOW())\n" +
			"GROUP BY u.id"
	)
	List<StatisticUserDTO> statisticTop(@Param("startAt") Date startAt, @Param("endAt") Date endAt);

	@Query("SELECT new com.ptit.hackerthonservice.dto.StatisticUserDTO(u.username, u.photoURL, SUM(a.score) AS score, COUNT(a.id) AS submissionCOUNT,\n" +
			"SUM(CASE WHEN a.statusCode = 100 THEN 1 ELSE 0 END) AS AC)\n" +
			"FROM com.ptit.hackerthonservice.entity.Answer a\n" +
			"JOIN a.createdBy u WHERE MONTH(a.createdAt) = MONTH(NOW()) AND YEAR(a.createdAt) = YEAR(NOW())\n" +
			"GROUP BY u.id"
	)
	List<StatisticUserDTO> statisticMonth();


	@Query("SELECT new com.ptit.hackerthonservice.dto.CountLevelDTO(COUNT(DISTINCT CASE WHEN t.title = 'Dễ' THEN e.id ELSE NULL END) AS easyCount,\n" +
			"       COUNT(DISTINCT CASE WHEN t.title = 'Trung Bình' THEN e.id ELSE NULL END) AS mediumCount,\n" +
			"       COUNT(DISTINCT CASE WHEN t.title = 'Khó' THEN e.id ELSE NULL END) AS hardCount ,\n"+
			"       COUNT(DISTINCT CASE WHEN t.title = 'Cực Khó' THEN e.id ELSE NULL END) AS tryhardCount) FROM Answer\n"+
			"a JOIN a.createdBy u JOIN a.exercise e JOIN e.tags t\n" +
			"WHERE u.id=:uid and a.statusCode = 100 ")
	CountLevelDTO countLevel(@Param("uid") Integer uid);

	@Query("SELECT u FROM User u WHERE u.username LIKE :name OR u.phone LIKE :name OR u.name LIKE :name")
	Page<User> find(@Param("name") String value, Pageable pageable);

	Optional<User> findByUsername(String username);
}
