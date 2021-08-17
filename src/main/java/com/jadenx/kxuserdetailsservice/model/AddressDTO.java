package com.jadenx.kxuserdetailsservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
public class AddressDTO {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Size(max = 150)
    private String surname;

    @NotNull
    @Size(max = 150)
    private String address;

    @NotNull
    @Size(max = 100)
    private String supplement;

    @NotNull
    @Size(max = 15)
    private String postalCode;

    @Size(max = 100)
    private String city;

    @NotNull
    @Size(max = 50)
    private String country;

    @NotNull
    private Long user;

}
