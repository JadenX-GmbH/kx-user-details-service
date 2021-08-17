package com.jadenx.kxuserdetailsservice.rest;

import com.jadenx.kxuserdetailsservice.config.BaseIT;
import com.jadenx.kxuserdetailsservice.model.CategoryDTO;
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


public class CategoryControllerTest extends BaseIT {

    @Test
    @Sql("/data/categoryData.sql")
    public void getAllCategorys_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<List<CategoryDTO>> response = restTemplate.exchange(
            "/api/categorys", HttpMethod.GET, request, new ParameterizedTypeReference<List<CategoryDTO>>() {
            });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1400L, response.getBody().get(0).getId());
    }

    @Test
    @Sql("/data/categoryData.sql")
    public void getCategory_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<CategoryDTO> response = restTemplate.exchange(
            "/api/categorys/1400", HttpMethod.GET, request, CategoryDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cras sed interdum...", response.getBody().getName());
    }

    @Test
    public void getCategory_notFound() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/categorys/2066", HttpMethod.GET, request, ErrorResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("ResponseStatusException", response.getBody().getException());
    }

    @Test
    public void createCategory_success() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/categoryDTORequest.json"), headers());
        final ResponseEntity<Long> response = restTemplate.exchange(
            "/api/categorys", HttpMethod.POST, request, Long.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, categoryRepository.count());
    }

    @Test
    public void createCategory_missingField() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/categoryDTORequest_missingField.json"), headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/categorys", HttpMethod.POST, request, ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("MethodArgumentNotValidException", response.getBody().getException());
        assertEquals("name", response.getBody().getFieldErrors().get(0).getField());
    }

    @Test
    @Sql("/data/categoryData.sql")
    public void updateCategory_success() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/categoryDTORequest.json"), headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/categorys/1400", HttpMethod.PUT, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Donec ac nibh...", categoryRepository.findById(1400L).get().getName());
    }

    @Test
    @Sql({"/data/categoryData.sql", "/data/skillData.sql"})
    public void deleteCategory_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/categorys/1400", HttpMethod.DELETE, request, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(0, categoryRepository.count());
        assertEquals(0, skillRepository.count());
    }

}
