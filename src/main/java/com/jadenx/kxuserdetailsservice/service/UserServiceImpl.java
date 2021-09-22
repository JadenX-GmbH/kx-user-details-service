package com.jadenx.kxuserdetailsservice.service;

import com.jadenx.kxuserdetailsservice.domain.*;
import com.jadenx.kxuserdetailsservice.model.*;
import com.jadenx.kxuserdetailsservice.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DetailsService detailsService;
    private final AddressService addressService;

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll()
            .stream()
            .map(user -> mapToDTO(user, new UserDTO()))
            .collect(Collectors.toList());
    }

    @Override
    public ProfileDTO get(final UUID id) {
        return userRepository.findUserByUid(id)
            .map(user -> mapToProfile(user, new ProfileDTO()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public ProfileDTO getProfileByUid(final UUID uid) {
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

    @Override
    public DetailsDTO getDetailsFromUser(final UUID uid) {
        return userRepository.findUserByUid(uid)
            .map(user -> mapDetailsToDTO(user.getDetails(), new DetailsDTO()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public void updateDetail(final UUID uid, final DetailsDTO detailsDTO) {
        User user = userRepository.findUserByUid(uid)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Details detail = user.getDetails();
        detailsService.update(detail.getId(), detailsDTO);
    }

    @Override
    public List<AddressDTO> getAddressFromUser(final UUID uid) {
        User user = userRepository.findUserByUid(uid)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return user.getUserAddresss().stream()
            .map(address -> mapAddressToDTO(address, new AddressDTO()))
            .collect(Collectors.toList());
    }

    @Override
    public void updateAddress(final UUID uid, final List<AddressDTO> addressDTOList) {
        userRepository.findUserByUid(uid)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        addressDTOList
            .forEach(addressDTO -> addressService.update(addressDTO.getId(), addressDTO));

    }

    @Override
    public void deleteAddress(final UUID uid) {
        User user = userRepository.findUserByUid(uid)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        user.getUserAddresss().clear();
        userRepository.save(user);
    }

    @Override
    public List<Long> createAddress(final UUID uid, final List<AddressDTO> addressDTOList) {
        userRepository.findUserByUid(uid)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return addressDTOList
            .stream().map(addressDTO -> {
                addressDTO.setUser(uid);
                return addressService.create(addressDTO);
            }).collect(Collectors.toList());
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
        profileDTO.setDetails(user.getDetails() == null ? null : mapDetailsToDTO(user.getDetails(), new DetailsDTO()));

        profileDTO.setUserAddresses(user.getUserAddresss() == null ? null
            : user.getUserAddresss().stream().map(address -> mapAddressToDTO(address, new AddressDTO()))
            .collect(Collectors.toSet()));
        profileDTO.setDateCreated(user.getDateCreated());
        profileDTO.setLastUpdated(user.getLastUpdated());
        return profileDTO;
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
        detailsDTO.setUser(details.getUser() == null ? null : details.getUser().getUid());
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
        addressDTO.setUser(address.getUser() == null ? null : address.getUser().getUid());
        return addressDTO;
    }
}

