package com.ks.projects.shortnrbackend.data.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Created by Pavel on 21.01.2017.
 */
@Getter
@Setter
@NoArgsConstructor
public class EditingLinkDto {

    private Long id;
    private String url;
    private String description;
    private Set<String> tags;
    private String token;
}
