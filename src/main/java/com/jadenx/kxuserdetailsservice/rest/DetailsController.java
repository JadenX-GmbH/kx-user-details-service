package com.jadenx.kxuserdetailsservice.rest;

import com.jadenx.kxuserdetailsservice.model.DetailsDTO;
import com.jadenx.kxuserdetailsservice.service.DetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "/api/details", produces = MediaType.APPLICATION_JSON_VALUE)
public class DetailsController {

    private final DetailsService detailsService;

    public DetailsController(final DetailsService detailsService) {
        this.detailsService = detailsService;
    }

    @GetMapping
    public ResponseEntity<List<DetailsDTO>> getAllDetailss() {
        return ResponseEntity.ok(detailsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailsDTO> getDetails(@PathVariable final Long id) {
        return ResponseEntity.ok(detailsService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createDetails(@RequestBody @Valid final DetailsDTO detailsDTO) {
        return new ResponseEntity<>(detailsService.create(detailsDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDetails(@PathVariable final Long id,
                                              @RequestBody @Valid final DetailsDTO detailsDTO) {
        detailsService.update(id, detailsDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDetails(@PathVariable final Long id) {
        detailsService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
