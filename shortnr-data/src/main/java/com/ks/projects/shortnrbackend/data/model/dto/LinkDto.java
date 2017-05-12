package com.ks.projects.shortnrbackend.data.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ks.projects.shortnrbackend.data.model.entity.LinkStatistic;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Pavel on 21.01.2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class LinkDto {

    private long id;
    @NotNull
    @NotEmpty
    private String url;
    @NotEmpty
    @NotNull
    private String token;
    @NotNull
    @NotEmpty
    private Date creationDate;
    private String description;
    private Set<String> tags;

    private List<LinkStatisticDto> linkStatistics;
}
