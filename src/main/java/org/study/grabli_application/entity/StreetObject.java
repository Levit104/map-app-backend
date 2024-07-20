package org.study.grabli_application.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Formula;

@Entity
@Data
@Table(name = "project_object", schema = "grabli_schema")
@EqualsAndHashCode(of={"id"})
public class StreetObject {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "id_creater")
    private Long idCreator;

    @Column(name = "id_object")
    private Long idStreetObjectType;

    @Column(name = "commentary")
    private String comment;

    @Formula("ST_AsGeoJSON(coordinate)")
    private String coordinate;
}
