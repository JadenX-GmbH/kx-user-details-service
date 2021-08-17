package com.jadenx.kxuserdetailsservice.rest;

import com.jadenx.kxuserdetailsservice.config.BaseIT;
import com.jadenx.kxuserdetailsservice.model.AddressDTO;
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


public class AddressControllerTest extends BaseIT {

    @Test
    @Sql({"/data/userData.sql", "/data/detailsData.sql", "/data/addressData.sql"})
    public void getAllAddresss_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<List<AddressDTO>> response = restTemplate.exchange(
            "/api/address", HttpMethod.GET, request, new ParameterizedTypeReference<List<AddressDTO>>() {
            });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1500, response.getBody().get(0).getId());
    }

    @Test
    @Sql({"/data/userData.sql", "/data/detailsData.sql", "/data/addressData.sql"})
    public void getAddress_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<AddressDTO> response = restTemplate.exchange(
            "/api/address/1500", HttpMethod.GET, request, AddressDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cras sed interdum...", response.getBody().getName());
    }

    @Test
    public void getAddress_notFound() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/address/2166", HttpMethod.GET, request, ErrorResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("ResponseStatusException", response.getBody().getException());
    }

    @Test
    @Sql({"/data/userData.sql", "/data/detailsData.sql"})
    public void createAddress_success() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/addressDTORequest.json"), headers());
        final ResponseEntity<Long> response = restTemplate.exchange(
            "/api/address", HttpMethod.POST, request, Long.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, addressRepository.count());
    }

    @Test
    public void createAddress_missingField() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/addressDTORequest_missingField.json"), headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/address", HttpMethod.POST, request, ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("MethodArgumentNotValidException", response.getBody().getException());
        assertEquals("name", response.getBody().getFieldErrors().get(0).getField());
    }

    @Test
    @Sql({"/data/userData.sql", "/data/detailsData.sql", "/data/addressData.sql"})
    public void updateAddress_success() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/addressDTORequest.json"), headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/address/1500", HttpMethod.PUT, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Donec ac nibh...", addressRepository.findById(1500L).get().getName());
    }

    @Test
    @Sql({"/data/userData.sql", "/data/detailsData.sql", "/data/addressData.sql"})
    public void deleteAddress_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/address/1500", HttpMethod.DELETE, request, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(0, addressRepository.count());
        assertEquals(1, userRepository.count());
        assertEquals(1, detailsRepository.count());
    }

}
