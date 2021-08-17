package com.jadenx.kxuserdetailsservice.service;

import com.jadenx.kxuserdetailsservice.model.SkillDTO;

import java.util.List;


public interface SkillService {

    List<SkillDTO> findAll();

    SkillDTO get(final Long id);

    Long create(final SkillDTO skillDTO);

    void update(final Long id, final SkillDTO skillDTO);

    void delete(final Long id);

}
