package com.ptit.hackerthonservice.service;


import com.ptit.hackerthonservice.entity.Exercise;
import com.ptit.hackerthonservice.entity.ExerciseRevision;
import com.ptit.hackerthonservice.entity.UserActivity;
import com.ptit.hackerthonservice.repository.ExerciseRevisionRepository;
import com.ptit.hackerthonservice.repository.UserActivityRepository;
import com.ptit.hackerthonservice.utils.ActionEnum;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {

	@Autowired
	UserActivityRepository activityRepository;

	@Autowired
	ExerciseRevisionRepository exerciseRevisionRepository;

	@Transactional
	public void createExerciseRevision(Exercise exercise) {
		ExerciseRevision exerciseRevision = new ModelMapper().map(exercise, ExerciseRevision.class);
		exerciseRevisionRepository.save(exerciseRevision);
	}

	@Transactional
	public void deteleExerciseRevisions(long exerciseId) {
		exerciseRevisionRepository.deleteByExerciseId(exerciseId);
	}

	@Transactional
	public void logUserActivity(ActionEnum action, String type, String description) {
		UserActivity userActivity = new UserActivity();
		userActivity.setAction(action);
		userActivity.setType(type);
		userActivity.setDescription(description);

		activityRepository.save(userActivity);
	}
}
