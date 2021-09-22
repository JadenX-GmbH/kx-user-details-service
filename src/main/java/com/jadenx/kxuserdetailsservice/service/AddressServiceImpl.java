package com.jadenx.kxuserdetailsservice.service;

import com.jadenx.kxuserdetailsservice.domain.Address;
import com.jadenx.kxuserdetailsservice.domain.User;
import com.jadenx.kxuserdetailsservice.model.AddressDTO;
import com.jadenx.kxuserdetailsservice.repos.AddressRepository;
import com.jadenx.kxuserdetailsservice.repos.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressServiceImpl(final AddressRepository addressRepository,
                              final UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<AddressDTO> findAll() {
        return addressRepository.findAll()
            .stream()
            .map(address -> mapToDTO(address, new AddressDTO()))
            .collect(Collectors.toList());
    }

    @Override
    public AddressDTO get(final Long id) {
        return addressRepository.findById(id)
            .map(address -> mapToDTO(address, new AddressDTO()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Long create(final AddressDTO addressDTO) {
        final Address address = new Address();
        mapToEntity(addressDTO, address);
        return addressRepository.save(address).getId();
    }

    @Override
    public void update(final Long id, final AddressDTO addressDTO) {
        final Address address = addressRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(addressDTO, address);
        addressRepository.save(address);
    }

    @Override
    public void delete(final Long id) {
        addressRepository.deleteById(id);
    }

    private AddressDTO mapToDTO(final Address address, final AddressDTO addressDTO) {
        addressDTO.setId(address.getId());
        addressDTO.setName(address.getName());
        addressDTO.setSurname(address.getSurname());
        addressDTO.setAddress(address.getAddress());
        addressDTO.setSupplement(address.getSupplement());
        addressDTO.setPostalCode(address.getPostalCode());
        addressDTO.setCity(address.getCity());
        addressDTO.setCountry(address.getCountry());
        addressDTO.setUser(address.getUser() == null ? null : address.getUser().getUid());
        return addressDTO;
    }

    private Address mapToEntity(final AddressDTO addressDTO, final Address address) {
        address.setName(addressDTO.getName());
        address.setSurname(addressDTO.getSurname());
        address.setAddress(addressDTO.getAddress());
        address.setSupplement(addressDTO.getSupplement());
        address.setPostalCode(addressDTO.getPostalCode());
        address.setCity(addressDTO.getCity());
        address.setCountry(addressDTO.getCountry());
        if (addressDTO.getUser() != null
            && (address.getUser() == null
            || !address.getUser().getId().equals(addressDTO.getUser()))) {
            final User user = userRepository.findUserByUid(addressDTO.getUser())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
            address.setUser(user);
        }
        return address;
    }

}
