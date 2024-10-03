package org.study.grabli_application.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import org.locationtech.jts.geom.Point;

import java.util.Objects;

@Entity
@Table(name = "street_object", schema = "grabli_schema")
@Getter
@Setter
@ToString
public class StreetObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id", nullable = false)
    private StreetObjectType type;

    @Column(columnDefinition = "geometry(point, 4326)", name = "location", nullable = false)
    private Point location;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "image", nullable = false, unique = true)
    private String image;

    @Column(name = "creator_name", nullable = false)
    private String creatorName;

    @Column(name = "creator_contact", nullable = false)
    private String creatorContact;

    @Column(name = "approved")
    private Boolean approved;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o)
                .getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this)
                .getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        StreetObject that = (StreetObject) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this)
                .getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
