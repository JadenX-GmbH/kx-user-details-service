package com.jadenx.kxuserdetailsservice.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ExtendedSkillsetDTO {
    private Long id;

    private Maturity maturity;

    private String skill;
    private String category;
}
