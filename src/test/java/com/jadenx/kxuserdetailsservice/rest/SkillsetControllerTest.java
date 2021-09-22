package com.jadenx.kxuserdetailsservice.rest;

import com.jadenx.kxuserdetailsservice.config.BaseIT;
import com.jadenx.kxuserdetailsservice.model.ErrorResponse;
import com.jadenx.kxuserdetailsservice.model.SkillsetDTO;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class SkillsetControllerTest extends BaseIT {

    @Test
    @Sql({"/data/userData.sql",
        "/data/detailsData.sql",
        "/data/categoryData.sql",
        "/data/skillData.sql",
        "/data/skillsetData.sql"})
    public void getAllSkillsets_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<List<SkillsetDTO>> response = restTemplate.exchange(
            "/api/skillsets", HttpMethod.GET, request, new ParameterizedTypeReference<List<SkillsetDTO>>() {
            });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1100L, response.getBody().get(0).getId());
    }

    @Test
    @Sql({"/data/userData.sql",
        "/data/detailsData.sql",
        "/data/categoryData.sql",
        "/data/skillData.sql",
        "/data/skillsetData.sql"})
    public void getSkillset_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<SkillsetDTO> response = restTemplate.exchange(
            "/api/skillsets/1100", HttpMethod.GET, request, SkillsetDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getSkillset_notFound() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/skillsets/1766", HttpMethod.GET, request, ErrorResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("ResponseStatusException", response.getBody().getException());
    }

    @Test
    @Sql({"/data/categoryData.sql", "/data/skillData.sql"})
    public void createSkillset_success() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/skillsetDTORequest.json"), headers());
        final ResponseEntity<List<Long>> response = restTemplate.exchange(
            "/api/skillsets", HttpMethod.POST, request, new ParameterizedTypeReference<List<Long>>() {
            });

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(2, skillsetRepository.count());
    }

    @Test
    @Sql({"/data/userData.sql",
        "/data/detailsData.sql",
        "/data/categoryData.sql",
        "/data/skillData.sql",
        "/data/skillsetData.sql"})
    public void updateSkillset_success() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/skillsetDTOUpdateRequest.json"), headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/skillsets/1100", HttpMethod.PUT, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Sql({
        "/data/userData.sql",
        "/data/detailsData.sql",
        "/data/categoryData.sql",
        "/data/skillData.sql",
        "/data/skillsetData.sql"})
    public void deleteSkillset_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/skillsets/1100", HttpMethod.DELETE, request, Void.class);

        assertAll(
            ()-> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()),
            ()-> assertEquals(0, skillsetRepository.count()),
            ()-> assertEquals(1, skillRepository.count()),
            ()-> assertEquals(1, categoryRepository.count()),
            ()-> assertEquals(1, userRepository.count()),
            ()-> assertEquals(1,detailsRepository.count())

        );



    }

}
