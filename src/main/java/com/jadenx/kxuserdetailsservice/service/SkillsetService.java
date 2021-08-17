package com.jadenx.kxuserdetailsservice.service;

import com.jadenx.kxuserdetailsservice.model.SkillsetDTO;

import java.util.List;


public interface SkillsetService {

    List<SkillsetDTO> findAll();

    SkillsetDTO get(final Long id);

    Long create(final SkillsetDTO skillsetDTO);

    void update(final Long id, final SkillsetDTO skillsetDTO);

    void delete(final Long id);

}
