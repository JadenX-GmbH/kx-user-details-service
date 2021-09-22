package com.jadenx.kxuserdetailsservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;


@Getter
@Setter
public class UserDTO {

    private Long id;

    private UUID uid;

    @Size(max = 150)
    private String identifier;

    @NotNull
    private Type type;

    @Size(max = 100)
    private String publicAddress;

    @Size(max = 255)
    private String tagLine;

    private String description;

    @Size(max = 255)
    private String userPhoto;

    @Size(max = 255)
    private String backgroundPhoto;

    private Boolean isActive = Boolean.TRUE;

}
