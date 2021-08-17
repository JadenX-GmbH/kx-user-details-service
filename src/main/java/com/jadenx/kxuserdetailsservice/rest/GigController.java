package com.jadenx.kxuserdetailsservice.rest;

import com.jadenx.kxuserdetailsservice.model.GigDTO;
import com.jadenx.kxuserdetailsservice.service.GigService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "/api/gigs", produces = MediaType.APPLICATION_JSON_VALUE)
public class GigController {

    private final GigService gigService;

    public GigController(final GigService gigService) {
        this.gigService = gigService;
    }

    @GetMapping
    public ResponseEntity<List<GigDTO>> getAllGigs() {
        return ResponseEntity.ok(gigService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GigDTO> getGig(@PathVariable final Long id) {
        return ResponseEntity.ok(gigService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createGig(@RequestBody @Valid final GigDTO gigDTO) {
        return new ResponseEntity<>(gigService.create(gigDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateGig(@PathVariable final Long id,
                                          @RequestBody @Valid final GigDTO gigDTO) {
        gigService.update(id, gigDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGig(@PathVariable final Long id) {
        gigService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
