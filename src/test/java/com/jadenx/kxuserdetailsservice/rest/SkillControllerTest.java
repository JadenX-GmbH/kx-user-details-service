package com.jadenx.kxuserdetailsservice.rest;

import com.jadenx.kxuserdetailsservice.config.BaseIT;
import com.jadenx.kxuserdetailsservice.model.ErrorResponse;
import com.jadenx.kxuserdetailsservice.model.SkillDTO;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SkillControllerTest extends BaseIT {

    @Test
    @Sql({"/data/categoryData.sql", "/data/skillData.sql"})
    public void getAllSkills_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<List<SkillDTO>> response = restTemplate.exchange(
            "/api/skills", HttpMethod.GET, request, new ParameterizedTypeReference<List<SkillDTO>>() {
            });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1200L, response.getBody().get(0).getId());
    }

    @Test
    @Sql({"/data/categoryData.sql", "/data/skillData.sql"})
    public void getSkill_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<SkillDTO> response = restTemplate.exchange(
            "/api/skills/1200", HttpMethod.GET, request, SkillDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cras sed interdum...", response.getBody().getName());
    }

    @Test
    public void getSkill_notFound() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/skills/1866", HttpMethod.GET, request, ErrorResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("ResponseStatusException", response.getBody().getException());
    }

    @Test
    @Sql("/data/categoryData.sql")
    public void createSkill_success() {
        final HttpEntity<String> request = new HttpEntity<>(readResource("/requests/skillDTORequest.json"), headers());
        final ResponseEntity<Long> response = restTemplate.exchange(
            "/api/skills", HttpMethod.POST, request, Long.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, skillRepository.count());
    }

    @Test
    public void createSkill_missingField() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/skillDTORequest_missingField.json"), headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/skills", HttpMethod.POST, request, ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("MethodArgumentNotValidException", response.getBody().getException());
        assertEquals("name", response.getBody().getFieldErrors().get(0).getField());
    }

    @Test
    @Sql({"/data/categoryData.sql", "/data/skillData.sql"})
    public void updateSkill_success() {
        final HttpEntity<String> request = new HttpEntity<>(readResource("/requests/skillDTORequest.json"), headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/skills/1200", HttpMethod.PUT, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Donec ac nibh...", skillRepository.findById((long) 1200).get().getName());
    }

    @Test
    @Sql({
        "/data/userData.sql",
        "/data/detailsData.sql",
        "/data/categoryData.sql",
        "/data/skillData.sql",
        "/data/skillsetData.sql"})
    public void deleteSkill_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/skills/1200", HttpMethod.DELETE, request, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(0, skillRepository.count());
        assertEquals(0, skillsetRepository.count());
        assertEquals(1, categoryRepository.count());
        assertEquals(1, userRepository.count());
        assertEquals(1, detailsRepository.count());
    }

}
