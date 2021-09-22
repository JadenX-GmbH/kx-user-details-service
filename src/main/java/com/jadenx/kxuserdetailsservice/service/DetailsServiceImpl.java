package com.jadenx.kxuserdetailsservice.service;

import com.jadenx.kxuserdetailsservice.domain.Details;
import com.jadenx.kxuserdetailsservice.domain.User;
import com.jadenx.kxuserdetailsservice.model.DetailsDTO;
import com.jadenx.kxuserdetailsservice.repos.DetailsRepository;
import com.jadenx.kxuserdetailsservice.repos.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class DetailsServiceImpl implements DetailsService {

    private final DetailsRepository detailsRepository;
    private final UserRepository userRepository;

    public DetailsServiceImpl(final DetailsRepository detailsRepository,
                              final UserRepository userRepository) {
        this.detailsRepository = detailsRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<DetailsDTO> findAll() {
        return detailsRepository.findAll()
            .stream()
            .map(details -> mapToDTO(details, new DetailsDTO()))
            .collect(Collectors.toList());
    }

    @Override
    public DetailsDTO get(final Long id) {
        return detailsRepository.findById(id)
            .map(details -> mapToDTO(details, new DetailsDTO()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Long create(final DetailsDTO detailsDTO) {
        final Details details = new Details();
        mapToEntity(detailsDTO, details);
        return detailsRepository.save(details).getId();
    }

    @Override
    public void update(final Long id, final DetailsDTO detailsDTO) {
        final Details details = detailsRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(detailsDTO, details);
        detailsRepository.save(details);
    }

    @Override
    public void delete(final Long id) {
        detailsRepository.deleteById(id);
    }

    private DetailsDTO mapToDTO(final Details details, final DetailsDTO detailsDTO) {
        detailsDTO.setId(details.getId());
        detailsDTO.setName(details.getName());
        detailsDTO.setSurname(details.getSurname());
        detailsDTO.setEmail(details.getEmail());
        detailsDTO.setPhone(details.getPhone());
        detailsDTO.setNationality(details.getNationality());
        detailsDTO.setEducation(details.getEducation());
        detailsDTO.setDegree(details.getDegree());
        detailsDTO.setUser(details.getUser() == null ? null : details.getUser().getUid());
        return detailsDTO;
    }

    private Details mapToEntity(final DetailsDTO detailsDTO, final Details details) {
        details.setName(detailsDTO.getName());
        details.setSurname(detailsDTO.getSurname());
        details.setEmail(detailsDTO.getEmail());
        details.setPhone(detailsDTO.getPhone());
        details.setNationality(detailsDTO.getNationality());
        details.setEducation(detailsDTO.getEducation());
        details.setDegree(detailsDTO.getDegree());
        if (detailsDTO.getUser() != null
            && (details.getUser() == null
            || !details.getUser().getId().equals(detailsDTO.getUser()))) {
            final User user = userRepository.findUserByUid(detailsDTO.getUser())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
            details.setUser(user);
        }
        return details;
    }

}
