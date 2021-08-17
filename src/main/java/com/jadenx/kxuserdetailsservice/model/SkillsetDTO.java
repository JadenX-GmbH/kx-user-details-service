package com.jadenx.kxuserdetailsservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Getter
@Setter
public class SkillsetDTO {

    private Long id;

    private Maturity maturity;

    @NotNull
    private Long user;

    @NotNull
    private Long skill;

}
