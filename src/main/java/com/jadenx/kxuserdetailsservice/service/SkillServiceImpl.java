package com.jadenx.kxuserdetailsservice.service;

import com.jadenx.kxuserdetailsservice.domain.Category;
import com.jadenx.kxuserdetailsservice.domain.Skill;
import com.jadenx.kxuserdetailsservice.model.SkillDTO;
import com.jadenx.kxuserdetailsservice.repos.CategoryRepository;
import com.jadenx.kxuserdetailsservice.repos.SkillRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final CategoryRepository categoryRepository;

    public SkillServiceImpl(final SkillRepository skillRepository,
                            final CategoryRepository categoryRepository) {
        this.skillRepository = skillRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<SkillDTO> findAll() {
        return skillRepository.findAll()
            .stream()
            .map(skill -> mapToDTO(skill, new SkillDTO()))
            .collect(Collectors.toList());
    }

    @Override
    public SkillDTO get(final Long id) {
        return skillRepository.findById(id)
            .map(skill -> mapToDTO(skill, new SkillDTO()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Long create(final SkillDTO skillDTO) {
        final Skill skill = new Skill();
        mapToEntity(skillDTO, skill);
        return skillRepository.save(skill).getId();
    }

    @Override
    public void update(final Long id, final SkillDTO skillDTO) {
        final Skill skill = skillRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(skillDTO, skill);
        skillRepository.save(skill);
    }

    @Override
    public void delete(final Long id) {
        skillRepository.deleteById(id);
    }

    private SkillDTO mapToDTO(final Skill skill, final SkillDTO skillDTO) {
        skillDTO.setId(skill.getId());
        skillDTO.setName(skill.getName());
        skillDTO.setCategory(skill.getCategory() == null ? null : skill.getCategory().getId());
        return skillDTO;
    }

    private Skill mapToEntity(final SkillDTO skillDTO, final Skill skill) {
        skill.setName(skillDTO.getName());
        if (skillDTO.getCategory() != null && (skill.getCategory() == null
            || !skill.getCategory().getId().equals(skillDTO.getCategory()))) {
            final Category category = categoryRepository.findById(skillDTO.getCategory())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "category not found"));
            skill.setCategory(category);
        }
        return skill;
    }

}
