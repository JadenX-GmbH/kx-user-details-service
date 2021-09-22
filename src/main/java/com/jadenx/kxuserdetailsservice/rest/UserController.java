package com.jadenx.kxuserdetailsservice.rest;

import com.jadenx.kxuserdetailsservice.model.AddressDTO;
import com.jadenx.kxuserdetailsservice.model.DetailsDTO;
import com.jadenx.kxuserdetailsservice.model.ProfileDTO;
import com.jadenx.kxuserdetailsservice.model.UserDTO;
import com.jadenx.kxuserdetailsservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO> getUsersProfile(final Principal user) {
        return ResponseEntity.ok(userService.getProfileByUid(UUID.fromString(user.getName())));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{uid}")
    public ResponseEntity<ProfileDTO> getUser(@PathVariable final UUID uid) {
        return ResponseEntity.ok(userService.get(uid));
    }

    @PostMapping
    public ResponseEntity<Long> createUser(@RequestBody @Valid final UserDTO userDTO) {
        return new ResponseEntity<>(userService.create(userDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable final Long id,
                                           @RequestBody @Valid final UserDTO userDTO) {
        userService.update(id, userDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable final Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{uid}/profile")
    public ResponseEntity<ProfileDTO> getUsersProfileFromUid(@PathVariable final UUID uid) {
        return ResponseEntity.ok(userService.getProfileByUid(uid));
    }

    @GetMapping("/{uid}/detail")
    public ResponseEntity<DetailsDTO> getDetailsFromUser(@PathVariable final UUID uid) {
        return ResponseEntity.ok(userService.getDetailsFromUser(uid));
    }

    @PutMapping("/{uid}/detail")
    public ResponseEntity<Void> updateDetails(@PathVariable final UUID uid,
                                              @RequestBody @Valid final DetailsDTO detailsDTO) {
        userService.updateDetail(uid, detailsDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{uid}/addresses")
    public ResponseEntity<List<AddressDTO>> getAddressFromUser(@PathVariable final UUID uid) {
        return  ResponseEntity.ok(userService.getAddressFromUser(uid));
    }

    @PutMapping("/{uid}/addresses")
    public ResponseEntity<Void> updateAddress(@PathVariable final UUID uid,
                                              @RequestBody @Valid final List<AddressDTO> addressDTO) {
        userService.updateAddress(uid, addressDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{uid}/addresses")
    public ResponseEntity<Void> deleteAddress(@PathVariable final UUID uid) {
        userService.deleteAddress(uid);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{uid}/addresses")
    public ResponseEntity<List<Long>> createAddresses(@PathVariable final UUID uid,
                                                      @RequestBody @Valid final List<AddressDTO> addressDTOList) {
        return new ResponseEntity<>(userService.createAddress(uid, addressDTOList), HttpStatus.CREATED);
    }

}
