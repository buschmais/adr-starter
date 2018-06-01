package com.salesmanager.catalog.model.integration.core;

import com.salesmanager.core.constants.SchemaConstant;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TAX_CLASS_INFO", schema = SchemaConstant.SALESMANAGER_SCHEMA)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
@Setter
@Getter
public class TaxClassInfo {

    @Id
    @Column(name = "TAX_CLASS_ID", unique=true, nullable=false)
    private Long id;

    @NotEmpty
    @Column(name="TAX_CLASS_CODE", nullable=false, length=10)
    private String code;

}
