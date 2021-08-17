package com.jadenx.kxuserdetailsservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
public class ProfileDTO {

    private Long id;

    private UUID uid;

    private String identifier;

    private Type type;

    private String publicAddress;

    private String tagLine;

    private String description;

    private String userPhoto;

    private String backgroundPhoto;

    private Boolean isActive;

    private Set<ExtendedSkillsetDTO> userSkillsets;

    private Set<GigDTO> userGigs;

    private DetailsDTO details;

    private Set<AddressDTO> userAddresses;

    private OffsetDateTime dateCreated;

    private OffsetDateTime lastUpdated;

}
