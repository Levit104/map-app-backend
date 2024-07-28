package org.study.grabli_application.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "street_object_type", schema = "grabli_schema")
@Data
@EqualsAndHashCode(of = {"id"})
@ToString(exclude = {"streetObjects"})
public class StreetObjectType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "tag", nullable = false)
    private String tag;

    @OneToMany(mappedBy = "type")
    private List<StreetObject> streetObjects;
}
