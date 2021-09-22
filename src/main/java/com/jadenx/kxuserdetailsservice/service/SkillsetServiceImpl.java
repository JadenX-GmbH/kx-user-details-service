package com.jadenx.kxuserdetailsservice.service;

import com.jadenx.kxuserdetailsservice.domain.Skill;
import com.jadenx.kxuserdetailsservice.domain.Skillset;
import com.jadenx.kxuserdetailsservice.domain.User;
import com.jadenx.kxuserdetailsservice.model.SkillsetDTO;
import com.jadenx.kxuserdetailsservice.repos.SkillRepository;
import com.jadenx.kxuserdetailsservice.repos.SkillsetRepository;
import com.jadenx.kxuserdetailsservice.repos.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SkillsetServiceImpl implements SkillsetService {

    private final SkillsetRepository skillsetRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    public SkillsetServiceImpl(final SkillsetRepository skillsetRepository,
                               final UserRepository userRepository, final SkillRepository skillRepository) {
        this.skillsetRepository = skillsetRepository;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
    }

    @Override
    public List<SkillsetDTO> findAll() {
        return skillsetRepository.findAll()
            .stream()
            .map(skillset -> mapToDTO(skillset, new SkillsetDTO()))
            .collect(Collectors.toList());
    }

    @Override
    public SkillsetDTO get(final Long id) {
        return skillsetRepository.findById(id)
            .map(skillset -> mapToDTO(skillset, new SkillsetDTO()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Long> create(final List<SkillsetDTO> skillsetDTOList) {
        List<Skillset> skillsetList = skillsetDTOList.stream()
            .map(skillsetDTO -> mapToEntity(skillsetDTO, new Skillset()))
            .collect(Collectors.toList());
        return skillsetRepository.saveAll(skillsetList)
            .stream().map(skillset -> skillset.getId())
            .collect(Collectors.toList());
    }

    @Override
    public void update(final Long id, final SkillsetDTO skillsetDTO) {
        final Skillset skillset = skillsetRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(skillsetDTO, skillset);
        skillsetRepository.save(skillset);
    }

    @Override
    public void delete(final Long id) {
        skillsetRepository.deleteById(id);
    }

    private SkillsetDTO mapToDTO(final Skillset skillset, final SkillsetDTO skillsetDTO) {
        skillsetDTO.setId(skillset.getId());
        skillsetDTO.setMaturity(skillset.getMaturity());
        skillsetDTO.setUser(skillset.getUser() == null ? null : skillset.getUser().getId());
        skillsetDTO.setSkill(skillset.getSkill() == null ? null : skillset.getSkill().getId());
        return skillsetDTO;
    }

    private Skillset mapToEntity(final SkillsetDTO skillsetDTO, final Skillset skillset) {
        skillset.setMaturity(skillsetDTO.getMaturity());
        if (skillsetDTO.getUser() != null && (skillset.getUser() == null
            || !skillset.getUser().getId().equals(skillsetDTO.getUser()))) {
            final User user = userRepository.findById(skillsetDTO.getUser())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
            skillset.setUser(user);
        }
        if (skillsetDTO.getSkill() != null && (skillset.getSkill() == null
            || !skillset.getSkill().getId().equals(skillsetDTO.getSkill()))) {
            final Skill skill = skillRepository.findById(skillsetDTO.getSkill())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "skill not found"));
            skillset.setSkill(skill);
        }
        return skillset;
    }

}
