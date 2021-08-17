package com.jadenx.kxuserdetailsservice.repos;

import com.jadenx.kxuserdetailsservice.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SkillRepository extends JpaRepository<Skill, Long> {
}
