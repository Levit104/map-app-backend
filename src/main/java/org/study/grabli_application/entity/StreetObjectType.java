package org.study.grabli_application.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "object", schema = "grabli_schema")
@Data
@EqualsAndHashCode(of = {"id"})
public class StreetObjectType {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "object_name")
    private String ObjectName;

    @Column(name = "tag")
    private String tag;
}
