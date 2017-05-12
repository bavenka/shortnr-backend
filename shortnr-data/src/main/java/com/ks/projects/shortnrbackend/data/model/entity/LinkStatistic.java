package com.ks.projects.shortnrbackend.data.model.entity;

import com.ks.projects.shortnrbackend.data.model.SuperClass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Pavel on 5/2/2017.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "links_statistic")
public class LinkStatistic extends SuperClass {

    @Temporal(TemporalType.TIMESTAMP)
    private Date clickDate;

    private String country;

    @ManyToOne
    @JoinColumn(name = "link_id")
    private Link link;

}
