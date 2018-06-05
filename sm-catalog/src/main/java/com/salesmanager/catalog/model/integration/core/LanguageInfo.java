package com.salesmanager.catalog.model.integration.core;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LANGUAGE_INFO")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
@Getter
@Setter
public class LanguageInfo {

    @Id
    @Column(name="LANGUAGE_ID")
    private Integer id;

    @Column(name="CODE", nullable = false)
    private String code;

}
