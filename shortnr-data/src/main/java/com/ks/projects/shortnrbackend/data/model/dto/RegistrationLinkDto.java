package com.ks.projects.shortnrbackend.data.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by Pavel on 20.01.2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class RegistrationLinkDto {

    @NotNull
    @NotEmpty
    private String url;
    //TODO TOKEN IS NOT NESSESARY ANYMORE
    private String token;
    private String description;
    private Set<String> tags;
}
