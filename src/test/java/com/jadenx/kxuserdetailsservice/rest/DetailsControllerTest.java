package com.jadenx.kxuserdetailsservice.rest;

import com.jadenx.kxuserdetailsservice.config.BaseIT;
import com.jadenx.kxuserdetailsservice.model.DetailsDTO;
import com.jadenx.kxuserdetailsservice.model.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class DetailsControllerTest extends BaseIT {

    @Test
    @Sql({"/data/userData.sql", "/data/detailsData.sql"})
    public void getAllDetailss_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<List<DetailsDTO>> response = restTemplate.exchange(
            "/api/details", HttpMethod.GET, request, new ParameterizedTypeReference<List<DetailsDTO>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1600L, response.getBody().get(0).getId());
    }

    @Test
    @Sql({"/data/userData.sql", "/data/detailsData.sql"})
    public void getDetails_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<DetailsDTO> response = restTemplate.exchange(
            "/api/details/1600", HttpMethod.GET, request, DetailsDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cras sed interdum...", response.getBody().getName());
    }

    @Test
    public void getDetails_notFound() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/details/2266", HttpMethod.GET, request, ErrorResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("ResponseStatusException", response.getBody().getException());
    }

    @Test
    @Sql("/data/userData.sql")
    public void createDetails_success() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/detailsDTORequest.json"), headers());
        final ResponseEntity<Long> response = restTemplate.exchange(
            "/api/details", HttpMethod.POST, request, Long.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, detailsRepository.count());
    }

    @Test
    public void createDetails_missingField() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/detailsDTORequest_missingField.json"), headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/details", HttpMethod.POST, request, ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("MethodArgumentNotValidException", response.getBody().getException());
        assertEquals("email", response.getBody().getFieldErrors().get(0).getField());
    }

    @Test
    @Sql({"/data/userData.sql", "/data/detailsData.sql"})
    public void updateDetails_success() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/detailsDTORequest.json"), headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/details/1600", HttpMethod.PUT, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Donec ac nibh...", detailsRepository.findById((long) 1600).get().getName());
    }

    @Test
    @Sql({"/data/userData.sql", "/data/detailsData.sql"})
    public void deleteDetails_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/details/1600", HttpMethod.DELETE, request, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(0, detailsRepository.count());
        assertEquals(1, userRepository.count());
    }
}
