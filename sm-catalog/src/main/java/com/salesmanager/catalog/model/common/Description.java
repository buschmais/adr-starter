package com.salesmanager.catalog.model.common;

import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.common.audit.AuditListener;
import com.salesmanager.catalog.model.common.audit.AuditSection;
import com.salesmanager.catalog.model.common.audit.Auditable;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@EntityListeners(value = AuditListener.class)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Description implements Auditable, Serializable {
	private static final long serialVersionUID = -4335863941736710046L;
	
	@Id
	@Column(name = "DESCRIPTION_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "DESCRIPTION_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Embedded
	private AuditSection auditSection = new AuditSection();
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "LANGUAGE_ID")
	private LanguageInfo language;
	
	@NotEmpty
	@Column(name="NAME", nullable = false, length=120)
	private String name;
	
	@Column(name="TITLE", length=100)
	private String title;
	
	@Column(name="DESCRIPTION")
	@Type(type = "org.hibernate.type.StringClobType")
	private String description;
	
	public Description() {
	}
	
	public Description(LanguageInfo language, String name) {
		this.setLanguage(language);
		this.setName(name);
	}
	
	@Override
	public AuditSection getAuditSection() {
		return auditSection;
	}

	@Override
	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}

	public LanguageInfo getLanguage() {
		return language;
	}

	public void setLanguage(LanguageInfo language) {
		this.language = language;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
