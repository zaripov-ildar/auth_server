package org.zaripov.istore.auth.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "authority")
public class Authority implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "title")
    @JsonBackReference
    private String title;

    @Override
    public String getAuthority() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Authority authority = (Authority) o;
        return getId() != null && Objects.equals(getId(), authority.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
