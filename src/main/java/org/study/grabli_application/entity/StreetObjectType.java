package org.study.grabli_application.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Table(name = "object", schema = "grabli_schema")
@EqualsAndHashCode(of={"id"})
public class StreetObjectType {

  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "object_name")
  private String ObjectName;

  @Column(name = "tag")
  private String tag;
}
