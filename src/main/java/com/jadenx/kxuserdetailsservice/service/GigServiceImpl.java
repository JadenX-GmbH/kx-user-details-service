package com.jadenx.kxuserdetailsservice.service;

import com.jadenx.kxuserdetailsservice.domain.Gig;
import com.jadenx.kxuserdetailsservice.model.GigDTO;
import com.jadenx.kxuserdetailsservice.repos.GigRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class GigServiceImpl implements GigService {

    private final GigRepository gigRepository;

    public GigServiceImpl(final GigRepository gigRepository) {
        this.gigRepository = gigRepository;
    }

    @Override
    public List<GigDTO> findAll() {
        return gigRepository.findAll()
            .stream()
            .map(gig -> mapToDTO(gig, new GigDTO()))
            .collect(Collectors.toList());
    }

    @Override
    public GigDTO get(final Long id) {
        return gigRepository.findById(id)
            .map(gig -> mapToDTO(gig, new GigDTO()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Long create(final GigDTO gigDTO) {
        final Gig gig = new Gig();
        mapToEntity(gigDTO, gig);
        return gigRepository.save(gig).getId();
    }

    @Override
    public void update(final Long id, final GigDTO gigDTO) {
        final Gig gig = gigRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(gigDTO, gig);
        gigRepository.save(gig);
    }

    @Override
    public void delete(final Long id) {
        gigRepository.deleteById(id);
    }

    private GigDTO mapToDTO(final Gig gig, final GigDTO gigDTO) {
        gigDTO.setId(gig.getId());
        gigDTO.setGigId(gig.getGigId());
        return gigDTO;
    }

    private Gig mapToEntity(final GigDTO gigDTO, final Gig gig) {
        gig.setGigId(gigDTO.getGigId());
        return gig;
    }

}
