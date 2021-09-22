package com.jadenx.kxuserdetailsservice.service;

import com.jadenx.kxuserdetailsservice.model.AddressDTO;
import com.jadenx.kxuserdetailsservice.model.DetailsDTO;
import com.jadenx.kxuserdetailsservice.model.ProfileDTO;
import com.jadenx.kxuserdetailsservice.model.UserDTO;

import java.util.List;
import java.util.UUID;


public interface UserService {

    List<UserDTO> findAll();

    ProfileDTO get(final UUID id);

    ProfileDTO getProfileByUid(final UUID id);

    Long create(final UserDTO userDTO);

    void update(final Long id, final UserDTO userDTO);

    void delete(final Long id);

    DetailsDTO getDetailsFromUser(final UUID uid);

    void updateDetail(final UUID uid, final DetailsDTO detailsDTO);

    List<AddressDTO> getAddressFromUser(final UUID uid);

    void updateAddress(final UUID uid, final List<AddressDTO> addressDTO);

    void deleteAddress(final UUID uid);

    List<Long> createAddress(final UUID uid, final List<AddressDTO> addressDTOList);

}
