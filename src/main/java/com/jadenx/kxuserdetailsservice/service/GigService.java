package com.jadenx.kxuserdetailsservice.service;

import com.jadenx.kxuserdetailsservice.model.GigDTO;

import java.util.List;


public interface GigService {

    List<GigDTO> findAll();

    GigDTO get(final Long id);

    Long create(final GigDTO gigDTO);

    void update(final Long id, final GigDTO gigDTO);

    void delete(final Long id);

}
