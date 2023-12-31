package com.ptit.hackerthonservice.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class UpdateAuditable extends CreateAuditable {

	@LastModifiedBy
	@JoinColumn(name = "updated_by")
	@ManyToOne(fetch = FetchType.EAGER)
	private User updatedBy;

}
