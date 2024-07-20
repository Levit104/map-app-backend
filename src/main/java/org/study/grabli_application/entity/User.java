package org.study.grabli_application.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

import org.study.grabli_application.security.DefaultRole;

@Entity
@Data
@EqualsAndHashCode(of = "id")
@Table(name = "users", schema = "grabli_schema")
public class User implements UserDetails {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "user_id_generator", schema = "grabli_schema",
            sequenceName = "users_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_generator")
    private Long id;

    @Column(name = "login")
    private String username;

    @Column(name = "passd")
    private String passd;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return Collections.singletonList(DefaultRole.DEFAULT);
    }

    @Override
    public String getPassword() {
        return passd;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}