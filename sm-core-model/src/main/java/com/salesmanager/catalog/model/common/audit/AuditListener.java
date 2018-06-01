package com.salesmanager.catalog.model.common.audit;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

public class AuditListener {
	
	@PrePersist
	@PreUpdate
	public void onSaveOrUpdate(Object o) {
		if (o instanceof Auditable) {
			Auditable audit = (Auditable) o;
			AuditSection auditSection = audit.getAuditSection();
			
			auditSection.setDateModified(new Date());
			if (auditSection.getDateCreated() == null) {
				auditSection.setDateCreated(new Date());
			}
			audit.setAuditSection(auditSection);
		}
	}
}
