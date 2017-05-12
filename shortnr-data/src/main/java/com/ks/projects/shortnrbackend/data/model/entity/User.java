package com.ks.projects.shortnrbackend.data.model.entity;

import com.ks.projects.shortnrbackend.data.model.SuperClass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Pavel on 24.12.2016.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends SuperClass implements UserDetails {
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> authorities = new HashSet<>();
    @Column(unique = true, nullable = false)
    private String username;
    @Column
    private String password;
    @Column
    private String email;
    @Column
    private boolean accountNonLocked = true;
    @Column
    private boolean accountNonExpired = true;
    @Column
    private boolean credentialsNonExpired = true;
    @Column
    private boolean enabled = true;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Link> links = new HashSet<>();

}
