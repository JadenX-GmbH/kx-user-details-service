package com.jadenx.kxuserdetailsservice.rest;

import com.jadenx.kxuserdetailsservice.config.BaseIT;
import com.jadenx.kxuserdetailsservice.model.ErrorResponse;
import com.jadenx.kxuserdetailsservice.model.GigDTO;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GigControllerTest extends BaseIT {

    @Test
    @Sql("/data/gigData.sql")
    public void getAllGigs_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<List<GigDTO>> response = restTemplate.exchange(
            "/api/gigs", HttpMethod.GET, request, new ParameterizedTypeReference<List<GigDTO>>() {
            });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1300L, response.getBody().get(0).getId());
    }

    @Test
    @Sql("/data/gigData.sql")
    public void getGig_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<GigDTO> response = restTemplate.exchange(
            "/api/gigs/1300", HttpMethod.GET, request, GigDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(65L, response.getBody().getGigId());
    }

    @Test
    public void getGig_notFound() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/gigs/1966", HttpMethod.GET, request, ErrorResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("ResponseStatusException", response.getBody().getException());
    }

    @Test
    public void createGig_success() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/gigDTORequest.json"), headers());
        final ResponseEntity<Long> response = restTemplate.exchange(
            "/api/gigs", HttpMethod.POST, request, Long.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, gigRepository.count());
    }

    @Test
    public void createGig_missingField() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/gigDTORequest_missingField.json"), headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/gigs", HttpMethod.POST, request, ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("MethodArgumentNotValidException", response.getBody().getException());
        assertEquals("gigId", response.getBody().getFieldErrors().get(0).getField());
    }

    @Test
    @Sql("/data/gigData.sql")
    public void updateGig_success() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/gigDTORequest.json"), headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/gigs/1300", HttpMethod.PUT, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(66L, gigRepository.findById(1300L).get().getGigId());
    }

    @Test
    @Sql("/data/gigData.sql")
    public void deleteGig_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/gigs/1300", HttpMethod.DELETE, request, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(0, gigRepository.count());
    }

}
