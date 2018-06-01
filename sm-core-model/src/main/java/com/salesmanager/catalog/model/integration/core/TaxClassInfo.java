package com.salesmanager.catalog.model.integration.core;

import com.salesmanager.core.constants.SchemaConstant;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

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

    @ManyToOne
    @JoinColumn(name="MERCHANT_ID", nullable=true)
    private MerchantStoreInfo merchantStore;

}
