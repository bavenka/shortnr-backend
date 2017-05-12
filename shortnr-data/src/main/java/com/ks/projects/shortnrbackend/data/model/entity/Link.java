package com.ks.projects.shortnrbackend.data.model.entity;

import com.ks.projects.shortnrbackend.data.model.SuperClass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.util.*;

/**
 * Created by Pavel on 20.01.2017.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "links")
public class Link extends SuperClass {

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(length = 2000)
    private String url;
    private String token;
    private String description;
    private String tags;

    @OneToMany(mappedBy = "link", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LinkStatistic> linkStatistics = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
