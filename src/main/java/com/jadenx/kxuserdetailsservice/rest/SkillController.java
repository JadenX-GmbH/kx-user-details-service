package com.jadenx.kxuserdetailsservice.rest;

import com.jadenx.kxuserdetailsservice.model.SkillDTO;
import com.jadenx.kxuserdetailsservice.service.SkillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "/api/skills", produces = MediaType.APPLICATION_JSON_VALUE)
public class SkillController {

    private final SkillService skillService;

    public SkillController(final SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    public ResponseEntity<List<SkillDTO>> getAllSkills() {
        return ResponseEntity.ok(skillService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillDTO> getSkill(@PathVariable final Long id) {
        return ResponseEntity.ok(skillService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createSkill(@RequestBody @Valid final SkillDTO skillDTO) {
        return new ResponseEntity<>(skillService.create(skillDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSkill(@PathVariable final Long id,
                                            @RequestBody @Valid final SkillDTO skillDTO) {
        skillService.update(id, skillDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable final Long id) {
        skillService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
