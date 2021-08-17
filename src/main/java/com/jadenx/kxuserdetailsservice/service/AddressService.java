package com.jadenx.kxuserdetailsservice.service;

import com.jadenx.kxuserdetailsservice.model.AddressDTO;

import java.util.List;


public interface AddressService {

    List<AddressDTO> findAll();

    AddressDTO get(final Long id);

    Long create(final AddressDTO addressDTO);

    void update(final Long id, final AddressDTO addressDTO);

    void delete(final Long id);

}
