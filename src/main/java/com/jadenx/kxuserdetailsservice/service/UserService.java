package com.jadenx.kxuserdetailsservice.service;

import com.jadenx.kxuserdetailsservice.model.ProfileDTO;
import com.jadenx.kxuserdetailsservice.model.UserDTO;

import java.util.List;
import java.util.UUID;


public interface UserService {

    List<UserDTO> findAll();

    UserDTO get(final Long id);

    ProfileDTO getProfileById(final UUID id);

    Long create(final UserDTO userDTO);

    void update(final Long id, final UserDTO userDTO);

    void delete(final Long id);

}
