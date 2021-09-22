package com.jadenx.kxuserdetailsservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;


@Getter
@Setter
public class DetailsDTO {

    private Long id;

    @Size(max = 100)
    private String name;

    @Size(max = 150)
    private String surname;

    @NotNull
    @Size(max = 150)
    private String email;

    @Size(max = 50)
    private String phone;

    @Size(max = 100)
    private String nationality;

    @Size(max = 255)
    private String education;

    @Size(max = 255)
    private String degree;

    @NotNull
    private UUID user;
}
