package com.jadenx.kxuserdetailsservice.service;

import com.jadenx.kxuserdetailsservice.model.DetailsDTO;

import java.util.List;


public interface DetailsService {

    List<DetailsDTO> findAll();

    DetailsDTO get(final Long id);

    Long create(final DetailsDTO detailsDTO);

    void update(final Long id, final DetailsDTO detailsDTO);

    void delete(final Long id);

}
