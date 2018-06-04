package com.salesmanager.catalog.model.common.audit;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class AuditSection implements Serializable {


	private static final long serialVersionUID = -1934446958975060889L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_CREATED")
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_MODIFIED")
	private Date dateModified;

	@Column(name = "UPDT_ID", length=20)
	private String modifiedBy;
	
	public AuditSection() {
	}

	public Date getDateCreated() {
		return dateCreated != null ? (Date) dateCreated.clone() : null;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated != null ? (Date) dateCreated.clone() : null;
	}

	public Date getDateModified() {
		return dateModified != null ? (Date) dateModified.clone() : null;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified != null ? (Date) dateModified.clone() : null;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
}
