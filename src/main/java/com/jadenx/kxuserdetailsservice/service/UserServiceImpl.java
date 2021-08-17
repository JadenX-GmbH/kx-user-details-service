package com.jadenx.kxuserdetailsservice.service;

import com.jadenx.kxuserdetailsservice.domain.*;
import com.jadenx.kxuserdetailsservice.model.*;
import com.jadenx.kxuserdetailsservice.repos.GigRepository;
import com.jadenx.kxuserdetailsservice.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final GigRepository gigRepository;

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll()
            .stream()
            .map(user -> mapToDTO(user, new UserDTO()))
            .collect(Collectors.toList());
    }

    @Override
    public UserDTO get(final Long id) {
        return userRepository.findById(id)
            .map(user -> mapToDTO(user, new UserDTO()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public ProfileDTO getProfileById(final UUID uid) {
        return userRepository.findUserByUid(uid)
            .map(user -> mapToProfile(user, new ProfileDTO()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Long create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    @Override
    public void update(final Long id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    @Override
    public void delete(final Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setUid(user.getUid());
        userDTO.setIdentifier(user.getIdentifier());
        userDTO.setType(user.getType());
        userDTO.setPublicAddress(user.getPublicAddress());
        userDTO.setTagLine(user.getTagLine());
        userDTO.setDescription(user.getDescription());
        userDTO.setUserPhoto(user.getUserPhoto());
        userDTO.setBackgroundPhoto(user.getBackgroundPhoto());
        userDTO.setIsActive(user.getIsActive());
        userDTO.setUserGigs(user.getUserGigGigs() == null ? null : user.getUserGigGigs().stream()
            .map(Gig::getId)
            .collect(Collectors.toList()));
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setUid(userDTO.getUid());
        user.setIdentifier(userDTO.getIdentifier());
        user.setType(userDTO.getType());
        user.setPublicAddress(userDTO.getPublicAddress());
        user.setTagLine(userDTO.getTagLine());
        user.setDescription(userDTO.getDescription());
        user.setUserPhoto(userDTO.getUserPhoto());
        user.setBackgroundPhoto(userDTO.getBackgroundPhoto());
        user.setIsActive(userDTO.getIsActive());
        if (userDTO.getUserGigs() != null) {
            final List<Gig> userGigs = gigRepository.findAllById(userDTO.getUserGigs());
            if (userGigs.size() != userDTO.getUserGigs().size()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "one of userGigs not found");
            }
            user.setUserGigGigs(new HashSet<>(userGigs));
        }
        return user;
    }

    private ProfileDTO mapToProfile(final User user, final ProfileDTO profileDTO) {
        profileDTO.setId(user.getId());
        profileDTO.setUid(user.getUid());
        profileDTO.setIdentifier(user.getIdentifier());
        profileDTO.setType(user.getType());
        profileDTO.setPublicAddress(user.getPublicAddress());
        profileDTO.setTagLine(user.getTagLine());
        profileDTO.setDescription(user.getDescription());
        profileDTO.setUserPhoto(user.getUserPhoto());
        profileDTO.setBackgroundPhoto(user.getBackgroundPhoto());
        profileDTO.setIsActive(user.getIsActive());
        profileDTO.setUserSkillsets(
            user.getUserSkillsets() == null ? null
                : user.getUserSkillsets()
                .stream()
                .map(skillset -> mapSkillsetToDTO(skillset, new ExtendedSkillsetDTO()))
                .collect(Collectors.toSet())
        );
        profileDTO.setUserGigs(user.getUserGigGigs() == null ? null
            : user.getUserGigGigs()
            .stream()
            .map(gig -> mapGigToDTO(gig, new GigDTO()))
            .collect(Collectors.toSet()));
        profileDTO.setDetails(user.getDetails() == null ? null : mapDetailsToDTO(user.getDetails(), new DetailsDTO()));

        profileDTO.setUserAddresses(user.getUserAddresss() == null ? null
            : user.getUserAddresss().stream().map(address -> mapAddressToDTO(address, new AddressDTO()))
            .collect(Collectors.toSet()));
        profileDTO.setDateCreated(user.getDateCreated());
        profileDTO.setLastUpdated(user.getLastUpdated());
        return profileDTO;
    }


    private GigDTO mapGigToDTO(final Gig gig, final GigDTO gigDTO) {
        gigDTO.setId(gig.getId());
        gigDTO.setGigId(gig.getGigId());
        return gigDTO;
    }

    private ExtendedSkillsetDTO mapSkillsetToDTO(
        final Skillset skillset, final ExtendedSkillsetDTO extendedSkillsetDTO) {
        extendedSkillsetDTO.setSkill(skillset.getSkill().getName());
        extendedSkillsetDTO.setCategory(skillset.getSkill().getCategory().getName());
        extendedSkillsetDTO.setMaturity(skillset.getMaturity());
        extendedSkillsetDTO.setId(skillset.getId());
        return extendedSkillsetDTO;
    }

    private DetailsDTO mapDetailsToDTO(final Details details, final DetailsDTO detailsDTO) {
        detailsDTO.setId(details.getId());
        detailsDTO.setName(details.getName());
        detailsDTO.setSurname(details.getSurname());
        detailsDTO.setEmail(details.getEmail());
        detailsDTO.setPhone(details.getPhone());
        detailsDTO.setNationality(details.getNationality());
        detailsDTO.setEducation(details.getEducation());
        detailsDTO.setDegree(details.getDegree());
        detailsDTO.setUser(details.getUser() == null ? null : details.getUser().getId());
        return detailsDTO;
    }

    private AddressDTO mapAddressToDTO(final Address address, final AddressDTO addressDTO) {
        addressDTO.setId(address.getId());
        addressDTO.setName(address.getName());
        addressDTO.setSurname(address.getSurname());
        addressDTO.setAddress(address.getAddress());
        addressDTO.setSupplement(address.getSupplement());
        addressDTO.setPostalCode(address.getPostalCode());
        addressDTO.setCity(address.getCity());
        addressDTO.setCountry(address.getCountry());
        addressDTO.setUser(address.getUser() == null ? null : address.getUser().getId());
        return addressDTO;
    }
}

