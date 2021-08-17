package com.jadenx.kxuserdetailsservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;


@Entity
@Getter
@Setter
public class User {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, columnDefinition = "char(36)")
    @Type(type = "uuid-char")
    private UUID uid;

    @Column(unique = true, length = 150)
    private String identifier;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private com.jadenx.kxuserdetailsservice.model.Type type;

    @Column(length = 100)
    private String publicAddress;

    @Column
    private String tagLine;

    @Column(name = "\"description\"", columnDefinition = "longtext")
    private String description;

    @Column
    private String userPhoto;

    @Column
    private String backgroundPhoto;

    @Column
    private Boolean isActive = Boolean.TRUE;

    @OneToMany(mappedBy = "user",
        fetch = FetchType.EAGER,
        cascade = CascadeType.ALL)
    private Set<Skillset> userSkillsets;

    // CHECKSTYLE IGNORE check FOR NEXT 6 LINES
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "user_gig",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "gig_id")
    )
    private Set<Gig> userGigGigs;

    // CHECKSTYLE IGNORE check FOR NEXT 7 LINES
    @OneToOne(
        mappedBy = "user",
        fetch = FetchType.LAZY,
        optional = false,
        orphanRemoval = true
    )
    private Details details;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Address> userAddresss;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    @PrePersist
    public void prePersist() {
        dateCreated = OffsetDateTime.now();
        lastUpdated = dateCreated;
    }

    @PreUpdate
    public void preUpdate() {
        lastUpdated = OffsetDateTime.now();
    }
}
