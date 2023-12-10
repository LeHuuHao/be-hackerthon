package com.ptit.hackerthonservice.entity;



import com.ptit.hackerthonservice.service.AuditService;
import com.ptit.hackerthonservice.service.FileStoreService;
import com.ptit.hackerthonservice.utils.ActionEnum;
import com.ptit.hackerthonservice.utils.BeanUtil;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PreRemove;

public class EntityListener {

	@PostPersist
	public void postPersist(Object object) {
		AuditService auditService = BeanUtil.getBean(AuditService.class);
		if (object instanceof Exercise) {
			auditService.createExerciseRevision((Exercise) object);
			auditService.logUserActivity(ActionEnum.CREATE, Exercise.class.getSimpleName(), ((Exercise) object).getTitle());
		}
	}

	@PostUpdate
	public void postUpdate(Object object) {
		AuditService auditService = BeanUtil.getBean(AuditService.class);
		if (object instanceof Exercise) {
			Exercise exercise = (Exercise) object;
			auditService.createExerciseRevision(exercise);
			auditService.logUserActivity(ActionEnum.UPDATE, Exercise.class.getSimpleName(), exercise.getTitle());
		}
	}

	@PreRemove
	public void preRemove(Object object) {
		AuditService auditService = BeanUtil.getBean(AuditService.class);
		if (object instanceof Exercise) {
			Exercise exercise = (Exercise) object;
			auditService.deteleExerciseRevisions(exercise.getId());
			auditService.logUserActivity(ActionEnum.DELETE, Exercise.class.getSimpleName(), exercise.getTitle());
		}

		if (object instanceof Tag) {
			FileStoreService fileStoreService = BeanUtil.getBean(FileStoreService.class);
			fileStoreService.deleteFile(((Tag) object).getFeatureImage());
		}
	}
}
