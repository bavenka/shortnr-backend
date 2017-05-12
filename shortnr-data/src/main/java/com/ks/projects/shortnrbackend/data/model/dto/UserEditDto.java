package com.ks.projects.shortnrbackend.data.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by kamaz on 09.05.2017.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserEditDto {

    @NotNull
    @NotEmpty
    private String username;

    private String password;
    @NotNull
    @NotEmpty
    @Email
    private String email;

}
