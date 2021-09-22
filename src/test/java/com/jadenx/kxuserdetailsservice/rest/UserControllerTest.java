package com.jadenx.kxuserdetailsservice.rest;

import com.jadenx.kxuserdetailsservice.config.BaseIT;
import com.jadenx.kxuserdetailsservice.model.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class UserControllerTest extends BaseIT {

    // CHECKSTYLE IGNORE check FOR NEXT 1 LINES
    private static final String AUTH_TOKEN = "eyJraWQiOiJvUVA3Zlp4NXN2SHRZVVptS0ZPeUw2WXVIUkFWdVZCclZ0XC80QkdaVHVhTT0iLCJhbGciOiJSUzI1NiJ9.eyJhdF9oYXNoIjoiUzdzQkNBY28wd25sRzdBVkl5YmI3QSIsInN1YiI6IjhiOWVlNjBjLTYxMDItNDYwMS04NTMwLTA0MWMwMWY0YTZlOSIsImF1ZCI6IjY0bDFrMzJrajc5OXIyczJ0NGM5c21hdmE1IiwiZW1haWxfdmVyaWZpZWQiOnRydWUsInRva2VuX3VzZSI6ImlkIiwiYXV0aF90aW1lIjoxNjI3ODk2MzM3LCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAuZXUtY2VudHJhbC0xLmFtYXpvbmF3cy5jb21cL2V1LWNlbnRyYWwtMV9mZDVENG9kbkEiLCJjb2duaXRvOnVzZXJuYW1lIjoiOGI5ZWU2MGMtNjEwMi00NjAxLTg1MzAtMDQxYzAxZjRhNmU5IiwiZXhwIjoxNjI3OTgyNzM3LCJpYXQiOjE2Mjc4OTYzMzcsImp0aSI6IjkwNmU0YjQ0LWZiNTctNDU1ZC1iNzgzLTEzOTI1MzY2MDVmZiIsImVtYWlsIjoiamFjZWsuamFuY3p1cmFAamFkZW54LmNvbSJ9.f3PhccH8P6fevh0U9sgzFlSTAeig1gDPps99e5y4RQF07dVEsNDh2JA5bNEHVtzrGmUycNsYMiE8qzQGyn-V38gbi7eTyTlfaQRjr8-GcghFL1j43DtNUW-Hxy84qjHrC_eFJ1mdfC_BZ19zmB2bU8OQjm6SQ3IT-gIHOKivtHFWq7unweRXz1YpZHZeQqzRGWO_k5M1eM8umLA-xWD2JaZgL4Mm4IYxOEXa_n_x7LN2p2J8JpIWiuXjBd44ANXGdeloVQJAsxQDfsevljH7aPbMyaL7E0UGRnIb7CJISDUPDlheR0ye0mNcisy4YnT-jPe9yw65DuEXqi4h2WHZxg";

    @Test
    @Sql("/data/userData.sql")
    public void getAllUsers_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<List<UserDTO>> response = restTemplate.exchange(
            "/api/users", HttpMethod.GET, request, new ParameterizedTypeReference<List<UserDTO>>() {
            });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1000L, response.getBody().get(0).getId());
    }

    @Test
    @Sql("/data/userData.sql")
    public void getUser_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<ProfileDTO> response = restTemplate.exchange(
            "/api/users/8b9ee60c-6102-4601-8530-041c01f4a6e9", HttpMethod.GET, request, ProfileDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(UUID.fromString("8b9ee60c-6102-4601-8530-041c01f4a6e9"), response.getBody().getUid());
    }

    @Test
    @Sql({"/data/userData.sql", "/data/detailsData.sql",
        "/data/addressData.sql", "/data/categoryData.sql",
        "/data/skillData.sql", "/data/skillsetData.sql"})
    public void getProfile_success() {
        var header = headers();
        header.setBearerAuth(AUTH_TOKEN);
        final HttpEntity<String> request = new HttpEntity<>(null, header);

        final ResponseEntity<ProfileDTO> response = restTemplate.exchange(
            "/api/users/profile", HttpMethod.GET, request, ProfileDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(UUID.fromString("8b9ee60c-6102-4601-8530-041c01f4a6e9"), response.getBody().getUid());
    }

    @Test
    public void getUser_notFound() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/users/8b9ee60c-6102-4601-8530-041c01f4a6ec", HttpMethod.GET, request, ErrorResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("ResponseStatusException", response.getBody().getException());
    }

    @Test
    public void createUser_success() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/userDTORequest.json"), headers());
        final ResponseEntity<Long> response = restTemplate.exchange(
            "/api/users", HttpMethod.POST, request, Long.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, userRepository.count());
    }

    @Test
    public void createUser_missingField() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/userDTORequest_missingField.json"), headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/users", HttpMethod.POST, request, ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("MethodArgumentNotValidException", response.getBody().getException());
        assertEquals("type", response.getBody().getFieldErrors().get(0).getField());
    }

    @Test
    @Sql("/data/userData.sql")
    public void updateUser_success() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/userDTORequest.json"), headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/users/1000", HttpMethod.PUT, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(UUID.fromString("fab26046-bf5a-38b1-a17c-cd7c42fefb62"),
            userRepository.findById(1000L).get().getUid());
    }

    @Test
    @Sql({"/data/userData.sql", "/data/detailsData.sql",
        "/data/addressData.sql", "/data/categoryData.sql",
        "/data/skillData.sql", "/data/skillsetData.sql"})
    public void deleteUser_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/users/1000", HttpMethod.DELETE, request, Void.class);

        assertAll(
            () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()),
            () -> assertEquals(0, userRepository.count()),
            () -> assertEquals(0, detailsRepository.count()),
            () -> assertEquals(0, addressRepository.count()),
            () -> assertEquals(0, skillsetRepository.count()),
            () -> assertEquals(1, skillRepository.count()),
            () -> assertEquals(1, categoryRepository.count())
        );


    }

    @Test
    @Sql({"/data/userData.sql", "/data/detailsData.sql",
        "/data/addressData.sql", "/data/categoryData.sql",
        "/data/skillData.sql", "/data/skillsetData.sql"})
    public void getProfileFromUid_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<ProfileDTO> response = restTemplate.exchange(
            "/api/users/8b9ee60c-6102-4601-8530-041c01f4a6e9/profile",
            HttpMethod.GET, request, ProfileDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(UUID.fromString("8b9ee60c-6102-4601-8530-041c01f4a6e9"), response.getBody().getUid());
    }

    @Test
    @Sql({"/data/userData.sql", "/data/detailsData.sql"})
    public void getDetailsFromUser() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<DetailsDTO> response = restTemplate.exchange(
            "/api/users/8b9ee60c-6102-4601-8530-041c01f4a6e9/detail",
            HttpMethod.GET, request, DetailsDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1600, response.getBody().getId());
    }

    @Test
    @Sql({"/data/userData.sql", "/data/detailsData.sql"})
    public void updateDetail() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/detailsDTORequest.json"), headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/users/8b9ee60c-6102-4601-8530-041c01f4a6e9/detail", HttpMethod.PUT, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Donec ac nibh...", detailsRepository.findById((long) 1600).get().getName());
    }

    @Test
    @Sql({"/data/userData.sql", "/data/detailsData.sql", "/data/addressData.sql"})
    public void getAddressFromUser() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<List<AddressDTO>> response = restTemplate.exchange(
            "/api/users/8b9ee60c-6102-4601-8530-041c01f4a6e9/addresses", HttpMethod.GET,
            request, new ParameterizedTypeReference<>() {
            });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1500, response.getBody().get(0).getId());
    }

    @Test
    @Sql({"/data/userData.sql", "/data/detailsData.sql", "/data/addressData.sql"})
    public void updateAddress() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/addressDTOListUpdateRequest.json"), headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/users/8b9ee60c-6102-4601-8530-041c01f4a6e9/addresses", HttpMethod.PUT, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Donec ac nibh...", addressRepository.findById(1500L).get().getName());
    }

    @Test
    @Sql({"/data/userData.sql", "/data/detailsData.sql", "/data/addressData.sql"})
    public void deleteAddress() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/users/8b9ee60c-6102-4601-8530-041c01f4a6e9/addresses", HttpMethod.DELETE, request, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(0, addressRepository.count());
        assertEquals(1, userRepository.count());
        assertEquals(1, detailsRepository.count());
    }

    @Test
    @Sql({"/data/userData.sql", "/data/detailsData.sql"})
    public void createAddress() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/addressDTOListRequest.json"), headers());
        final ResponseEntity<List<Long>> response = restTemplate.exchange(
            "/api/users/8b9ee60c-6102-4601-8530-041c01f4a6e9/addresses", HttpMethod.POST, request,
            new ParameterizedTypeReference<>() {
            });

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, addressRepository.count());
    }
}
