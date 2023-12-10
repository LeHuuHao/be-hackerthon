package com.ptit.hackerthonservice.service;

import com.ptit.hackerthonservice.dto.StatisticDTO;
import com.ptit.hackerthonservice.entity.Statistic;
import com.ptit.hackerthonservice.repository.AnswerRepo;
import com.ptit.hackerthonservice.repository.SolutionRepository;
import com.ptit.hackerthonservice.repository.StatisticRepository;
import com.ptit.hackerthonservice.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

public interface StatisticService {

	void updateView(long id);

	void updateLike(long id);

	StatisticDTO get(long id);

	StatisticDTO appStatistic();
}

@Service
class StatisticServiceImpl implements StatisticService {
	@Autowired
	StatisticRepository statisticRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	AnswerRepo answerRepository;

	@Autowired
	SolutionRepository solutionRepository;

	@Override
	@Transactional
	public void updateView(long id) {
		Statistic statistic = statisticRepository.findById(id).orElseThrow(NoResultException::new);
		statistic.setViewNo(statistic.getViewNo() + 1);
		statisticRepository.save(statistic);
	}

	@Override
	@Transactional
	public void updateLike(long id) {
		Statistic statistic = statisticRepository.findById(id).orElseThrow(NoResultException::new);
		statistic.setLikeNo(statistic.getLikeNo() + 1);
		statisticRepository.save(statistic);
	}

	@Override
	public StatisticDTO get(long id) {
		return statisticRepository.findById(id).map(this :: convert).orElseThrow(NoResultException::new);
	}

	private StatisticDTO convert(Statistic statistic) {
		ModelMapper mapper = new ModelMapper();
		return mapper.map(statistic, StatisticDTO.class);
	}

	@Override
	public StatisticDTO appStatistic() {
		StatisticDTO statistic = statisticRepository.statistic();

		statistic.setAnswerNo(answerRepository.count());
		statistic.setSolutionNo(solutionRepository.count());
		statistic.setUserNo(userRepository.count());

		return statistic;
	}

}