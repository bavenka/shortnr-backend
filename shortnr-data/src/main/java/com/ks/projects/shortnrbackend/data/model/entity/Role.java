package com.ks.projects.shortnrbackend.data.model.entity;

import com.ks.projects.shortnrbackend.data.model.SuperClass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Pavel on 25.12.2016.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends SuperClass implements GrantedAuthority {

    @Column(unique = true, nullable = false)
    private String authority;
}
